package com.hzgc.service.starepo.bean.export;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 按照对象类型分类，排序返回
 */
@ApiModel(value = "按照对象类型分类，排序返回")
@Data
@ToString
public class PersonObjectGroupByPkey implements Serializable {

    /**
     * 对象类型Key
     */
    @ApiModelProperty(value = "对象类型Key")
    private String objectTypeKey;

    /**
     * 对象类型名称
     */
    @ApiModelProperty(value = "对象类型名称")
    private String objectTypeName;

    /**
     * 底库信息
     */
    @ApiModelProperty(value = "底库信息")
    private List<PersonObject> personObjectList;

    /**
     * 当前 pkey 下的人的总数
     */
    @ApiModelProperty(value = "当前 pkey 下的人的总数")
    private int total;
}
