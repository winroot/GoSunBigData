package com.hzgc.service.alarm.bean;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/*
* 告警查询入参参数
*
*/
@Data
@ApiModel
public class AlarmBean {

    //设备编码
    private String device_id;
    //告警类型
    private String alarm_type;
    //起始时间
    private String alarm_start_time;
    //结束时间
    private String alarm_end_time;
    //排序
    private String sort;
    //对象类型
    private String object_type;
    //分页参数
    private int start;
    private int limit;
    //相似度
    private double similarity;
}
