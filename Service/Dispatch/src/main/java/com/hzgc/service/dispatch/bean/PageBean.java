package com.hzgc.service.dispatch.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("分页查询参数")
@Data
public class PageBean implements Serializable{

    private static final long serialVersionUID = -3272444465191596546L;
    @ApiModelProperty("分页开始")
    private Integer start;

    @ApiModelProperty("每页数量")
    private Integer limit;

    @ApiModelProperty("排序规则")
    private String sort;

    @ApiModelProperty("模糊查询字段")
    private String fuzzy_field;

    @ApiModelProperty("模糊查询值")
    private String fuzzy_value;
}
