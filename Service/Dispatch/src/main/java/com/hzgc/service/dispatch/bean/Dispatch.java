package com.hzgc.service.dispatch.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/*
*
* 布控
* */
@Data
@ApiModel("布控参数")
public class Dispatch implements Serializable{

    private static final long serialVersionUID = -7903032316586781243L;
    @ApiModelProperty("布控规则对象")
    private Rule rule;

    @ApiModelProperty("布控设备集合")
    private List<Device> devices;


}
