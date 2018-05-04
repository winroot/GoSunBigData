package com.hzgc.service.device.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(value = "设备参数")
public class DeviceData implements Serializable {

    /**
     * 平台ID
     */
    @ApiModelProperty(value = "平台ID",required = true)
    private String platformId;

    /**
     * 设备ID
     */
    @ApiModelProperty(value = "设备ID")
    private String ipcID;

    /**
     * 设备备注
     */
    @ApiModelProperty(value = "设备备注")
    private String notes;

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
        return "DeviceData{" +
                "platformId='" + platformId + '\'' +
                ", ipcID='" + ipcID + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}
