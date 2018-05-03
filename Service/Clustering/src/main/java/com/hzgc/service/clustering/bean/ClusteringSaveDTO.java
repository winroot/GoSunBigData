package com.hzgc.service.clustering.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 聚类信息存储前台入参
 */
@ApiModel(value = "聚类信息存储入参")
public class ClusteringSaveDTO implements Serializable {

    /**
     * 聚类ID
     */
    @ApiModelProperty(value = "聚类ID")
    private String clusterId;

    /**
     * 聚类ID列表(包含地区)
     */
    @ApiModelProperty(value = "聚类ID列表")
    private List<String> clusterIdList;

    /**
     * 聚类时间
     */
    @ApiModelProperty(value = "聚类时间")
    private String time;

    /**
     * yes: 忽略聚类, no :不被忽略的聚类
     */
    @ApiModelProperty(value = "忽略聚类")
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

    @Override
    public String toString() {
        return "ClusteringSaveDTO{" +
                "clusterId='" + clusterId + '\'' +
                ", clusterIdList=" + clusterIdList +
                ", time='" + time + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }
}
