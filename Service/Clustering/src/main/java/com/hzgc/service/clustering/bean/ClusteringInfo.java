package com.hzgc.service.clustering.bean;

import com.hzgc.common.service.clustering.ClusteringAttribute;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 建议迁入人口首页查询返回信息
 */
@Data
public class ClusteringInfo implements Serializable {

    private int totalClustering;

    private List<ClusteringAttribute> clusteringAttributeList;
}
