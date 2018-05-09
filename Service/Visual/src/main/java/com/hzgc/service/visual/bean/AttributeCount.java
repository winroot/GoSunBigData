package com.hzgc.service.visual.bean;

import com.hzgc.common.attribute.bean.Attribute;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 属性统计
 */
@Data
public class AttributeCount implements Serializable {

    /**
     * 设备ID
     */
    private String IPCId;

    /**
     * 抓拍统计
     */
    private long captureCount;

    /**
     * 属性统计
     */
    private List<Attribute> attributes;
}
