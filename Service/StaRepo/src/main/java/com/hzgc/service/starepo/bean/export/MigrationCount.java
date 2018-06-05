package com.hzgc.service.starepo.bean.export;

import lombok.Data;
import lombok.ToString;

/**
 * 人口迁入迁出统计
 */
@Data
@ToString
public class MigrationCount {
    private String groupId;         // 月份（格式：2018-07）
    private int immigratory;       // 迁入人口数量
    private int emigration;        // 迁出人口数量
}
