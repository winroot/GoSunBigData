package com.hzgc.collect.expand.receiver;

public class Event {
     //ftp hostname path(ftp://hostname:port/xx/xx/xx)
    private String ftpHostNameUrlPath;

     //ftp ip url path(ftp://ip:port/xx/xx/xx)
    private String ftpIpUrlPath;

    // big picture ftp url
    private String bigPicurl;

     //face absolute path (file:///xx/xx/xx)
    private String absolutePath;

    //ftp relative path (/ipc/xx/xx/xx)
    private String relativePath;

    //picture timestamp
    private String timeStamp;

    //this picture ipcid
    private String ipcId;

    //this picture date
    private String date;

    private int timeSlot;

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public int getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(int timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIpcId() {
        return ipcId;
    }

    public void setIpcId(String ipcId) {
        this.ipcId = ipcId;
    }

    public String getBigPicurl() {
        return bigPicurl;
    }

    public void setBigPicurl(String bigPicurl) {
        this.bigPicurl = bigPicurl;
    }

    public String getFtpHostNameUrlPath() {
        return ftpHostNameUrlPath;
    }

    public void setFtpHostNameUrlPath(String ftpHostNameUrlPath) {
        this.ftpHostNameUrlPath = ftpHostNameUrlPath;
    }

    public String getFtpIpUrlPath() {
        return ftpIpUrlPath;
    }

    public void setFtpIpUrlPath(String ftpIpUrlPath) {
        this.ftpIpUrlPath = ftpIpUrlPath;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
