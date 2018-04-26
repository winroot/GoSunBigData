package com.hzgc.service.dynrepo.vo;

import com.hzgc.service.dynrepo.object.SearchOption;
import com.hzgc.service.dynrepo.object.SearchResultOption;
import com.hzgc.common.util.searchtype.SearchType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 动态库以图搜图前台入参
 */
@ApiModel(value = "以图搜图入参")
public class CapturePictureSearchVO implements Serializable {

    /**
     * 搜索选项
     */
    @ApiModelProperty(value = "搜索选项")
    private SearchOption searchOption;

    /**
     * 历史结果查询参数对象
     */
    @ApiModelProperty(value = "历史结果查询参数")
    private SearchResultOption searchResultOption;

    /**
     * 图片类型（人、车）
     */
    @ApiModelProperty(value = "图片类型")
    private SearchType type;

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
}
