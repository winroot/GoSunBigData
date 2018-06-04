package com.hzgc.service.starepo.bean.export;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 多个人的的情况下，每个图片对应的
 */
@ApiModel(value = "多个人的的情况下，每个图片对应的")
@Data
@ToString
public class PersonSingleResult implements Serializable {

    /**
     * 子搜索Id
     */
    @ApiModelProperty(value = "子搜索Id")
    private String searchId;

    /**
     * 搜索的总数
     */
    @ApiModelProperty(value = "搜索的总数")
    private int total;

    /**
     * 搜索图片ID
     */
    @ApiModelProperty(value = "搜索图片ID")
    private List<String> imageNames;

    /**
     * 不用聚类的时候的返回结果
     */
    @ApiModelProperty(value = "不用聚类的时候的返回结果")
    private List<PersonObject> objectInfoBeans;

    /**
     * 根据 objectTypeKey 分类后的返回结果
     */
    @ApiModelProperty(value = "根据 pkey 分类后的返回结果")
    private List<PersonObjectGroupByPkey> singleObjKeyResults;
}
