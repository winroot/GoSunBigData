package com.hzgc.service.dynrepo.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
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
}
