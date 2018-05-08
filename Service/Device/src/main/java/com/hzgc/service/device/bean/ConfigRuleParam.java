package com.hzgc.service.device.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "规则配置参数")
@Data
public class ConfigRuleParam implements Serializable {

    /**
     * 设备ID
     */
    @ApiModelProperty("设备ID")
    private String ipcID;

    /**
     * 设备ID的集合
     */
    @ApiModelProperty("设备ID的集合")
    private List<String> ipcIDs;

    /**
     * 告警规则集合
     */
    @ApiModelProperty("告警规则集合")
    private List<WarnRule> rules;

    /**
     * 对象类型
     */
    @ApiModelProperty("对象类型")
    private String objectType;
}
