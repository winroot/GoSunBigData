package com.hzgc.service.starepo.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;

/**
 * 静态库查询的时候返回的结果
 */
@ApiModel(value = "静态库查询的时候返回的结果")
@Data
@ToString
public class ObjectSearchResult implements Serializable {

    /**
     * 总的searchId
     */
    @ApiModelProperty(value = "总的searchId")
    private String searchTotalId;

    /**
     * 查询成功与否状态
     */
    @ApiModelProperty(value = "查询成功与否状态")
    private int searchStatus;

    /**
     * 最终需要返回的结果，String是分别的Id
     */
    @ApiModelProperty(value = "最终需要返回的结果，String是分别的Id")
    private List<PersonSingleResult> finalResults;
}
