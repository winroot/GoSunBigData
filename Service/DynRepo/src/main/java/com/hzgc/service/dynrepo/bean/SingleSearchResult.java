package com.hzgc.service.dynrepo.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SingleSearchResult implements Serializable {
    private String searchId;
    private List<String> imageNames;
    private int total;
    private List<CapturedPicture> pictures;
    private List<GroupByIpc> devicePictures;

}
