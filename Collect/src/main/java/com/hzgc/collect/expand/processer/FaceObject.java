package com.hzgc.collect.expand.processer;


import com.hzgc.jni.FaceAttribute;

import java.io.Serializable;

/**
 * 人脸对象
 */
public class FaceObject implements Serializable {

    //设备ID
    private String ipcId;

    //时间戳（格式：2017-01-01 00：00：00）
    private String timeStamp;

    //日期（格式：2017-01-01）
    private String date;

    //时间段（格式：0000）(小时+分钟)
    private int timeSlot;

    //人脸属性对象
    private FaceAttribute attribute;

    private String startTime;

    private String surl;

    private String burl;

    private String relativePath;


    private String hostname;

    public String getRelativePath() {
        return relativePath;
    }

    public FaceObject setRelativePath(String relativePath) {
        this.relativePath = relativePath;
        return this;
    }

    public static FaceObject builder() {
        return new FaceObject();
    }

    public String getIpcId() {
        return ipcId;
    }

    public FaceObject setIpcId(String ipcId) {
        this.ipcId = ipcId;
        return this;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public FaceObject setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
        return this;
    }

    public String getDate() {
        return date;
    }

    public FaceObject setDate(String date) {
        this.date = date;
        return this;
    }

    public int getTimeSlot() {
        return timeSlot;
    }

    public FaceObject setTimeSlot(int timeSlot) {
        this.timeSlot = timeSlot;
        return this;
    }

    public FaceAttribute getAttribute() {
        return attribute;
    }

    public FaceObject setAttribute(FaceAttribute attribute) {
        this.attribute = attribute;
        return this;
    }

    public String getStartTime() {
        return startTime;
    }

    public FaceObject setStartTime(String startTime) {
        this.startTime = startTime;
        return this;
    }

    public String getSurl() {
        return surl;
    }

    public FaceObject setSurl(String surl) {
        this.surl = surl;
        return this;
    }

    public String getBurl() {
        return burl;
    }

    public FaceObject setBurl(String burl) {
        this.burl = burl;
        return this;
    }

    public String getHostname() {
        return hostname;
    }

    public FaceObject setHostname(String hostname) {
        this.hostname = hostname;
        return this;
    }
}
