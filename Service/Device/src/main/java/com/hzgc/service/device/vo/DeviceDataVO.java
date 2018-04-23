package com.hzgc.service.device.vo;

import java.io.Serializable;

public class DeviceDataVO implements Serializable{

    /**
     * 平台ID
     */
    String platformId;

    /**
     * 设备ID
     */
    String ipcID;

    /**
     * 设备备注
     */
    String notes;

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public String getIpcID() {
        return ipcID;
    }

    public void setIpcID(String ipcID) {
        this.ipcID = ipcID;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "DeviceDataVO{" +
                "platformId='" + platformId + '\'' +
                ", ipcID='" + ipcID + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}
