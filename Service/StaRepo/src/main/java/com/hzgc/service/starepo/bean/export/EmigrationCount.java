package com.hzgc.service.starepo.bean.export;

import lombok.Data;

@Data
public class EmigrationCount {
    // 当前月份
    private String month;
    // 当前月份统计
    private int count;
}
