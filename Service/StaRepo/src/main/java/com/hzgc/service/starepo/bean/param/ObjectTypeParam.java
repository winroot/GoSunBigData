package com.hzgc.service.starepo.bean.param;

import com.hzgc.common.util.empty.IsEmpty;
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
public class ObjectTypeParam implements Serializable {

    @ApiModelProperty(value = "对象类型ID")
    private String objectTypeKey;           // 对象类型ID       必填选项（update）

    @ApiModelProperty(value = "对象类型名")
    private String objectTypeName;          // 对象类型名       必填选项（add、update）

    @ApiModelProperty(value = "创建者")
    private String creator;                 // 创建者

    @ApiModelProperty(value = "备注")
    private String remark;

    public static boolean ValidateParam_add(ObjectTypeParam param) {
        return param != null && param.getObjectTypeName()!= null;
    }

    public static boolean ValidateParam_update(ObjectTypeParam param) {
        return param != null && IsEmpty.strIsRight(param.getObjectTypeKey()) && IsEmpty.strIsRight(param.getObjectTypeName());
    }
}
