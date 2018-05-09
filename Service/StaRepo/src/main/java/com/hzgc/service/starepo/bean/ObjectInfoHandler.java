package com.hzgc.service.starepo.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 静态库前台入参
 */
@ApiModel(value = "对象信息封装类")
@Data
public class ObjectInfoHandler implements Serializable{

    /**
     * 平台ID
     */
    @ApiModelProperty(value = "平台ID")
    private String platformId;

    /**
     * K-V 对，里面存放的是字段和值之间的一一对应关系,
     * 例如：传入一个Map 里面的值如下map.put("idcard", "450722199502196939")
     * 表示的是身份证号（idcard）是450722199502196939，
     * 其中的K 的具体，请参考给出的数据库字段设计
     */
    @ApiModelProperty(value = "person对象")
    private Map<String, Object> personObject;

    /**
     * 具体的一个人员信息的ID，值唯一
     */
    @ApiModelProperty(value = "具体的一个人员信息的ID")
    private List<String> rowkeys;

    /**
     * 搜索参数的封装
     */
    @ApiModelProperty(value = "搜索参数的封装")
    private PSearchArgsModel pSearchArgsModel;

    /**
     * 标记一条对象信息的唯一标志
     */
    @ApiModelProperty(value = "标记一条对象信息的唯一标志")
    private String rowkey;

    /**
     * 历史查询参数
     */
    @ApiModelProperty(value = "历史查询参数")
    private SearchRecordOpts searchRecordOpts;
}
