package com.hzgc.service.device.service;

import com.hzgc.service.device.bean.WarnRule;
import com.hzgc.service.device.dao.HBaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WarnRuleService {

    @Autowired
    private HBaseDao hBaseDao;

    public Map <String, Boolean> configRules(List <String> ipcIDs, List <WarnRule> rules) {
        return hBaseDao.configRules(ipcIDs, rules);
    }

    /**
     * 添加布控规则
     *
     * @param ipcIDs 设备 ipcID 列表
     * @param rules  对比规则
     * @return boxChannelId 是否添加成功的 map
     */
    public Map <String, Boolean> addRules(List <String> ipcIDs, List <WarnRule> rules) {
        return hBaseDao.addRules(ipcIDs, rules);
    }

    /**
     * 获取设备的对比规则
     *
     * @param ipcID 设备 ipcID
     * @return 设备的布控规则
     */
    public List <WarnRule> getCompareRules(String ipcID) {
        return hBaseDao.getCompareRules(ipcID);
    }

    /**
     * 删除设备的布控规则
     *
     * @param ipcIDs 设备 ipcID 列表
     * @return channelId 是否删除成功的 map
     */
    public Map <String, Boolean> deleteRules(List <String> ipcIDs) {
        return hBaseDao.deleteRules(ipcIDs);
    }

    /**
     * 查看有多少设备绑定了此人员类型objectType
     *
     * @param objectType 识别库 id
     * @return ipcIDs
     */
    public List <String> objectTypeHasRule(String objectType) {
        return hBaseDao.objectTypeHasRule(objectType);
    }


    /**
     * 在所有设备列表绑定的规则中，删除此objectType
     *
     * @param objectType 对象类型
     * @param ipcIDs     设备列表
     * @return 删除成功的条数
     */
    public int deleteObjectTypeOfRules(String objectType, List <String> ipcIDs) {
        return hBaseDao.deleteObjectTypeOfRules(objectType, ipcIDs);
    }
}
