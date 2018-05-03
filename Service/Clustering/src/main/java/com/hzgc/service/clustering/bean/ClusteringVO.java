package com.hzgc.service.clustering.bean;

import com.hzgc.common.service.clustering.AlarmInfo;
import com.hzgc.service.clustering.service.ClusteringInfo;

import java.io.Serializable;
import java.util.List;

/**
 * 聚类查询返回前台封装
 */
public class ClusteringVO implements Serializable {

    /**
     * 建议迁入人口首页查询返回信息
     */
    private ClusteringInfo clusteringInfo;

    /**
     * 单个聚类详细信息
     */
    private List<AlarmInfo> alarmInfoList;

    /**
     * 单个聚类下所有告警ID
     */
    private List<Integer> alarmIdList;

    public ClusteringInfo getClusteringInfo() {
        return clusteringInfo;
    }

    public void setClusteringInfo(ClusteringInfo clusteringInfo) {
        this.clusteringInfo = clusteringInfo;
    }

    public List<AlarmInfo> getAlarmInfoList() {
        return alarmInfoList;
    }

    public void setAlarmInfoList(List<AlarmInfo> alarmInfoList) {
        this.alarmInfoList = alarmInfoList;
    }

    public List<Integer> getAlarmIdList() {
        return alarmIdList;
    }

    public void setAlarmIdList(List<Integer> alarmIdList) {
        this.alarmIdList = alarmIdList;
    }

    @Override
    public String toString() {
        return "ClusteringVO{" +
                "clusteringInfo=" + clusteringInfo +
                ", alarmInfoList=" + alarmInfoList +
                ", alarmIdList=" + alarmIdList +
                '}';
    }
}
