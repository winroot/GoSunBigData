package com.hzgc.service.dispatch.controller;

import com.hzgc.common.util.json.JSONUtil;
import com.hzgc.service.dispatch.bean.*;
import com.hzgc.service.dispatch.service.WarnRuleService;
import com.hzgc.service.dispatch.util.IpcIdsUtil;
import com.hzgc.service.util.api.bean.DeviceDTO;
import com.hzgc.service.util.api.service.DeviceQueryService;
import com.hzgc.service.util.error.RestErrorCode;
import com.hzgc.service.util.response.ResponseResult;
import com.hzgc.service.util.rest.BigDataPath;
import com.hzgc.service.util.rest.BigDataPermission;
import io.swagger.annotations.*;

import java.io.IOException;
import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "warnRule", tags = {"告警规则"})
@Slf4j
public class WarnRuleController {

    @Autowired
    private WarnRuleService warnRuleService;

    @Autowired
    private DeviceQueryService deviceQueryService;

    @ApiOperation(value = "根据规则id获取规则详情", response = ResponseResult.class)
    @ApiImplicitParam(name = "id", value = "规则id", required = true, dataType = "string", paramType = "query")
    @RequestMapping(value = BigDataPath.DISPATCH_SEARCH_BYID, method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('" + BigDataPermission.RULE_VIEW + "')")
    public ResponseResult<Dispatch> getRuleInfo(String id) {
        if (null != id) {
            log.info("Get rule info , param is " + id);
            ResponseResult<Dispatch> responseResult = null;
            try {
                responseResult = warnRuleService.searchByRuleId(id);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseResult;
        } else {
            log.info("Get rule info , param is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
        }
    }

    @ApiOperation(value = "添加规则", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.DISPATCH_ADD, method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @PreAuthorize("hasAuthority('" + BigDataPermission.RULE_OPERATION + "')")
    public ResponseResult<String> addRule(@RequestBody @ApiParam(value = "规则配置参数", required = true) Dispatch dispatch) throws IOException {
        if (null != dispatch) {
            List<String> ipcIDs = new ArrayList<>();
            List<Warn> warnList;
            log.info("Add rule , param is " + JSONUtil.toJson(dispatch));
            //通过设备id查找ipcid
            List<Device> deviceList = dispatch.getDevices();
            List<Long> list = IpcIdsUtil.toDeviceIdList(deviceList);
            Map<String, DeviceDTO> map = deviceQueryService.getDeviceInfoByBatchId(list);
            //设置ipcid
            for (String s : map.keySet()) {
                DeviceDTO deviceDTO = map.get(s);
                String ipcid = deviceDTO.getSerial();
                if (null != ipcid){
                    ipcIDs.add(ipcid);
                    for (Device device:deviceList){
                        String id = device.getId();
                        if (id.equals(s)){
                            device.setIpcId(ipcid);
                        }
                    }
                }else {
                    return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT,"设备" + deviceDTO.getName() +
                            "未设置序列号,请配置序列号,重新添加");
                }
            }
            //参数封装
            warnList = dispatch.getRule().getWarns();
            Map<String, Dispatch> dispatchMap = IpcIdsUtil.toDispatchMap(dispatch);
            ResponseResult<String> responseResult = warnRuleService.saveOriginData(dispatchMap);
            if (responseResult.getHead().getErrorCode() == RestErrorCode.DB_DUPLICAET_KEY ||
                    responseResult.getHead().getErrorCode() == RestErrorCode.ERR_DEVICE_ALREADY_BIND_RULE) {
                return responseResult;
            }
            //调用大数据接口
            ipcIDs.removeAll(Collections.singleton(null));
            log.info("Bigdata param , ipcIDs is " + JSONUtil.toJson(ipcIDs) + " warnlist is " + JSONUtil.toJson(warnList));
            if (ipcIDs.size() > 0 && null != warnList && warnList.size() > 0) {
                warnRuleService.configRules(ipcIDs, warnList);
            }
            return responseResult;
        }
        log.info("Add rule , param is null");
        return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
    }

    @ApiOperation(value = "修改规则", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.DISPATCH_MODIFY, method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    @PreAuthorize("hasAuthority('" + BigDataPermission.RULE_OPERATION + "')")
    public ResponseResult<Boolean> updateRule(@RequestBody Dispatch dispatch) throws IOException {
        if (null != dispatch) {
            List<String> ipcIDs = new ArrayList<>();
            List<Warn> warnList;
            log.info("Update rule , param is " + JSONUtil.toJson(dispatch));
            List <Device> deviceList = dispatch.getDevices();
            List<Long> list = IpcIdsUtil.toDeviceIdList(deviceList);
            Map<String, DeviceDTO> map = deviceQueryService.getDeviceInfoByBatchId(list);
            for (String s : map.keySet()) {
                ipcIDs.add(map.get(s).getSerial());
            }
            //参数封装
            warnList = dispatch.getRule().getWarns();
            ResponseResult<Boolean> responseResult = warnRuleService.updateRule(dispatch);
            //调用大数据接口
            ipcIDs.removeAll(Collections.singleton(null));
            log.info("Bigdata param , ipcIDs is " + JSONUtil.toJson(ipcIDs) + " warn list is " + JSONUtil.toJson(warnList));
            if (ipcIDs.size() > 0 && null != warnList && warnList.size() > 0) {
                warnRuleService.configRules(ipcIDs, warnList);
            }
            return responseResult;
        }
        log.info("Update rule , param is null");
        return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
    }

    @ApiOperation(value = "删除规则", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.DISPATCH_DELETE, method = RequestMethod.DELETE, consumes = "application/json", produces = "application/json")
    @PreAuthorize("hasAuthority('" + BigDataPermission.RULE_OPERATION + "')")
    public ResponseResult<Boolean> delRules(@RequestBody IdsType<String> idsType) throws IOException {
        if (null != idsType) {
            log.info("Delete rules , param is " + idsType.toString());
            List<Long> ids = warnRuleService.delRules(idsType);
            Map<String, DeviceDTO> map = deviceQueryService.getDeviceInfoByBatchId(ids);
            List<String> ipcIDs = new ArrayList<>();
            for (String s : map.keySet()) {
                ipcIDs.add(map.get(s).getSerial());
            }
            ipcIDs.removeAll(Collections.singleton(null));
            //调用大数据接口
            log.info("Bigdata param , ipcIDs is " + JSONUtil.toJson(ipcIDs));
            warnRuleService.deleteRules(ipcIDs);
            return ResponseResult.init(true);
        }
        log.info("Delete rules , param is null");
        return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
    }

    @ApiOperation(value = "分页获取规则列表", response = ResponseResult.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "start", value = "分页开始", required = true, dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页数量", required = true, dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", value = "排序规则", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "fuzzy_field", value = "模糊查询字段", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "fuzzy_value", value = "模糊查询值", dataType = "string", paramType = "query")})
    @RequestMapping(value = BigDataPath.DISPATCH_CUTPAGE_RULE, method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('" + BigDataPermission.RULE_VIEW + "')")
    public ResponseResult<List> getRuleList(PageBean pageBean) throws IOException {
        if (null != pageBean) {
            log.info("Get rule list , param is " + JSONUtil.toJson(pageBean));
            return warnRuleService.getRuleList(pageBean);
        }
        log.info("Get rule list , param is null");
        return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
    }

    @ApiOperation(value = "获取某个规则绑定的所有设备", response = ResponseResult.class)
    @ApiImplicitParam(name = "rule_id", value = "规则id", required = true, dataType = "string", paramType = "query")
    @RequestMapping(value = "/getdevicelist/{rule_id}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('" + BigDataPermission.RULE_VIEW + "')")
    public ResponseResult<List> getDeviceList(String rule_id) throws IOException {
        if (null != rule_id) {
            log.info("Get device list , param is" + rule_id);
            return warnRuleService.getDeviceList(rule_id);
        }
        log.info("Get device list , param is null");
        return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
    }
}