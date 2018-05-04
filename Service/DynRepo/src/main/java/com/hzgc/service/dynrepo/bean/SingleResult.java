package com.hzgc.service.dynrepo.bean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class SingleResult implements Serializable {

    /**
     * 查询子ID
     */
    private String id;

    /**
     * 图片二进制数据
     */
    private List<byte[]> binPicture;

    /**
     * 单一结果的结果总数
     */
    private int total;

    /**
     * 非设备归类时的结果集
     * 在第一次查询返回结果是肯定是按此种集合返回
     * 后续再次查询时如果按照设备归类的则有可能按照picturesByIpc来返回
     */
    private List<CapturedPicture> pictures;

    /**
     * 按设备归类时的结果集
     */
    private List<GroupByIpc> picturesByIpc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<CapturedPicture> getPictures() {
        return pictures;
    }

    public void setPictures(List<CapturedPicture> pictures) {
        this.pictures = pictures;
    }

    public List<byte[]> getBinPicture() {
        return binPicture;
    }

    public void setBinPicture(List<byte[]> binPicture) {
        this.binPicture = binPicture;
    }

    public List<GroupByIpc> getPicturesByIpc() {
        return picturesByIpc;
    }

    public void setPicturesByIpc(List<GroupByIpc> picturesByIpc) {
        this.picturesByIpc = picturesByIpc;
    }

    @Override
    public String toString() {
        return "Single search id is:"
                + id
                + ", picture is:"
                + (binPicture != null ? "true" : "false")
                + ", total is:"
                + total
                + ", CapturePicture"
                + Arrays.toString(pictures.toArray());
    }
}
