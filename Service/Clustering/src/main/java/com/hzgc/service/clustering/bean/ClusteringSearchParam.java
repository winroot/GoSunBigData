package com.hzgc.service.clustering.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 聚类查询前台入参
 */
@ApiModel(value = "聚类信息查询入参")
@Data
@ToString
public class ClusteringSearchParam implements Serializable {

    //聚类ID
    @ApiModelProperty(value = "聚类ID")
    private String clusterId;

    //聚类地区
    @ApiModelProperty(value = "聚类地区")
    private String region;

    //聚类时间
    @ApiModelProperty(value = "聚类时间")
    private String time;

    //分页起始位置
    @ApiModelProperty(value = "分页起始位置")
    private int start;

    //分页行数
    @ApiModelProperty(value = "分页行数")
    private int limit;

    //排序参数（默认按出现次数排序）
    @ApiModelProperty(value = "排序参数")
    private String sortParam;
}
