package com.hzgc.service.dispatch.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("设备对象")
public class Device implements Serializable{

    private static final long serialVersionUID = -2886805757480304222L;

    //平台端设备ID
    @ApiModelProperty("设备ID")
    private String id;
    //设备名称
    @ApiModelProperty("设备名称")
    private String name;
}
