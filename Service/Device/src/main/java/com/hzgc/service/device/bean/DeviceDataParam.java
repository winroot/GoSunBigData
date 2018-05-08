package com.hzgc.service.device.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "设备参数")
@Data
public class DeviceDataParam implements Serializable {

    /**
     * 平台ID
     */
    @ApiModelProperty(value = "平台ID",required = true)
    private String platformId;

    /**
     * 设备ID
     */
    @ApiModelProperty(value = "设备ID")
    private String ipcID;

    /**
     * 设备备注
     */
    @ApiModelProperty(value = "设备备注")
    private String notes;
}
