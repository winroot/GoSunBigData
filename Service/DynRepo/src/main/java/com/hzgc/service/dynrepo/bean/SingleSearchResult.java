package com.hzgc.service.dynrepo.bean;

import lombok.Data;

@Data
public class SingleSearchResult extends SingleCaptureResult {
    //查询子ID
    private String searchId;
}
