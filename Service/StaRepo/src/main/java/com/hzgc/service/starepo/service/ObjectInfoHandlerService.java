package com.hzgc.service.starepo.service;

import com.hzgc.common.table.starepo.ObjectInfoTable;
import com.hzgc.common.util.json.JSONUtil;
import com.hzgc.service.starepo.bean.*;
import com.hzgc.service.starepo.dao.PhoenixDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.hzgc.service.starepo.service.StaticProtocol.*;

@Service
@Slf4j
public class ObjectInfoHandlerService {

    @Autowired
    private PhoenixDao phoenixDao;
    @Autowired
    private StaticRepoProducer staticProducer;

    public ObjectInfoHandlerService() {
    }

    /**
     * 针对单个对象信息的添加处理  （外）（李第亮）
     * @param  platformId 表示的是平台的ID， 平台的唯一标识。
     * @param personObject K-V 对，里面存放的是字段和值之间的一一对应关系,
     *               例如：传入一个Map 里面的值如下map.put("idcard", "450722199502196939")
     *               表示的是身份证号（idcard）是450722199502196939，
     *               其中的K 的具体，请参考给出的数据库字段设计
     * @return 返回值为0，表示插入成功，返回值为1，表示插入失败
     */
    public Integer addObjectInfo(String platformId, Map<String, Object> personObject) {
        log.info("personObject: " + personObject.entrySet().toString());
        long start = System.currentTimeMillis();
        PersonObject person = PersonObject.mapToPersonObject(personObject);
        person.setPlatformid(platformId);
        log.info("the rowkey off this add person is: " + person.getId());
        //数据库操作
        Integer i = phoenixDao.addObjectInfo(person);
        log.info("添加一条数据到静态库花费时间： " + (System.currentTimeMillis() - start));
        if (personObject.get(ObjectInfoTable.FEATURE) != null) {
            StaticRepoObject object = new StaticRepoObject();
            object.setFeature((float[]) personObject.get(ObjectInfoTable.FEATURE));
            object.setPkey(person.getPkey());
            object.setRowkey(person.getId());
            staticProducer.sendKafkaMessage(INNERTOPIC, ADD, JSONUtil.toJson(object));
        }
        return i;
    }

    /**
     * 删除对象的信息  （外）（李第亮）
     * @param rowkeys 具体的一个人员信息的ID，值唯一
     * @return 返回值为0，表示删除成功，返回值为1，表示删除失败
     */
    public int deleteObjectInfo(List<String> rowkeys) {
        for (String rowkey : rowkeys) {
            staticProducer.sendKafkaMessage(INNERTOPIC, DELETE, rowkey);
        }
        return phoenixDao.deleteObjectInfo(rowkeys);
    }

    /**
     * 修改对象的信息   （外）（李第亮）
     * @param personObject K-V 对，里面存放的是字段和值之间的一一对应关系，参考添加里的描述
     * @return 返回值为0，表示更新成功，返回值为1，表示更新失败
     */
    public Integer updateObjectInfo(Map<String, Object> personObject) {
        log.info("personObject: " + personObject.entrySet().toString());
        long start = System.currentTimeMillis();
        String thePassId = (String) personObject.get(ObjectInfoTable.ROWKEY);
        if (thePassId == null) {
            log.info("the pass Id can not be null....");
            return 1;
        }
        //数据库更新操作
        Integer i = phoenixDao.updateObjectInfo(personObject);
        log.info("更新rowkey为: " + thePassId + "数据花费的时间是: " + (System.currentTimeMillis() - start));
        if (personObject.get(ObjectInfoTable.FEATURE) != null) {
            StaticRepoObject object = new StaticRepoObject();
            object.setFeature((float[]) personObject.get(ObjectInfoTable.FEATURE));
            object.setPkey((String)personObject.get(ObjectInfoTable.PKEY));
            object.setRowkey((String)personObject.get(ObjectInfoTable.ROWKEY));
            staticProducer.sendKafkaMessage(INNERTOPIC, UPDATE, JSONUtil.toJson(object));
        }
        return i;
    }

    /**
     * 根据rowkey 进行查询 （外）
     * @param id  标记一条对象信息的唯一标志。
     * @return  返回搜索所需要的结果封装成的对象，包含搜索id，成功与否标志，记录数，记录信息，照片id
     */
    public ObjectSearchResult searchByRowkey(String id) {
        long start = System.currentTimeMillis();
        ObjectSearchResult result = new ObjectSearchResult();
        List<PersonObject> personObjects = phoenixDao.searchByRowkey(id);
        if (null == personObjects){
            result.setSearchStatus(1);
        }else{
            result.setSearchStatus(0);
            PersonSingleResult personSingleResult = new PersonSingleResult();
            List<PersonSingleResult> personSingleResults = new ArrayList<>();
            personSingleResult.setSearchNums(1);
            personSingleResult.setPersons(personObjects);
            personSingleResults.add(personSingleResult);
            result.setFinalResults(personSingleResults);
        }
        log.info("获取一条数据的时间是：" + (System.currentTimeMillis() - start));
        return result;
    }

    /**
     * 可以匹配精确查找，以图搜索人员信息，模糊查找   （外）（李第亮）
     * @param pSearchArgsModel 搜索参数的封装
     * @return 返回搜索所需要的结果封装成的对象，包含搜索id，成功与否标志，记录数，记录信息，照片id
     */
    public ObjectSearchResult getObjectInfo(PSearchArgsModel pSearchArgsModel) {
        return phoenixDao.getObjectInfo(pSearchArgsModel);
    }


    /**
     * 根据rowkey 返回人员的照片
     * @param rowkey 人员在对象信息库中的唯一标志。
     * @return 图片的byte[] 数组
     */
    public Byte getPhotoByKey(String rowkey) {
        return phoenixDao.getPhotoByKey(rowkey);
    }


    /**
     * 根据传过来的搜索rowkey 返回搜索记录 （外） （李第亮）
     * @param  searchRecordOpts 历史查询参数
     * @return  返回一个ObjectSearchResult 对象，
     * @author 李第亮
     * 里面包含了本次查询ID，查询成功标识，
     * 查询照片ID（无照片，此参数为空），结果数，人员信息列表
     */
    public ObjectSearchResult getRocordOfObjectInfo(SearchRecordOpts searchRecordOpts) {
        log.info("searchRecordOpts: " + searchRecordOpts);
        // 传过来的参数中为空，或者子查询为空，或者子查询大小为0，都返回查询错误。
        if (searchRecordOpts == null) {
            log.info("SearchRecordOpts 为空，请确认参数是否正确.");
            return null;
        }
        // 总的searchId
        List<SubQueryOpts> subQueryOptsList = searchRecordOpts.getSubQueryOptsList();
        if (subQueryOptsList == null || subQueryOptsList.size() == 0) {
            log.info("子查询列表为空，请确认参数是否正确.");
            return null;
        }

        SubQueryOpts subQueryOpts = subQueryOptsList.get(0);
        if (subQueryOpts == null) {
            log.info("子查询对象SubQueryOpts 对象为空，请确认参数是否正确.");
            return null;
        }

        // 子查询Id
        String subQueryId = subQueryOpts.getQueryId();
        if (subQueryId == null) {
            log.info("子查询Id 为空");
            return null;
        }
        PersonSingleResult personSingleResult = phoenixDao.getRocordOfObjectInfo(subQueryId);
        // 需要分组的pkeys
        List<String> pkeys = subQueryOptsList.get(0).getPkeys();
        // 排序参数
        List<StaticSortParam> staticSortParams = searchRecordOpts.getStaticSortParams();
        ObjectSearchResult finnalObjectSearchResult = new ObjectSearchResult();
        List<PersonSingleResult> personSingleResults = new ArrayList<>();
        if (personSingleResult != null) {
            List<PersonObject> personObjects = personSingleResult.getPersons();
            List<GroupByPkey> groupByPkeys = new ArrayList<>();
            GroupByPkey groupByPkey;
            if (personObjects != null && staticSortParams != null && staticSortParams.contains(StaticSortParam.PEKEY)) {
                Map<String, List<PersonObject>> groupingByPkeys = personObjects.stream()
                        .collect(Collectors.groupingBy(PersonObject::getPkey));
                for (Map.Entry<String, List<PersonObject>> entry : groupingByPkeys.entrySet()) {
                    groupByPkey = new GroupByPkey();
                    String pkey = entry.getKey();
                    groupByPkey.setPkey(pkey);
                    List<PersonObject> personObjectList = entry.getValue();
                    // 对结果进行排序
                    new ObjectInfoHandlerTool().sortPersonObject(personObjectList, staticSortParams);

                    // 如果指定了需要返回的Pkey
                    if (pkeys != null && pkeys.size() > 0 && pkeys.contains(pkey)) {
                        groupByPkey.setPersons(personObjectList);
                        groupByPkeys.add(groupByPkey);
                        continue;
                    }
                    if (pkeys == null || pkeys.size() == 0) {
                        groupByPkey.setPersons(personObjectList);
                        groupByPkeys.add(groupByPkey);
                    }
                }
                personSingleResult.setGroupByPkeys(groupByPkeys);
                personSingleResult.setPersons(null);
            } else if (personObjects != null && staticSortParams != null && !staticSortParams.contains(StaticSortParam.PEKEY)) {
                personSingleResult.setGroupByPkeys(null);
                new ObjectInfoHandlerTool().sortPersonObject(personObjects, staticSortParams);
                personSingleResult.setPersons(personObjects);
            }
        }
        personSingleResults.add(personSingleResult);
        finnalObjectSearchResult.setSearchStatus(0);
        finnalObjectSearchResult.setFinalResults(personSingleResults);
        int pageSize = searchRecordOpts.getSize();
        int start = searchRecordOpts.getStart();
        new ObjectInfoHandlerTool().formatTheObjectSearchResult(finnalObjectSearchResult, start, pageSize);
        log.info("***********************");
        log.info(finnalObjectSearchResult.toString());
        log.info("***********************");
        return finnalObjectSearchResult;
    }
    /**
     * 根据穿过来的rowkey 返回照片 （外） （李第亮）
     * @param rowkey 即Hbase 数据库中的rowkey，查询记录唯一标志
     * @return 返回查询的照片
     */
    public byte[] getSearchPhoto(String rowkey) {
        return null;
    }
}

class StaticRepoObject {
    private float[] feature;
    private String pkey;
    private String rowkey;

    float[] getFeature() {
        return feature;
    }

    public void setFeature(float[] feature) {
        this.feature = feature;
    }

    String getPkey() {
        return pkey;
    }

    public void setPkey(String pkey) {
        this.pkey = pkey;
    }

    String getRowkey() {
        return rowkey;
    }

    public void setRowkey(String rowkey) {
        this.rowkey = rowkey;
    }
}


class StaticProtocol {
    static String INNERTOPIC = "staticrepo";
    static final String DELETE = "DELETE";
    static final String ADD = "ADD";
    static final String UPDATE = "UPDATE";
}