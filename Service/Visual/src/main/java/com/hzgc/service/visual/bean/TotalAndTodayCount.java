package com.hzgc.service.visual.bean;

import lombok.Data;

@Data
public class TotalAndTodayCount {

    /**
     * 抓拍统计
     */
    private int totalNumber;

    /**
     * 今日抓拍统计
     */
    private int todayTotalNumber;
}
