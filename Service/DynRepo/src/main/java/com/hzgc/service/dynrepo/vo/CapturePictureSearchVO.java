package com.hzgc.service.dynrepo.vo;

import com.hzgc.service.dynrepo.object.SearchOption;
import com.hzgc.service.dynrepo.object.SearchResultOption;
import com.hzgc.common.util.searchtype.SearchType;

import java.io.Serializable;
import java.util.List;

/**
 * 动态库以图搜图前台入参
 */
public class CapturePictureSearchVO implements Serializable {

    /**
     * 搜索选项
     */
    private SearchOption searchOption;

    /**
     * 历史结果查询参数对象
     */
    private SearchResultOption searchResultOption;

    /**
     * 图片类型（人、车）
     */
    private SearchType type;

    /**
     * 起始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 设备ID
     */
    private String ipcId;

    /**
     * 设备ID列表
     */
    private List<String> ipcIdList;

    public SearchOption getSearchOption() {
        return searchOption;
    }

    public void setSearchOption(SearchOption searchOption) {
        this.searchOption = searchOption;
    }

    public SearchResultOption getSearchResultOption() {
        return searchResultOption;
    }

    public void setSearchResultOption(SearchResultOption searchResultOption) {
        this.searchResultOption = searchResultOption;
    }

    public SearchType getType() {
        return type;
    }

    public void setType(SearchType type) {
        this.type = type;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getIpcId() {
        return ipcId;
    }

    public void setIpcId(String ipcId) {
        this.ipcId = ipcId;
    }

    public List<String> getIpcIdList() {
        return ipcIdList;
    }

    public void setIpcIdList(List<String> ipcIdList) {
        this.ipcIdList = ipcIdList;
    }
}
