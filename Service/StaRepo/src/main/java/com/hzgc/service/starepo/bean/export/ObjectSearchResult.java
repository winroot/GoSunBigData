package com.hzgc.service.starepo.bean.export;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

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
    private String searchId;

    /**
     * 最终需要返回的结果，String是分别的Id
     */
    @ApiModelProperty(value = "最终需要返回的结果，String是分别的Id")
    private List<PersonSingleResult> singleSearchResults;
}
