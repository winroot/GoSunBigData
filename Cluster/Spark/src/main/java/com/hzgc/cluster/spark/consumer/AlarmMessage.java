package com.hzgc.cluster.spark.consumer;


import com.hzgc.cluster.spark.message.Item;

import java.io.Serializable;

/**
 * 识别告警推送信息类（刘善彬）
 */
public class AlarmMessage implements Serializable {

    /**
     * 告警类型
     */
    private String alarmType;

    /**
     * 动态抓取人脸的设备id
     */
    private String ipcID;

    /**
     * 告警推送时间
     */
    private String alarmTime;


    /**
     * 动态抓取照片的存储主机名(新增字段)
     */
    private String hostName;

    /**
     * 动态抓取人脸大图URL(新增字段)
     */
    private String bigPictureURL;

    /**
     * 动态抓取人脸小图URL(新增字段)
     */
    private String smallPictureURL;

    /**
     * 动态库抓取最相似的图片的相似度
     */
    private String sim;

    /**
     * 动态库抓取最相似的图片的静态库ID
     */
    private String StaticID;

    /**
     * 动态库抓取最相似的图片的对象类型
     */
    private String ObjectType;

    /**
     * 告警的flag
     */
    private Integer flag;

    /**
     * 告警的confirm
     */
    private Integer confirm;


    public AlarmMessage(String alarmType, String ipcID, String alarmTime, String hostName, String bigPictureURL, String smallPictureURL, String sim, String staticID, String objectType, Integer flag, Integer confirm) {

        this.alarmType = alarmType;
        this.ipcID = ipcID;
        this.alarmTime = alarmTime;
        this.hostName = hostName;
        this.bigPictureURL = bigPictureURL;
        this.smallPictureURL = smallPictureURL;
        this.sim = sim;
        StaticID = staticID;
        ObjectType = objectType;
        this.flag = flag;
        this.confirm = confirm;
    }

    public AlarmMessage() {
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public String getIpcID() {
        return ipcID;
    }

    public void setIpcID(String ipcID) {
        this.ipcID = ipcID;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getBigPictureURL() {
        return bigPictureURL;
    }

    public void setBigPictureURL(String bigPictureURL) {
        this.bigPictureURL = bigPictureURL;
    }

    public String getSmallPictureURL() {
        return smallPictureURL;
    }

    public void setSmallPictureURL(String smallPictureURL) {
        this.smallPictureURL = smallPictureURL;
    }

    public String getSim() {
        return sim;
    }

    public void setSim(String sim) {
        this.sim = sim;
    }

    public String getStaticID() {
        return StaticID;
    }

    public void setStaticID(String staticID) {
        StaticID = staticID;
    }

    public String getObjectType() {
        return ObjectType;
    }

    public void setObjectType(String objectType) {
        ObjectType = objectType;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Integer getConfirm() {
        return confirm;
    }

    public void setConfirm(Integer confirm) {
        this.confirm = confirm;
    }
}
