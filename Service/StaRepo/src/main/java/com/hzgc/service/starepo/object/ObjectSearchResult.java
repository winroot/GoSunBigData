package com.hzgc.service.starepo.object;

import java.io.Serializable;
import java.util.List;

/**
 * 静态库查询的时候返回的结果
 */
public class ObjectSearchResult implements Serializable {

    /**
     * 总的searchId
     */
    private String searchTotalId;

    /**
     * 查询成功与否状态
     */
    private int searchStatus;

    /**
     * 最终需要返回的结果，String是分别的Id
     */
    private List<PersonSingleResult> finalResults;

    public ObjectSearchResult() {
    }

    public String getSearchTotalId() {
        return searchTotalId;
    }

    public void setSearchTotalId(String searchTotalId) {
        this.searchTotalId = searchTotalId;
    }

    public int getSearchStatus() {
        return searchStatus;
    }

    public void setSearchStatus(int searchStatus) {
        this.searchStatus = searchStatus;
    }

    public List<PersonSingleResult> getFinalResults() {
        return finalResults;
    }

    public void setFinalResults(List<PersonSingleResult> finalResults) {
        this.finalResults = finalResults;
    }

    @Override
    public String toString() {
        return "ObjectSearchResult{" +
                "searchTotalId='" + searchTotalId + '\'' +
                ", searchStatus=" + searchStatus +
                ", finalResults=" + finalResults +
                '}';
    }
}
