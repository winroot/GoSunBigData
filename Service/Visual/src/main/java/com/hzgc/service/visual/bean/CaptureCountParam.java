package com.hzgc.service.visual.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 大数据可视化前台入参
 */
@ApiModel(value = "大数据可视化入参")
@Data
public class CaptureCountParam implements Serializable {

    /**
     * 设备ID
     */
    @ApiModelProperty(value = "设备ID")
    private String ipcId;

    /**
     * 设备ID列表
     */
    @ApiModelProperty(value = "设备ID列表")
    private List<String> ipcIdList;

    /**
     * 起始时间
     */
    @ApiModelProperty(value = "起始时间")
    private String startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private String endTime;
}
