package com.hzgc.service.dynrepo.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SearchResultOption implements Serializable {

    /**
     * 查询总ID
     */
    private String searchId;

    /**
     * 针对每个子ID查询的参数
     */
    private List<SingleResultOption> singleSearchResultOptions;

    /**
     * 总的排序参数
     */
    private List<Integer> sort;

    /**
     * 查询起始位置
     */
    private int start;

    /**
     * 查多少条
     */
    private int limit;
}
