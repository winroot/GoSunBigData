package com.hzgc.service.starepo.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 历史查询的时候传过来的参数
 */
@ApiModel(value = "历史查询的时候传过来的参数")
@Data
public class SearchRecordOpts implements Serializable {

    /**
     * 查询ID
     */
    @ApiModelProperty(value = "查询ID")
    private String totalSearchId;

    /**
     * 排序参数
     */
    @ApiModelProperty(value = "排序参数")
    private List<StaticSortParam> staticSortParams;

    /**
     * 子查询Id,以及子查询中按照对象类型分类的对象类型类表
     */
    @ApiModelProperty(value = "子查询Id,以及子查询中按照对象类型分类的对象类型类表")
    private List<SubQueryOpts> subQueryOptsList;

    /**
     * 返回条数中的起始位置
     */
    @ApiModelProperty(value = "返回条数中的起始位置")
    private int start;

    /**
     * 返回数据中的条数
     */
    @ApiModelProperty(value = "返回数据中的条数")
    private int size;
}
