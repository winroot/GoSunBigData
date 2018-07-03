package com.hzgc.service.dynrepo.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hzgc.common.jni.PictureData;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 搜索选项
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SearchOption extends CaptureOption implements Serializable {


    //待查询图片对象列表
    private List<PictureData> images;

    //是否将传入若干图片当做同一个人,不设置默认为false,即不是同一个人
    private boolean singlePerson;

    //阈值
    private float similarity;

    //搜索的时间区间，为空或者没有传入这个参数时候搜索整天
    private List<TimeInterval> periodTimes;

    /**
     * 是否开启清晰度评价，true为开启，false为不开启
     */
    @JsonIgnore
    private boolean isClean;
}
