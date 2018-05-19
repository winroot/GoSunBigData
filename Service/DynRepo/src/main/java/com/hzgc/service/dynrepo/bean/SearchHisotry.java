package com.hzgc.service.dynrepo.bean;

import com.hzgc.common.attribute.bean.Attribute;
import lombok.Data;

import java.util.List;

@Data
public class SearchHisotry {
    private List<String> deviceIds;
    private String similarity;
    private String startTime;
    private String endTime;
    private List<Attribute> attributes;
    private String searchTime;
    private String searchId;
}
