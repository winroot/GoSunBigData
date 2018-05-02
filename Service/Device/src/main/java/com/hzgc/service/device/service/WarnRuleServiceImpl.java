package com.hzgc.service.device.service;

import com.alibaba.fastjson.JSON;
import com.hzgc.common.service.table.column.DeviceTable;
import com.hzgc.common.util.object.ObjectUtil;
import com.hzgc.common.util.empty.IsEmpty;
import com.hzgc.common.service.connection.HBaseHelper;
import com.hzgc.service.device.bean.WarnRule;
import com.hzgc.service.device.dao.WarnRuleDao;
import com.hzgc.service.device.util.ChangeUtil;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class WarnRuleServiceImpl{

    private static Logger LOG = Logger.getLogger(WarnRuleServiceImpl.class);

    @Autowired
    private WarnRuleDao warnRuleDao;

    public Map <String, Boolean> configRules(List <String> ipcIDs, List <WarnRule> rules) {
        Map <String, Boolean> map = warnRuleDao.configRules(ipcIDs,rules);
        return map;
    }

    public Map <String, Boolean> addRules(List <String> ipcIDs, List <WarnRule> rules) {
        Map <String, Boolean> map = warnRuleDao.addRules(ipcIDs,rules);
        return map;
    }

    public List <WarnRule> getCompareRules(String ipcID) {
        List <WarnRule> list = warnRuleDao.getCompareRules(ipcID);
        return list;
    }

    public Map <String, Boolean> deleteRules(List <String> ipcIDs) {
        Map <String, Boolean> map = warnRuleDao.deleteRules(ipcIDs);
        return map;
    }

    public List <String> objectTypeHasRule(String objectType) {
        List <String> list = warnRuleDao.objectTypeHasRule(objectType);
        return list;
    }

    public int deleteObjectTypeOfRules(String objectType, List <String> ipcIDs) {
        int i = warnRuleDao.deleteObjectTypeOfRules(objectType,ipcIDs);
        return i;
    }
}
