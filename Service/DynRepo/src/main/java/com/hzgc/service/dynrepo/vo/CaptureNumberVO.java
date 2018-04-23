package com.hzgc.service.dynrepo.vo;

import java.util.List;

/**
 * 大数据可视化前台入参
 */
public class CaptureNumberVO {

    /**
     * 设备ID列表
     */
    private List<String> ipcIdList;

    /**
     * 平台ID
     */
    private String platformId;

    /**
     * 起始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    public List<String> getIpcIdList() {
        return ipcIdList;
    }

    public void setIpcIdList(List<String> ipcIdList) {
        this.ipcIdList = ipcIdList;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
