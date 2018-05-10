package com.hzgc.service.dynrepo.attribute;

import com.hzgc.common.attribute.bean.Logistic;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 人脸特征属性对象
 */
@Data
public class Attribute implements Serializable {

    /**
     * 属性字段名称，平台传过来的是枚举类的类名称，数据库查询时需要转小写
     */
    private String identify;

    /**
     * 属性中文描述
     */
    private String desc;

    /**
     * 逻辑关系,AND,OR
     */
    private Logistic logistic;

    /**
     * 属性值
     */
    private List<AttributeValue> values;
}
