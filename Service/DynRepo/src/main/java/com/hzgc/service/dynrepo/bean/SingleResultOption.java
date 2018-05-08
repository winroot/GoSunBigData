package com.hzgc.service.dynrepo.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SingleResultOption implements Serializable {

    /**
     * 子ID
     */
    private String id;

    /**
     * 传入的设备列表,在按设备归类并进行分页查询时有效
     */
    private List<String> ipcList;
}
