package com.hzgc.service.dynrepo.service;

import com.hzgc.common.util.searchtype.SearchType;
import com.hzgc.service.dynrepo.attribute.Attribute;
import com.hzgc.service.dynrepo.object.SearchOption;
import com.hzgc.service.dynrepo.object.SearchResult;
import com.hzgc.service.dynrepo.object.SearchResultOption;

import java.util.List;

/**
 * 以图搜图接口，内含四个方法（外）（彭聪）
 */
public interface CapturePictureSearchService {

    /**
     * 以图搜图
     * 接收应用层传递的参数进行搜图，，如果大数据处理的时间过长
     * 则先返回searchId,finished=false,然后再开始计算；如果能够在秒级时间内计算完则计算完后再返回结果
     *
     * @param option 搜索选项
     * @return 搜索结果SearchResult对象
     */
    SearchResult search(SearchOption option);

    /**
     * 历史搜索记录查询
     *
     * @param resultOption 历史结果查询参数对象
     * @return SearchResult对象
     */
    SearchResult getSearchResult(SearchResultOption resultOption);

    /**
     * 人/车属性查询
     *
     * @param type 图片类型（人、车）
     * @return 属性对象列表
     */
    List<Attribute> getAttribute(SearchType type);

    /**
     * 抓拍历史记录查询（陈柯）
     * 根据条件筛选抓拍图片，并返回图片对象
     *
     * @param option option中包含count、时间段、时间戳、人脸属性等值，根据这些值去筛选
     *               符合条件的图片对象并返回
     * @return List<SearchResult>符合条件的图片对象以list形式返回
     */
    List<SearchResult> getCaptureHistory(SearchOption option);
}
