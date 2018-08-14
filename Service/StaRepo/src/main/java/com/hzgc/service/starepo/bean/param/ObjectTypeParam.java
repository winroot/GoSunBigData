package com.hzgc.service.starepo.bean.param;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 静态库前台入参
 */
@ApiModel(value = "对象类型封装类")
@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObjectTypeParam implements Serializable {

    @ApiModelProperty(value = "对象类型ID")
    private String objectTypeKey;           // 对象类型ID       必填选项（update）

    @ApiModelProperty(value = "对象类型名")
    private String objectTypeName;          // 对象类型名       必填选项（add、update）

    @ApiModelProperty(value = "创建者")
    private String creator;                 // 创建者

    @ApiModelProperty(value = "备注")
    private String remark;
}
