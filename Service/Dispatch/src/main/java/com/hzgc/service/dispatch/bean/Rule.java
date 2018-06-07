package com.hzgc.service.dispatch.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/*
* 告警规则
*/
@ApiModel("告警规则")
@Data
public class Rule implements Serializable{

    private static final long serialVersionUID = -3607521286456981127L;
    @ApiModelProperty("规则ID")
    private String ruleId;

    @ApiModelProperty("规则名称")
    private String name;

    @ApiModelProperty("规则描述")
    private String description;

    @ApiModelProperty("告警规则")
    private List<Warn> warns;


}
