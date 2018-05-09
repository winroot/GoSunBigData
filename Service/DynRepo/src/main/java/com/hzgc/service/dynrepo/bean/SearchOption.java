package com.hzgc.service.dynrepo.bean;

import com.hzgc.common.attribute.bean.Attribute;
import com.hzgc.common.util.searchtype.SearchType;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 搜索选项
 */
@Data
public class SearchOption implements Serializable {

    /**
     * 搜索类型，人PERSON（0）,车CAR（1）
     */
    private SearchType searchType;

    /**
     * 待查询图片对象列表
     */
    private List<PictureData> images;

    /**
     * 是否将传入若干图片当做同一个人,不设置默认为false,即不是同一个人
     */
    private boolean isOnePerson;

    /**
     * 车牌，当 SearchType 为 CAR 时有用，需要支持模糊搜索
     */
    private String plateNumber;

    /**
     * 阈值
     */
    private float threshold;

    /**
     * 搜索的设备范围
     */
    private List<String> deviceIds;

    /**
     * 平台 Id 优先使用 deviceIds 圈定范围
     */
    private String platformId;

    /**
     * 开始日期,格式：xxxx-xx-xx xx:xx:xx
     */
    private String startDate;

    /**
     * 截止日期,格式：xxxx-xx-xx xx:xx:xx
     */
    private String endDate;

    /**
     * 搜索的时间区间，为空或者没有传入这个参数时候搜索整天
     */
    private List<TimeInterval> intervals;

    /**
     * 是否开启清晰度评价，true为开启，false为不开启
     */
    private boolean isClean;

    /**
     * 参数筛选选项
     */
    private List<Attribute> attributes;

    /**
     * 排序参数
     */
    private List<SortParam> sortParams;

    /**
     * 分页查询开始行
     */
    private int offset;

    /**
     * 查询条数
     */
    private int count;
}
