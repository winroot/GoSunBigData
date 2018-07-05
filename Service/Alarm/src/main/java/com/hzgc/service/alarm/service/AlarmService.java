package com.hzgc.service.alarm.service;

import com.hzgc.service.alarm.bean.AlarmBean;
import com.hzgc.service.alarm.bean.UserAlarmMessage;
import com.hzgc.service.alarm.dao.AlarmDao;
import com.hzgc.service.util.response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlarmService {

    @Autowired
    private AlarmDao alarmDao;

    public ResponseResult<List<UserAlarmMessage>> alarmSearch(AlarmBean alarmBean) throws Exception{
        return alarmDao.alarmSearch(alarmBean);
    }

}
