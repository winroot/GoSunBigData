package com.hzgc.service.visual.bean;

import lombok.Data;

@Data
public class PeopleManager {
    // 当前月份
    private String month;
    // 当前月份迁入统计
    private int moveInCount;
    // 当前月份迁出统计
    private int moveOutCount;
}
