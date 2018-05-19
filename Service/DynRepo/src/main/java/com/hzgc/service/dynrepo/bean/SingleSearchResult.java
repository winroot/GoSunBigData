package com.hzgc.service.dynrepo.bean;

import lombok.Data;

import java.util.List;

@Data
public class SingleSearchResult {
    private String searchId;
    private List<String> imageNames;
    private int total;
    private List<CapturedPicture> pictures;
    private List<GroupByIpc> devicePictures;

}
