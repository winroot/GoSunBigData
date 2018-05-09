package com.hzgc.service.starepo.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class PrisonCountResult implements Serializable{

    private String pkey;

    private Map<String, Integer> locationCounts;
}
