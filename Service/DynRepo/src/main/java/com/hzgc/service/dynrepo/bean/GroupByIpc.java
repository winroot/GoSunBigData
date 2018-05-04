package com.hzgc.service.dynrepo.bean;

import java.io.Serializable;
import java.util.List;

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

    public String getIpc() {
        return ipc;
    }

    public void setIpc(String ipc) {
        this.ipc = ipc;
    }

    public List<CapturedPicture> getPictures() {
        return pictures;
    }

    public void setPictures(List<CapturedPicture> pictures) {
        this.pictures = pictures;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
