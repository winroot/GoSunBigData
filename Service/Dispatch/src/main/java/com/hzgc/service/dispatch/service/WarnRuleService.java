package com.hzgc.service.dispatch.service;

import com.hzgc.common.service.error.RestErrorCode;
import com.hzgc.common.service.response.ResponseResult;
import com.hzgc.common.util.json.JSONUtil;
import com.hzgc.service.dispatch.bean.Dispatch;
import com.hzgc.service.dispatch.bean.IdsType;
import com.hzgc.service.dispatch.bean.PageBean;
import com.hzgc.service.dispatch.bean.Warn;
import com.hzgc.service.dispatch.dao.HBaseDao;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WarnRuleService {

    @Autowired
    @SuppressWarnings("unused")
    private HBaseDao hBaseDao;

    public void configRules(List<String> ipcIDs, List<Warn> warns) {
        hBaseDao.configRules(ipcIDs, warns);
    }

    /**
     * 删除设备的布控规则
     *
     * @param ipcIDs 设备 ipcID 列表
     */
    public void deleteRules(List<String> ipcIDs) {
        hBaseDao.deleteRules(ipcIDs);
    }

    //存储原数据
    public ResponseResult<String> saveOriginData(Map<String, Dispatch> map) throws IOException {
        return this.hBaseDao.saveOriginData(map);
    }

    //根据ruleId进行全部参数查询
    public ResponseResult<Dispatch> searchByRuleId(String id) throws IOException {
        Map<String, Dispatch> map = hBaseDao.searchByRuleId();
        for (String ruleId : map.keySet()) {
            if (ruleId.equals(id)) {
                Dispatch dispatch = map.get(ruleId);
                List<Warn> warnList = dispatch.getRule().getWarns();
                String[] strings = new String[warnList.size()];
                for (int i = 0; i < warnList.size(); i++) {
                    strings[i] = (warnList.get(i)).getObjectType();
                }
                log.info("Strings is " + JSONUtil.toJson(strings));
                Map<String, Map<String, String>> responseResult = hBaseDao.getObjectTypeName(strings);
                Map<String, String> m = responseResult.get("restbody");
                for (Warn warn : warnList) {
                    for (String s : m.keySet()) {
                        if (warn.getObjectType().equals(s)) {
                            if (null != m.get(s)) {
                                warn.setObjectTypeName(m.get(s));
                            }
                        }
                    }
                }
                log.info("Dispatch all info is " + dispatch.toString());
                return ResponseResult.init(dispatch);
            }
        }
        log.info("Id query Data is null");
        return ResponseResult.error(RestErrorCode.RECORD_NOT_EXIST);
    }

    //修改规则
    public ResponseResult<Boolean> updateRule(Dispatch dispatch) throws IOException {
        return hBaseDao.updateRule(dispatch);
    }

    //删除规则
    public List<Long> delRules(IdsType<String> idsType) throws IOException {
        return hBaseDao.delRules(idsType);
    }

    //分页获取规则列表
    public ResponseResult<List> getRuleList(PageBean pageBean) throws IOException {
        return hBaseDao.getRuleList(pageBean);
    }

    //获取某个规则绑定的所有设备
    public ResponseResult<List> getDeviceList(String rule_id) throws IOException {
        return hBaseDao.getDeviceList(rule_id);
    }

}