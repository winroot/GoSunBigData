package com.hzgc.service.dynrepo.dto;

import java.util.Map;

/**
 * 大数据可视化返回前台封装（抓拍统计返回封装）
 */
public class CaptureNumberDTO {

    /**
     * 统计信息
     */
    private Map<String,Integer> map;

    public Map<String, Integer> getMap() {
        return map;
    }

    public void setMap(Map<String, Integer> map) {
        this.map = map;
    }
}
