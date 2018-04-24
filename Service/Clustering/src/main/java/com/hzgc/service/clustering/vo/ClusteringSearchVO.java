package com.hzgc.service.clustering.vo;

import java.io.Serializable;

/**
 * 聚类查询前台入参
 */
public class ClusteringSearchVO implements Serializable {

    /**
     * 聚类ID
     */
    private String clusterId;

    /**
     * 聚类地区
     */
    private String region;

    /**
     * 聚类时间
     */
    private String time;

    /**
     * 分页起始位置
     */
    private int start;

    /**
     * 分页行数
     */
    private int limt;

    /**
     * 排序参数（默认按出现次数排序）
     */
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
}
