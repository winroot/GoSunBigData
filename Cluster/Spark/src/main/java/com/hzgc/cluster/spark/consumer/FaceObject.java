package com.hzgc.cluster.spark.consumer;

import com.hzgc.common.jni.FaceAttribute;
import com.hzgc.common.util.searchtype.SearchType;

import java.io.Serializable;

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

    public String getIpcId() {
        return ipcId;
    }

    public void setIpcId(String ipcId) {
        this.ipcId = ipcId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public SearchType getType() {
        return type;
    }

    public void setType(SearchType type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public FaceAttribute getAttribute() {
        return attribute;
    }

    public void setAttribute(FaceAttribute attribute) {
        this.attribute = attribute;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getSurl() {
        return surl;
    }

    public void setSurl(String surl) {
        this.surl = surl;
    }

    public String getBurl() {
        return burl;
    }

    public void setBurl(String burl) {
        this.burl = burl;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
}
