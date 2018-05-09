package com.hzgc.service.dynrepo.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 动态图片定义
 */
@Data
public class CapturedPicture implements Serializable {

    /**
     * 小图url
     */
    private String surl;

    /**
     * 大图url
     */
    private String burl;

    /**
     * 捕获照片的设备 id
     */
    private String ipcId;

    /**
     * 图片的描述信息
     */
    private String description;

    /**
     * 图片的相似度
     */
    private Float similarity;

    /**
     * 图片的附加信息，扩展预留
     */
    private Map<String, Object> extend;

    /**
     * 时间戳
     */
    private String timeStamp;

    /**
     * 车牌，当 SearchType 为 CAR 时有用，需要支持模糊搜索
     */
    private String plateNumber;
}
