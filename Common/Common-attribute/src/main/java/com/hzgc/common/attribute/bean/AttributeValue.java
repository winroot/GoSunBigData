package com.hzgc.common.attribute.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * 单个属性值
 */
@Data
public class AttributeValue implements Serializable {

    /**
     * 属性的值
     */
    private Integer value;

    /**
     * 值描述
     */
    private String desc;

    /**
     * 属性统计（属性统计接口使用）
     */
    private long count;
}
