package com.hzgc.collect.expand.processer;

import com.hzgc.common.jni.FaceAttribute;
import com.hzgc.common.util.searchtype.SearchType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 人脸对象
 */
@Data
@AllArgsConstructor
public class FaceObject implements Serializable {

    //设备ID
    private String ipcId;

    //时间戳（格式：2017-01-01 00：00：00）
    private String timeStamp;

    //文件类型(区分人/车)
    private SearchType type;

    //日期（格式：2017-01-01）
    private String date;

    //时间段（格式：0000）(小时+分钟)
    private String timeSlot;

    //人脸属性对象
    private FaceAttribute attribute;

    private String startTime;

    private String surl;

    private String burl;

    private String hostname;
}
