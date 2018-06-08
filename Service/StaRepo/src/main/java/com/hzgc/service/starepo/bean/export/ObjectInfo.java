package com.hzgc.service.starepo.bean.export;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 对象信息部分字段（除了pictureData字段）
 */
@Data
public class ObjectInfo {
    /* 根据id获取对象信息——后台返回封装 */
    @ApiModelProperty(value = "对象名字")
    private String name;                        // 对象名字
    @ApiModelProperty(value = "对象类型名字")
    private String objectTypeName;             // 对象类型名字
    @ApiModelProperty(value = "身份证")
    private String idCardNumber;               // 身份证
    @ApiModelProperty(value = "性别")
    private int sex;                            // 性别 [ 1:男，2:女 默认为0：未知]
    @ApiModelProperty(value = "布控理由")
    private String createdReason  ;            // 布控理由
    @ApiModelProperty(value = "创建人")
    private String creator;                     // 创建人
    @ApiModelProperty(value = "布控人联系方式")
    private String creatorPhone;               // 布控人联系方式
    @ApiModelProperty(value = "创建时间")
    private String createTime;                 // 创建时间
    @ApiModelProperty(value = "更新时间")
    private String updateTime;                 // 更新时间
    @ApiModelProperty(value = "关注等级")
    private int followLevel;                   // 关注等级 [ 0:非重点关注，1:重点关注，默认为0]
    @ApiModelProperty(value = "人员状态")
    private int status;                         // 人员状态 [ 0:常住人口，1:建议迁出，默认为0]
}
