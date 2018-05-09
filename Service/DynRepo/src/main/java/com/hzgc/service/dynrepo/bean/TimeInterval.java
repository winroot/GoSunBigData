package com.hzgc.service.dynrepo.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * 一天内的时间区间
 */
@Data
public class TimeInterval implements Serializable {

    /**
     * 开始时间，以一天内的分钟数计算
     */
    private int start;

    /**
     * 结束时间，以一天内的分钟数计算
     */
    private int end;
}
