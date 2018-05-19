package com.hzgc.service.dynrepo.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SearchResult implements Serializable {

    /**
     * 总搜索ID
     */
    private String searchId;

    /**
     * 子结果集集合
     */
    private List<SingleSearchResult> singleResults;
}

