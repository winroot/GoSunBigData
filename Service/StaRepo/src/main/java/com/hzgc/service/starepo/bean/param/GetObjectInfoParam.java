package com.hzgc.service.starepo.bean.param;

import com.hzgc.jni.PictureData;
import com.hzgc.service.starepo.bean.StaticSortParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 获取对象信息查询参数
 */
@ApiModel(value = "对象信息查询封装类")
@Data
@ToString
public class GetObjectInfoParam implements Serializable{

    @ApiModelProperty(value = "图片数据")
    private List<PictureData> pictureDataList;    // 照片数据
    @ApiModelProperty(value = "对象类型key列表")
    private List<String> objectTypeKeyList;       // 对象类型key列表
    @ApiModelProperty(value = "对象名字")
    private String objectName;                    // 对象名字
    @ApiModelProperty(value = "身份证")
    private String idcard;                        // 身份证
    @ApiModelProperty(value = "性别")
    private Integer sex;                          // 性别 [0 = 未知（默认选项），1 = 男，2 = 女]
    @ApiModelProperty(value = "创建人")
    private String creator;                       // 创建人
    @ApiModelProperty(value = "布控人联系方式")
    private String creatorConractWay;             // 布控人联系方式
    @ApiModelProperty(value = "相似度")
    private float similarity;                     // 相似度 [ 与 pictureDataList 共同存在 ]
    @ApiModelProperty(value = "是否是同一个人")
    private boolean singlePerson;                 // 是否是同一个人
    @ApiModelProperty(value = "排序参数")
    private List<StaticSortParam> sortParamList;  // 排序参数
    @ApiModelProperty(value = "人员状态")
    private Integer status;                       // 人员状态 [0 = 常住人口（默认选项），1 = 建议迁出]
    @ApiModelProperty(value = "关注等级")
    private Integer followLevel;                  // 关注等级 [1 = 非重点关注（默认选项），2 = 重点关注]
    @ApiModelProperty(value = "起始行数")
    private Integer start;                        // 起始行数
    @ApiModelProperty(value = "分页行数")
    private Integer limit;                        // 分页行数
    @ApiModelProperty(value = "地址")
    private String location;                      // 地址
}
