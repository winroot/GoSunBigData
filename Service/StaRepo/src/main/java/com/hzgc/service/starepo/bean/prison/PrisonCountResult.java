package com.hzgc.service.starepo.bean.prison;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class PrisonCountResult implements Serializable{

    private String pkey;

    private Map<String, Integer> locationCounts;
}
