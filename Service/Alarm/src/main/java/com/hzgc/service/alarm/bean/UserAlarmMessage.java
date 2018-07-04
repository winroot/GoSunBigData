package com.hzgc.service.alarm.bean;

import lombok.Data;

/*
 * 告警查询出参参数
 *
 */
@Data
public class UserAlarmMessage {

    private String alarmType;
    private String occurTime;
    private String deviceID;
    private String deviceName;
    private AlarmData alarmData;

}
