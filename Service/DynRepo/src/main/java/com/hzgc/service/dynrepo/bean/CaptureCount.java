package com.hzgc.service.dynrepo.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class CaptureCount implements Serializable {

    /**
     * 匹配到的查询结果
     */
    private Long totalresultcount;

    private String lastcapturetime;
}
