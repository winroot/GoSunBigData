package com.hzgc.service.dynrepo.bean;

import com.hzgc.common.util.searchtype.SearchType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 大数据可视化前台入参
 */
@ApiModel(value = "大数据可视化入参")
public class CaptureCountVO implements Serializable {

    /**
     * 设备ID
     */
    @ApiModelProperty(value = "设备ID")
    private String ipcId;

    /**
     * 设备ID列表
     */
    @ApiModelProperty(value = "设备ID列表")
    private List<String> ipcIdList;

    /**
     * 平台ID
     */
    @ApiModelProperty(value = "平台ID")
    private String platformId;

    /**
     * 起始时间
     */
    @ApiModelProperty(value = "起始时间")
    private String startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private String endTime;

    /**
     * 搜索类型（人/车）
     */
    @ApiModelProperty(value = "搜索类型")
    private SearchType type;

    public String getIpcId() {
        return ipcId;
    }

    public void setIpcId(String ipcId) {
        this.ipcId = ipcId;
    }

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

    public SearchType getType() {
        return type;
    }

    public void setType(SearchType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "CaptureCountVO{" +
                "ipcId='" + ipcId + '\'' +
                ", ipcIdList=" + ipcIdList +
                ", platformId='" + platformId + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", type=" + type +
                '}';
    }
}
