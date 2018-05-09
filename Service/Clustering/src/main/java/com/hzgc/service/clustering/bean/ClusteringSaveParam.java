package com.hzgc.service.clustering.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 聚类信息存储前台入参
 */
@ApiModel(value = "聚类信息存储入参")
@ToString()
@Data
public class ClusteringSaveParam implements Serializable {

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
}
