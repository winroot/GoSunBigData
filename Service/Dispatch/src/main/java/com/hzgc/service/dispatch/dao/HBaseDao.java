package com.hzgc.service.dispatch.dao;

import com.alibaba.fastjson.JSON;
import com.hzgc.common.hbase.HBaseHelper;
import com.hzgc.common.table.device.DeviceTable;
import com.hzgc.common.util.json.JSONUtil;
import com.hzgc.common.util.object.ObjectUtil;
import com.hzgc.service.dispatch.bean.*;
import com.hzgc.service.dispatch.util.JsonToMap;
import com.hzgc.service.dispatch.bean.Device;
import com.hzgc.service.dispatch.bean.IdsType;
import com.hzgc.service.dispatch.bean.PageBean;
import com.hzgc.service.dispatch.bean.Warn;
import com.hzgc.service.util.error.RestErrorCode;
import com.hzgc.service.util.response.ResponseResult;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

@Repository
@Slf4j
public class HBaseDao {

    @Autowired
    private RestTemplate restTemplate = null;

    public HBaseDao() {
        HBaseHelper.getHBaseConnection();
    }

    public Map <String, Boolean> configRules(List <String> ipcIDs, List <Warn> rules) {
        //从Hbase读device表
        Table deviceTable = HBaseHelper.getTable(DeviceTable.TABLE_DEVICE);
        //初始化设备布控预案对象Map<告警类型，Map<对象类型，阈值>>
        Map <Integer, Map <String, Integer>> commonRule = new HashMap <>();
        //初始化离线告警对象Map<对象类型，Map<设备ID，离线天数>>
        Map <String, Map <String, Integer>> offlineMap = new HashMap <>();
        List <Put> putList = new ArrayList <>();
        //reply表示：是否添加成功
        Map <String, Boolean> reply = new HashMap <>();

        // 把传进来的rules：List<Warn> rules转化为commonRule：Map<Integer, Map<String, Integer>>格式
        String jsonString = parseDeviceRule(rules, ipcIDs, commonRule);
        byte[] commonRuleBytes = ObjectUtil.objectToByte(jsonString);
        for (String ipcID : ipcIDs) {
            //“以传入的设备ID为行键，device为列族，告警类型w为列，commonRuleBytes为值”，的Put对象添加到putList列表
            Put put = new Put(Bytes.toBytes(ipcID));
            put.addColumn(DeviceTable.CF_DEVICE, DeviceTable.WARN, commonRuleBytes);
            putList.add(put);
            //对于每一条规则
            for (Warn rule : rules) {
                //解析离线告警：在离线告警offlineMap中添加相应的对象类型、ipcID和规则中的离线天数阈值DayThreshold
                parseOfflineWarn(rule, ipcID, offlineMap);
            }
            reply.put(ipcID, true);
        }
        try {
            //把putList列表添加到表device表中
            deviceTable.put(putList);
            log.info("Config rules are " + jsonString);
            //config模式下，把离线告警offlineMap对象插入到device表中
            configOfflineWarn(offlineMap, deviceTable);
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            HBaseHelper.closeTable(deviceTable);
        }
        return reply;
    }

    public Map <String, Boolean> deleteRules(List <String> ipcIDs) {
        //获取device表
        Table deviceTable = HBaseHelper.getTable(DeviceTable.TABLE_DEVICE);
        Map <String, Boolean> reply = new HashMap <>();
        List <Delete> deviceDelList = new ArrayList <>();
        String id = "";
        //若传入的设备ID不为空
        if (ipcIDs != null && ipcIDs.size()>0) {
            try {
                //对于每一个设备ID，删除其在device表中对应的列族、列
                for (String ipc : ipcIDs) {
                    id = ipc;
                    Delete deviceDelete = new Delete(Bytes.toBytes(ipc));
                    //列族：device，列：w
                    deviceDelete.addColumns(DeviceTable.CF_DEVICE, DeviceTable.WARN);
                    deviceDelList.add(deviceDelete);
                    //reply：（设备ID，删除成功）
                    reply.put(ipc, true);
                    log.info("Release the rule binding, the device ID is:" + ipc);
                }
                //获取离线告警数据的行键
                Get offlineGet = new Get(DeviceTable.OFFLINERK);
                Result offlineResult = deviceTable.get(offlineGet);
                //若离线告警数据非空
                if (!offlineResult.isEmpty()) {
                    //将离线告警数据反序列化
                    byte[] bytes = offlineResult.getValue(DeviceTable.CF_DEVICE, DeviceTable.OFFLINECOL);
                    String offlineStr = null;
                    if (bytes != null) {
                        offlineStr = Bytes.toString(bytes);
                    }
                    Map <String, Map <String, Integer>> offlineMap = JsonToMap.stringToMap(offlineStr);
                    /*
                     * offlineMap：Map<String, Map<String, Integer>>
                     *                      对象类型      设备ID,离线天数
                     */
                    //对于离线告警数据中的每个对象类型
                    for (String type : offlineMap.keySet()) {
                        //对于每个设备ID
                        for (String ipc : ipcIDs) {
                            //删除设备ID对应的键值内容
                            offlineMap.get(type).remove(ipc);
                        }
                    }
                    //把删除后的离线告警数据存入device表中
                    Put offlinePut = new Put(DeviceTable.OFFLINERK);
                    String offline = JSONUtil.toJson(offlineMap);
                    offlinePut.addColumn(DeviceTable.CF_DEVICE, DeviceTable.OFFLINECOL, Bytes.toBytes(offline));
                    deviceTable.put(offlinePut);
                    log.info("Delete rule is " + offline);
                }
                deviceTable.delete(deviceDelList);
                return reply;
            } catch (IOException e) {
                reply.put(id, false);
                log.error(e.getMessage());
            } finally {
                HBaseHelper.closeTable(deviceTable);
            }
        }
        return reply;
    }

    /**
     * config模式下配置离线告警（内部方法）（config模式下，把离线告警offlineMap对象插入到device表中）
     * 离线告警数据类型offlineMap：Map<String, Map<String, Integer>>
     * 对象类型,   设备ID, 离线天数
     * tempMap：device表中的值
     */
    private void configOfflineWarn(Map <String, Map <String, Integer>> offlineMap, Table deviceTable) {
        try {
            String offlineString;
            Get offlinGet = new Get(DeviceTable.OFFLINERK);
            //获取device表中offlineWarnRowKey行键对应的数据
            Result offlineResult = deviceTable.get(offlinGet);
            //若device表中offlineWarnRowKey行键对应的数据offlineResult非空（value中有值）
            if (!offlineResult.isEmpty()) {
                //反序列化该值类型（转化为Object）
                byte[] bytes = offlineResult.getValue(DeviceTable.CF_DEVICE, DeviceTable.OFFLINECOL);
                String tempMapStr = null;
                if (bytes != null) {
                    tempMapStr = Bytes.toString(bytes);
                }
                Map <String, Map <String, Integer>> tempMap = JsonToMap.stringToMap(tempMapStr);
                //对于离线告警offlineMap中的每个对象类型
                for (String type : offlineMap.keySet()) {
                    //假如Hbase数据库中的device表中包含离线告警offlineMap的对象类型
                    if (tempMap.containsKey(type)) {
                        //对于离线告警offlineMap中的每一个设备ID
                        for (String ipc : offlineMap.get(type).keySet()) {
                            //覆盖device表中原有的值。offlineMap.get(type).get(ipc)：离线天数
                            tempMap.get(type).put(ipc, offlineMap.get(type).get(ipc));
                        }
                    } else {
                        /*
                         * 假如Hbase数据库中的device表中不包含离线告警offlineMap的对象类型，
                         * 直接向device表中添加离线告警offlineMap中的值
                         */
                        tempMap.put(type, offlineMap.get(type));
                    }
                }
                Put offlinePut = new Put(DeviceTable.OFFLINERK);
                offlineString = JSONUtil.toJson(offlineMap);
                offlinePut.addColumn(DeviceTable.CF_DEVICE, DeviceTable.OFFLINECOL, Bytes.toBytes(offlineString));
                deviceTable.put(offlinePut);
            } else {
                //若hbase的device表中offlineWarnRowKey行键对应的数据为空，直接把offlineMap的值加入到device表
                Put offlinePut = new Put(DeviceTable.OFFLINERK);
                offlineString = JSONUtil.toJson(offlineMap);
                offlinePut.addColumn(DeviceTable.CF_DEVICE, DeviceTable.OFFLINECOL, Bytes.toBytes(offlineString));
                deviceTable.put(offlinePut);
            }
            log.info("configRules are " + offlineString);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


    /**
     * 解析configRules()传入的布控规则，并在解析的同时同步其他相关数据（内部方法）
     * 把传进来的rules：List<Warn> rules转化为commonRule：Map<Integer, Map<String, Integer>>格式
     */

    private String parseDeviceRule(List <Warn> rules, List <String> ipcIDs, Map <Integer, Map <String, Integer>> commonRule) {
        //判断：规则不为空，设备ID不为空
        if (rules != null && commonRule != null && ipcIDs != null) {
            for (Warn rule : rules) {
                /*
                 * code：告警类型。0：识别告警；1：新增告警；2：离线告警
                 * rules：传入的规则，List<Warn>格式；
                 * commonRule：设备布控预案，需转化成的Map<Integer, Map<String, Integer>>格式
                 */

                //IDENTIFY：识别告警；ADDED：新增告警。若传入的规则rules中的告警类型为这两者
                if (Objects.equals(rule.getAlarmType(), DeviceTable.IDENTIFY) || Objects.equals(rule.getAlarmType(), DeviceTable.ADDED)) {
                    //判断commonRule的键（告警类型）是否是传入的规则rule中的告警类型。
                    if (commonRule.containsKey(rule.getAlarmType())) {
                        //如果之前存在对比规则，覆盖之前的规则（相当于清除后写入）
                        commonRule.get(rule.getAlarmType()).put(rule.getObjectType(), rule.getThreshold());
                    } else {
                        //如果之前不存在对比规则，直接写入
                        Map <String, Integer> temMap = new HashMap <>();
                        //getThreshold()：识别或新增告警需要的相似度阈值
                        temMap.put(rule.getObjectType(), rule.getThreshold());
                        commonRule.put(rule.getAlarmType(), temMap);
                    }
                }

                //OFFLINE：离线告警。若传入的规则rules中的告警类型为离线告警
                if (Objects.equals(rule.getAlarmType(), DeviceTable.OFFLINE)) {
                    //如果之前存在对比规则，先清除之前的规则，再重新写入
                    if (commonRule.containsKey(rule.getAlarmType())) {
                        commonRule.get(rule.getAlarmType()).put(rule.getObjectType(), rule.getOfflineDayThreshold());
                    } else {
                        //如果之前不存在对比规则，直接写入
                        Map <String, Integer> tempMap = new HashMap <>();
                        //getDayThreshold()：离线告警需要的离线天数
                        tempMap.put(rule.getObjectType(), rule.getOfflineDayThreshold());
                        commonRule.put(rule.getAlarmType(), tempMap);
                    }
                }
            }
        }
        return JSONUtil.toJson(commonRule);
    }

    /**
     * 解析离线告警（内部方法）
     * 离线告警数据类型Map<String, Map<String, Integer>>
     * 对象类型,   设备ID, 离线天数
     */
    private void parseOfflineWarn(Warn rule, String ipcID, Map <String, Map <String, Integer>> offlineMap) {
        //离线告警中存在传入规则中的对象类型
        if (offlineMap.containsKey(rule.getObjectType())) {
            //在离线告警相应的对象类型中添加ipcID和规则中的离线天数阈值DayThreshold
            offlineMap.get(rule.getObjectType()).put(ipcID, rule.getOfflineDayThreshold());
        } else {
            //若离线告警中不存在传入规则中的对象类型
            Map <String, Integer> ipcMap = new HashMap <>();
            ipcMap.put(ipcID, rule.getOfflineDayThreshold());
            offlineMap.put(rule.getObjectType(), ipcMap);
        }
    }

    //添加规则进行判断这个ipcId是不是已经存在，存在的话返回该设备已经绑定了规则，不存在直接进行添加
    public ResponseResult<String> saveOriginData(Map<String,Dispatch> originMap)throws IOException{
        log.info("Origin map is "+ JSONUtil.toJson(originMap));
        String originId;
        String oldId;
        //从Hbase数据库中读dispatchTable表
        Table dispatchTable = HBaseHelper.getTable("dispatchTable");
        Get get = new Get(Bytes.toBytes("ruleId"));
        get.addColumn(Bytes.toBytes("dispatch"),Bytes.toBytes("dispatchObj"));
        Result result = dispatchTable.get(get);
        byte[] dispatchObj = result.getValue(Bytes.toBytes("dispatch"),Bytes.toBytes("dispatchObj"));
        if(null != dispatchObj){
            String hbaseMapString = (String)ObjectUtil.byteToObject(dispatchObj);
            LinkedHashMap<String,Dispatch> hbaseMap = JsonToMap.dispatchStringToMap(hbaseMapString);
            //进行id对比是否存在新添加的id
            for (String newRuleId:originMap.keySet()){
                //拿到新添加的设备集合
                List<Device> newDeviceList = originMap.get(newRuleId).getDevices();
                for (Device newDevice:newDeviceList){
                    //拿到新添加的ipcId
                    originId = newDevice.getId();
                    for (String oldRuleId:hbaseMap.keySet()){
                        //拿到数据库中的设备集合
                        List<Device> oldDeviceList = hbaseMap.get(oldRuleId).getDevices();
                        for (Device oldDevice:oldDeviceList){
                            //拿到旧的ipcId
                            oldId = oldDevice.getId();
                            //进行比对，如果之前绑定了规则，直接返回报错
                            if (oldId.equals(originId)){
                                String name = newDevice.getName();
                                log.info("This deviceId is already binded");
                                ResponseResult<String> responseResult = ResponseResult.init(name+"已经绑定了规则");
                                return responseResult;
                            }
                        }
                    }
                }
                log.info("This deviceID is not bind");
                hbaseMap.put(newRuleId,originMap.get(newRuleId));
            }
            //把新数据同步到数据库中
            Put put = new Put(Bytes.toBytes("ruleId"));
            put.addColumn(Bytes.toBytes("dispatch"),Bytes.toBytes("dispatchObj"), ObjectUtil.objectToByte(JSON.toJSONString(hbaseMap)));
            dispatchTable.put(put);
            log.info("Hbase map " + JSON.toJSONString(hbaseMap));
            return ResponseResult.init("规则添加成功");
        }else{
            //数据库为空表示第一次增加，直接存到数据库中
            Put put = new Put(Bytes.toBytes("ruleId"));
            put.addColumn(Bytes.toBytes("dispatch"),Bytes.toBytes("dispatchObj"), ObjectUtil.objectToByte(JSON.toJSONString(originMap)));
            dispatchTable.put(put);
            log.info("This is first add originMap is "+ JSONUtil.toJson(originMap));
            return ResponseResult.init("规则添加成功");
        }
    }

    //根据ruleId进行全部参数查询
    public Map<String,Dispatch> searchByRuleId() throws IOException {
            Table dispatchTable = HBaseHelper.getTable("dispatchTable");
            Get get = new Get(Bytes.toBytes("ruleId"));
            get.addColumn(Bytes.toBytes("dispatch"),Bytes.toBytes("dispatchObj"));
            Result result = dispatchTable.get(get);
            byte[] bytes = result.getValue(Bytes.toBytes("dispatch"),Bytes.toBytes("dispatchObj"));
            if (null != bytes){
                String hbaseMapString = (String)ObjectUtil.byteToObject(bytes);
                LinkedHashMap<String,Dispatch> map = JsonToMap.dispatchStringToMap(hbaseMapString);
                if (null == map){
                    return null;
                }else {
                    return map;
                }
            }
            log.info("Hbase data is null");
            return null;
    }

    //修改规则
    public ResponseResult<Boolean> updateRule(Dispatch dispatch) throws IOException {
        Table dispatchTable = HBaseHelper.getTable("dispatchTable");
        Get get = new Get(Bytes.toBytes("ruleId"));
        get.addColumn(Bytes.toBytes("dispatch"),Bytes.toBytes("dispatchObj"));
        Result result = dispatchTable.get(get);
        byte[] bytes = result.getValue(Bytes.toBytes("dispatch"),Bytes.toBytes("dispatchObj"));
        if (null != bytes){
            String hbaseMapString = (String)ObjectUtil.byteToObject(bytes);
            LinkedHashMap<String,Dispatch> map = JsonToMap.dispatchStringToMap(hbaseMapString);
            log.info("Before update map is "+ JSONUtil.toJson(map));
            //需要修改的规则id
            String ruleId = dispatch.getRule().getRuleId();
            for (String rule_id:map.keySet()){
                if (rule_id.equals(ruleId)){
                    map.put(rule_id,dispatch);
                    Put put = new Put(Bytes.toBytes("ruleId"));
                    put.addColumn(Bytes.toBytes("dispatch"),Bytes.toBytes("dispatchObj"), ObjectUtil.objectToByte(JSONUtil.toJson(map)));
                    dispatchTable.put(put);
                    log.info("Later update map is "+ JSONUtil.toJson(map));
                    return ResponseResult.init(true);
                }
            }
        }
        log.info("Hbase data is null");
        return ResponseResult.error(RestErrorCode.RECORD_NOT_EXIST);
    }

    //删除规则
    @SuppressWarnings("UnnecessaryLocalVariable")
    public List<Long> delRules(IdsType<String> idsType) throws IOException {
        if (null != idsType){
            Table dispatchTable = HBaseHelper.getTable("dispatchTable");
            Get get = new Get(Bytes.toBytes("ruleId"));
            get.addColumn(Bytes.toBytes("dispatch"),Bytes.toBytes("dispatchObj"));
            Result result = dispatchTable.get(get);
            byte[] bytes = result.getValue(Bytes.toBytes("dispatch"),Bytes.toBytes("dispatchObj"));
            if (null != bytes){
                List<Long> ids = new ArrayList<>();
                String hbaseMapString = (String)ObjectUtil.byteToObject(bytes);
                LinkedHashMap<String,Dispatch> map = JsonToMap.dispatchStringToMap(hbaseMapString);
                log.info("Before delete map is "+ JSONUtil.toJson(map));
                //判断是否存在需要删除的ruleID
                for (String idType:idsType.getId()){
                    if (map.containsKey(idType)){
                        //获取大数据传参需要的参数
                        List<Device> deviceList = map.get(idType).getDevices();
                        for (Device device:deviceList){
                            ids.add(Long.valueOf(device.getId()));
                        }
                        //移除数据
                        map.remove(idType);
                    }
                }
                Put put = new Put(Bytes.toBytes("ruleId"));
                put.addColumn(Bytes.toBytes("dispatch"),Bytes.toBytes("dispatchObj"), ObjectUtil.objectToByte(JSONUtil.toJson(map)));
                dispatchTable.put(put);
                log.info("Later delete map is "+ JSONUtil.toJson(map));
                return ids;
            }
        }
        log.info("Hbase data is null");
        return null;
    }

    //分页获取规则列表
    public ResponseResult<List> getRuleList(PageBean pageBean) throws IOException {
        List<Rule> list = new ArrayList<>();
        Table dispatchTable = HBaseHelper.getTable("dispatchTable");
        Get get = new Get(Bytes.toBytes("ruleId"));
        get.addColumn(Bytes.toBytes("dispatch"),Bytes.toBytes("dispatchObj"));
        Result result = dispatchTable.get(get);
        byte[] bytes = result.getValue(Bytes.toBytes("dispatch"),Bytes.toBytes("dispatchObj"));
        if (null != bytes){
            List<Rule> cutList = null;
            String hbaseMapString = (String)ObjectUtil.byteToObject(bytes);
            LinkedHashMap<String,Dispatch> map = JsonToMap.dispatchStringToMap(hbaseMapString);
            for (String ruleId:map.keySet()){
                Rule rule = map.get(ruleId).getRule();
                list.add(rule);
            }
            log.info("Origin data is "+ JSONUtil.toJson(list));

            //模糊查询
            List<Rule> likeList = new ArrayList <>();
            if (null != pageBean.getFuzzy_field() && null != pageBean.getFuzzy_value()){
                for (Rule rule:list){
                    String value = rule.getName();
                    if ("name".equals(pageBean.getFuzzy_field())){
                        if (value.contains(pageBean.getFuzzy_value())){
                            likeList.add(rule);
                        }
                    }
                }
                log.info("Like query data is "+ JSONUtil.toJson(likeList));
                // 分页
                if (null != pageBean.getStart() && null != pageBean.getLimit() && likeList.size()>0 && likeList.size()>pageBean.getStart() && likeList.size()>(list.size()+pageBean.getLimit())){
                        cutList = likeList.subList(pageBean.getStart(),pageBean.getLimit());
                        log.info("Cutpage data is "+ JSONUtil.toJson(cutList));
                        ResponseResult<List> responseResult = ResponseResult.init(cutList,(long) likeList.size());
                        return responseResult;
                } else if (null != pageBean.getStart() && null != pageBean.getLimit() && likeList.size()<pageBean.getStart() || likeList.size()==pageBean.getStart()){
                    log.info("Query data is null");
                    return ResponseResult.init(new ArrayList(),0L);
                }else if (null != pageBean.getStart() && null != pageBean.getLimit() && likeList.size()>0 && likeList.size()>pageBean.getStart() && likeList.size()<(likeList.size()+pageBean.getLimit())){
                        cutList = likeList.subList(pageBean.getStart(),likeList.size());
                        log.info("Cutpage data is "+ JSONUtil.toJson(cutList));
                        ResponseResult<List> responseResult = ResponseResult.init(cutList,(long) likeList.size());
                        return responseResult;
                }
            }
            // 分页
            if (null != pageBean.getStart() && null != pageBean.getLimit() && list.size()>0 && list.size()>pageBean.getStart() && list.size()>(list.size()+pageBean.getLimit())){
                cutList = list.subList(pageBean.getStart(),pageBean.getLimit());
            } else if (null != pageBean.getStart() && null != pageBean.getLimit() && list.size()<pageBean.getStart() || list.size()==pageBean.getStart()){
                log.info("Query data is null");
                return ResponseResult.init(new ArrayList(),0L);
            }else if (null != pageBean.getStart() && null != pageBean.getLimit() && list.size()>0 && list.size()>pageBean.getStart() && list.size()<(list.size()+pageBean.getLimit())){
                cutList = list.subList(pageBean.getStart(),list.size());
            }else {
                log.info("Pagebean param is error");
            }
            log.info("Cutpage data is "+ JSONUtil.toJson(cutList));
            ResponseResult<List> responseResult = ResponseResult.init(cutList,(long) list.size());
            return responseResult;
        }
        log.info("Hbase data is null");
        return ResponseResult.init(new ArrayList(),0L);
    }

    //获取某个规则绑定的所有设备
    public ResponseResult<List> getDeviceList(String rule_id) throws IOException {
        Table dispatchTable = HBaseHelper.getTable("dispatchTable");
        Get get = new Get(Bytes.toBytes("ruleId"));
        get.addColumn(Bytes.toBytes("dispatch"),Bytes.toBytes("dispatchObj"));
        Result result = dispatchTable.get(get);
        byte[] bytes = result.getValue(Bytes.toBytes("dispatch"),Bytes.toBytes("dispatchObj"));
        if (null != bytes){
            String hbaseMapString = (String)ObjectUtil.byteToObject(bytes);
            LinkedHashMap<String,Dispatch> map = JsonToMap.dispatchStringToMap(hbaseMapString);
            List<Device> deviceList = map.get(rule_id).getDevices();
            log.info("One ruleID all Devices is "+ JSON.toJSONString(deviceList));
            return ResponseResult.init(deviceList);
        }
        log.info("Hbase data is null");
        return ResponseResult.error(RestErrorCode.RECORD_NOT_EXIST);
    }

    //获取对象类型名称
    @HystrixCommand(fallbackMethod = "getObjectTypeNameError")
    @SuppressWarnings("unchecked")
    public Map<String,Map<String,String>> getObjectTypeName(String[] strings) {
        if (null != strings && strings.length>0){
            Map<String,Map<String,String>> map = restTemplate.postForObject("http://STAREPO/type_search_names",strings,Map.class);
            log.info("StaRepo return param is "+JSONUtil.toJson(map));
            return map;
        }
            log.info("Start staRepo param is null");
            return null;
    }

    @SuppressWarnings("unused")
    public Map<String, Map<String,String>> getObjectTypeNameError(String[] strings) {
        log.error("Get objectTypeName error, strings is:" + strings);
        return new HashMap<>();
    }
}
