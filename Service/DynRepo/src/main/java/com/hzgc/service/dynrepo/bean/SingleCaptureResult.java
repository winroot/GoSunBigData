package com.hzgc.service.dynrepo.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SingleCaptureResult implements Serializable {
    private String deviceId;
    private String deviceName;
    private int total;
    public List<CapturedPicture> pictures;
}
