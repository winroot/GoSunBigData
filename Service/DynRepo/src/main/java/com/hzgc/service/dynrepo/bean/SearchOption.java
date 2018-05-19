package com.hzgc.service.dynrepo.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hzgc.common.attribute.bean.Attribute;
import com.hzgc.jni.PictureData;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 搜索选项
 */
@Data
public class SearchOption implements Serializable {
    //搜索的设备范围
    private List<String> deviceIds;

    //待查询图片对象列表
    private List<PictureData> images;

    //是否将传入若干图片当做同一个人,不设置默认为false,即不是同一个人
    private boolean singlePerson;

    //阈值
    private float similarity;



    //开始日期,格式：xxxx-xx-xx xx:xx:xx
    private String startTime;

    //截止日期,格式：xxxx-xx-xx xx:xx:xx
    private String endTime;

    //搜索的时间区间，为空或者没有传入这个参数时候搜索整天
    private List<TimeInterval> periodTimes;

    /**
     * 是否开启清晰度评价，true为开启，false为不开启
     */
    @JsonIgnore
    private boolean isClean;

     //参数筛选选项
    private List<Attribute> attributes;

    //排序参数
    private List<Integer> sort;

    //分页查询开始行
    private int start;

    //查询条数
    private int limit;
}
