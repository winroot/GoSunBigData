package com.hzgc.service.starepo.bean;


import com.hzgc.jni.FaceAttribute;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 即时查询的时候传过来的参数
 */
@ApiModel(value = "即时查询的时候传过来的参数")
@Data
public class PSearchArgsModel implements Serializable {

    @ApiModelProperty(value = "id")
    private String rowkey;          // 即id

    @ApiModelProperty(value = "平台Id")
    private String paltaformId;     // 平台Id

    @ApiModelProperty(value = "姓名")
    private String name;            // 姓名

    @ApiModelProperty(value = "身份证号")
    private String idCard;          // 身份证号

    @ApiModelProperty(value = "性别")
    private Integer sex;            // 性别

    @ApiModelProperty(value = "图片列表")
    private Map<String,byte[]> images;                  //图片列表

    @ApiModelProperty(value = "特征值列表")
    private Map<String,FaceAttribute> faceAttributeMap; //特征值列表

    @ApiModelProperty(value = "是否是同一个人")
    private boolean theSameMan;                         //是否是同一个人

    @ApiModelProperty(value = "排序参数")
    private List<StaticSortParam> staticSortParams;     //排序参数

    @ApiModelProperty(value = "阈值")
    private float thredshold;       // 阈值

    @ApiModelProperty(value = "人员类型列表")
    private List<String> pkeys;     // 人员类型列表

    @ApiModelProperty(value = "布控人")
    private String creator;         // 布控人

    @ApiModelProperty(value = "布控人手机号")
    private String cphone;          // 布控人手机号

    @ApiModelProperty(value = "开始的行数")
    private Integer start;          // 开始的行数

    @ApiModelProperty(value = "需要返回多少行")
    private Integer pageSize;       // 需要返回多少行

    @ApiModelProperty(value = "搜索Id")
    private String searchId;        // 搜索Id

    @ApiModelProperty(value = "搜索类型，对应的是调用的函数的名字")
    private String searchType;      // 搜索类型，对应的是调用的函数的名字

    @ApiModelProperty(value = "是否模糊查询， true ,是，false 不是")
    private boolean moHuSearch;     // 是否模糊查询， true ,是，false 不是

    @ApiModelProperty(value = "0,重点关注，1非重点关注")
    private Integer important;      // 0,重点关注，1非重点关注

    @ApiModelProperty(value = "0 ,常住人口，1 建议迁出")
    private Integer status;         // 0 ,常住人口，1 建议迁出

    @ApiModelProperty(value = "人员所在位置查询")
    private String location;        // 人员所在位置查询
}
