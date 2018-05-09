package com.hzgc.service.starepo.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * 静态库前台入参
 */
@ApiModel(value = "对象类型封装类")
@Data
public class ObjectType implements Serializable {

    @ApiModelProperty(value = "类型ID")
    private String id;

    @ApiModelProperty(value = "类型名")
    private String name;

    @ApiModelProperty(value = "创建者")
    private String creator;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "页码")
    private int pageIndex;

    @ApiModelProperty(value = "一页大小")
    private int pageSize;
}
