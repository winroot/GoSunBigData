package com.hzgc.service.dynrepo.bean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class SearchResult implements Serializable {

    /**
     * 总搜索ID
     */
    private String searchId;

    /**
     * 子结果集集合
     */
    private List<SingleResult> results;

    /**
     * 搜索类型
     */
    private String searchType;

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    public List<SingleResult> getResults() {
        return results;
    }

    public void setResults(List<SingleResult> results) {
        this.results = results;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    @Override
    public String toString() {
        return "Search ID is:"
                + this.searchId
                + ", search type is:"
                + this.searchType
                + ", Singleresult " + Arrays.toString(results.toArray());
    }
}

