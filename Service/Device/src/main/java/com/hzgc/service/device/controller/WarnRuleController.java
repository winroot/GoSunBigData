package com.hzgc.service.device.controller;

import com.hzgc.common.service.BigDataPath;
import com.hzgc.common.service.ResponseResult;
import com.hzgc.service.device.service.WarnRule;
import com.hzgc.service.device.service.WarnRuleServiceImpl;
import com.hzgc.service.device.vo.ConfigRuleVO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@FeignClient(name = "warnRule")
@RequestMapping(value = BigDataPath.WARNRULE)
public class WarnRuleController {

    private static Logger LOG = Logger.getLogger(WarnRuleController.class);

    @Autowired
    WarnRuleServiceImpl warnRuleService;

    @RequestMapping(value = BigDataPath.WARNRULE_CONFIG)
    public ResponseResult<Map<String, Boolean>> configRules(ConfigRuleVO configRuleVO) {
        List<String> ipcIDs;
        List<WarnRule> rules;
        if (null != configRuleVO) {
            ipcIDs = configRuleVO.getIpcIDs();
            rules = configRuleVO.getRules();
        } else {
            LOG.info("configRule参数错误");
            return null;
        }
        Map<String, Boolean> map = warnRuleService.configRules(ipcIDs, rules);
        return ResponseResult.init(map);
    }

    @RequestMapping(value = BigDataPath.WARNRULE_ADD)
    public ResponseResult<Map<String, Boolean>> addRules(ConfigRuleVO configRuleVO) {
        List<String> ipcIDs;
        List<WarnRule> rules;
        if (null != configRuleVO) {
            ipcIDs = configRuleVO.getIpcIDs();
            rules = configRuleVO.getRules();
        } else {
            return null;
        }
        Map<String, Boolean> map = warnRuleService.addRules(ipcIDs, rules);
        return ResponseResult.init(map);
    }

    @RequestMapping(value = BigDataPath.WARNRULE_GETCOMPARE)
    public ResponseResult<List<WarnRule>> getCompareRules(ConfigRuleVO configRuleVO) {
        String ipcID;
        if (null != configRuleVO) {
            ipcID = configRuleVO.getIpcID();
        } else {
            return null;
        }
        List<WarnRule> warnRuleList = warnRuleService.getCompareRules(ipcID);
        return ResponseResult.init(warnRuleList);
    }

    @RequestMapping(value = BigDataPath.WARNRULE_DELETE)
    public ResponseResult<Map<String, Boolean>> deleteRules(ConfigRuleVO configRuleVO) {
        List<String> ipcIdList;
        if (null != configRuleVO) {
            ipcIdList = configRuleVO.getIpcIDs();
        } else {
            return null;
        }
        Map<String, Boolean> map = warnRuleService.deleteRules(ipcIdList);
        return ResponseResult.init(map);
    }

    @RequestMapping(value = BigDataPath.WARNRULE_OBJECTTYPE_GET)
    public ResponseResult<List<String>> objectTypeHasRule(ConfigRuleVO configRuleVO) {
        String objectType;
        if (null != configRuleVO) {
            objectType = configRuleVO.getObjectType();
        } else {
            return null;
        }
        List<String> list = warnRuleService.objectTypeHasRule(objectType);
        return ResponseResult.init(list);
    }

    @RequestMapping(value = BigDataPath.WARNRULE_OBJECTTYPE_DELETE)
    public ResponseResult<Integer> deleteObjectTypeOfRules(ConfigRuleVO configRuleVO) {
        String objectType;
        List<String> ipcIdList;
        if (null != configRuleVO) {
            objectType = configRuleVO.getObjectType();
            ipcIdList = configRuleVO.getIpcIDs();
        } else {
            return null;
        }
        int i = warnRuleService.deleteObjectTypeOfRules(objectType, ipcIdList);
        return ResponseResult.init(i);
    }
}
