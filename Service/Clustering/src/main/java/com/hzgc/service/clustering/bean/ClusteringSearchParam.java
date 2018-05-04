package com.hzgc.service.clustering.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 聚类查询前台入参
 */
@ApiModel(value = "聚类信息查询入参")
public class ClusteringSearchParam implements Serializable {

    /**
     * 聚类ID
     */
    @ApiModelProperty(value = "聚类ID")
    private String clusterId;

    /**
     * 聚类地区
     */
    @ApiModelProperty(value = "聚类地区")
    private String region;

    /**
     * 聚类时间
     */
    @ApiModelProperty(value = "聚类时间")
    private String time;

    /**
     * 分页起始位置
     */
    @ApiModelProperty(value = "分页起始位置")
    private int start;

    /**
     * 分页行数
     */
    @ApiModelProperty(value = "分页行数")
    private int limt;

    /**
     * 排序参数（默认按出现次数排序）
     */
    @ApiModelProperty(value = "排序参数")
    private String sortParam;

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimt() {
        return limt;
    }

    public void setLimt(int limt) {
        this.limt = limt;
    }

    public String getSortParam() {
        return sortParam;
    }

    public void setSortParam(String sortParam) {
        this.sortParam = sortParam;
    }

    @Override
    public String toString() {
        return "ClusteringSearchParam{" +
                "clusterId='" + clusterId + '\'' +
                ", region='" + region + '\'' +
                ", time='" + time + '\'' +
                ", start=" + start +
                ", limt=" + limt +
                ", sortParam='" + sortParam + '\'' +
                '}';
    }
}
