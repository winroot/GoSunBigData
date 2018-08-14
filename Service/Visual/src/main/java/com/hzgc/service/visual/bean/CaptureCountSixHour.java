package com.hzgc.service.visual.bean;

import lombok.Data;

import java.util.List;

/**
 * 某地区某段日期每天固定时间段抓拍统计 返回值
 */
@Data
public class CaptureCountSixHour {
    // 日期（eg：2018-01-01）
    private String data;

    // 时间段（eg：00:00-06:00）
    private String timeSolt;

    // 当前时间段内抓拍总数
    private int total;

    public CaptureCountSixHour(String data, String timeSolt, int total){
        this.data = data;
        this.timeSolt = timeSolt;
        this.total = total;
    }
}
