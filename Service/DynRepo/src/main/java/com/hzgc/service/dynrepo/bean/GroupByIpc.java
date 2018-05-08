package com.hzgc.service.dynrepo.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GroupByIpc implements Serializable {

    /**
     * ipcID
     */
    private String ipc;

    /**
     * 抓拍图片
     */
    private List<CapturedPicture> pictures;

    /**
     * 当前设备图片总计
     */
    private int total;
}
