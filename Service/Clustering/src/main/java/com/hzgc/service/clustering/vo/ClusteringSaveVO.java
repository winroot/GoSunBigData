package com.hzgc.service.clustering.vo;

import java.util.List;

/**
 * 聚类信息存储前台入参
 */
public class ClusteringSaveVO {

    /**
     * 聚类ID
     */
    private String clusterId;
    /**
     * 聚类ID列表(包含地区)
     */
    private List<String> clusterIdList;

    /**
     * 聚类时间
     */
    private String time;

    /**
     * yes: 删除忽略聚类, no :删除不被忽略的聚类
     */
    private String flag;

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public List<String> getClusterIdList() {
        return clusterIdList;
    }

    public void setClusterIdList(List<String> clusterIdList) {
        this.clusterIdList = clusterIdList;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
