package com.hzgc.service.device.controller;

import com.hzgc.common.service.BigDataPath;
import com.hzgc.common.service.ResponseResult;
import com.hzgc.service.device.service.WarnRule;
import com.hzgc.service.device.service.WarnRuleService;
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
    WarnRuleService warnRuleServiceImpl;

    @RequestMapping(value = BigDataPath.WARNRULE_CONFIG)
    public ResponseResult<Map<String,Boolean>> configRules(ConfigRuleVO configRuleVO) {
        if(null != configRuleVO && null != configRuleVO.getIpcIDs() && null != configRuleVO.getRules()){
            Map<String,Boolean> map = warnRuleServiceImpl.configRules(configRuleVO.getIpcIDs(), configRuleVO.getRules());
            return ResponseResult.init(map);
        }
        LOG.info("configRule参数错误");
        return null;
    }

    @RequestMapping(value = BigDataPath.WARNRULE_ADD)
    public ResponseResult<Map<String,Boolean>> addRules(ConfigRuleVO configRuleVO) {
        if(null != configRuleVO && null != configRuleVO.getIpcIDs() && null != configRuleVO.getRules()){
            Map<String,Boolean> map = warnRuleServiceImpl.addRules(configRuleVO.getIpcIDs(), configRuleVO.getRules());
            return ResponseResult.init(map);
        }
        return null;
    }

    @RequestMapping(value = BigDataPath.WARNRULE_GETCOMPARE)
    public ResponseResult<List<WarnRule>> getCompareRules(ConfigRuleVO configRuleVO) {
        if(null != configRuleVO && null != configRuleVO.getIpcID()){
            List<WarnRule> list = warnRuleServiceImpl.getCompareRules(configRuleVO.getIpcID());
            return ResponseResult.init(list);
        }
        return null;
    }

    @RequestMapping(value = BigDataPath.WARNRULE_DELETE)
    public ResponseResult<Map<String,Boolean>> deleteRules(ConfigRuleVO configRuleVO) {
        if(null != configRuleVO && null != configRuleVO.getIpcIDs()){
            Map<String,Boolean> map = warnRuleServiceImpl.deleteRules(configRuleVO.getIpcIDs());
            return ResponseResult.init(map);
        }
        return null;
    }

    @RequestMapping(value = BigDataPath.WARNRULE_OBJECTTYPE_GET)
    public ResponseResult<List<String>> objectTypeHasRule(ConfigRuleVO configRuleVO) {
        if(null != configRuleVO && null != configRuleVO.getObjectType()){
            List<String> list = warnRuleServiceImpl.objectTypeHasRule(configRuleVO.getObjectType());
            return ResponseResult.init(list);
        }
        return null;
    }

    @RequestMapping(value = BigDataPath.WARNRULE_OBJECTTYPE_DELETE)
    public ResponseResult<Integer> deleteObjectTypeOfRules(ConfigRuleVO configRuleVO) {
        if(null != configRuleVO && null != configRuleVO.getIpcIDs() && null != configRuleVO.getObjectType()){
            int i = warnRuleServiceImpl.deleteObjectTypeOfRules(configRuleVO.getObjectType(), configRuleVO.getIpcIDs());
            return ResponseResult.init(i);
        }
        return null;
    }
}
