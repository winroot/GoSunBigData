package com.hzgc.service.dynrepo.bean;

import com.hzgc.common.faceattribute.bean.Attribute;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SearchHisotry implements Serializable {
    private List<String> deviceIds;
    private String similarity;
    private String startTime;
    private String endTime;
    private List<Attribute> attributes;
    private String searchTime;
    private String searchId;
}
