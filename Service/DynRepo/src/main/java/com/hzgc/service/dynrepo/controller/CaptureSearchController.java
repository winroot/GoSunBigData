package com.hzgc.service.dynrepo.controller;

import com.hzgc.common.attribute.bean.Attribute;
import com.hzgc.common.attribute.service.AttributeService;
import com.hzgc.common.util.searchtype.SearchType;
import com.hzgc.service.dynrepo.bean.SearchOption;
import com.hzgc.service.dynrepo.bean.SearchResult;
import com.hzgc.service.dynrepo.bean.SearchResultOption;
import com.hzgc.service.dynrepo.service.CaptureHistoryService;
import com.hzgc.service.dynrepo.service.CaptureSearchService;
import com.hzgc.service.util.response.ResponseResult;
import com.hzgc.service.util.rest.BigDataPath;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
@FeignClient(name = "dynRepo")
@RequestMapping(value = BigDataPath.DYNREPO, consumes = "application/json", produces = "application/json")
@Api(value = "/dynRepoSearch", tags = "以图搜图服务")
public class CaptureSearchController {

    private AttributeService attributeService = new AttributeService();
    @Autowired
    private CaptureHistoryService captureHistoryService;
    @Autowired
    private CaptureSearchService captureSearchService;

    /**
     * 以图搜图
     *
     * @param searchOption 以图搜图入参
     * @return SearchResult
     */
    @ApiOperation(value = "以图搜图", response = SearchResult.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful response"),
            @ApiResponse(code = 404, message = "404")})
    @RequestMapping(value = BigDataPath.DYNREPO_SEARCH, method = RequestMethod.POST)
    public ResponseResult<SearchResult> searchPicture(
            @RequestBody @ApiParam(value = "以图搜图入参") SearchOption searchOption) throws SQLException {
        SearchResult searchResult;
        if (searchOption != null) {
            searchResult = captureSearchService.searchPicture(searchOption);
        } else {
            searchResult = null;
        }
        return ResponseResult.init(searchResult);
    }

    /**
     * 历史搜索记录查询
     *
     * @param searchResultOption 以图搜图入参
     * @return SearchResult
     */
    @ApiOperation(value = "历史搜索记录查询", response = SearchResult.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful response"),
            @ApiResponse(code = 404, message = "404")})
    @RequestMapping(value = BigDataPath.DYNREPO_SEARCHRESULT, method = RequestMethod.POST)
    public ResponseResult<SearchResult> getSearchResult(
            @RequestBody @ApiParam(value = "以图搜图入参") SearchResultOption searchResultOption) {
        SearchResult searchResult;
        if (searchResultOption != null) {
            searchResult = captureSearchService.getSearchResult(searchResultOption);
        } else {
            searchResult = null;
        }
        return ResponseResult.init(searchResult);
    }

    /**
     * 人/车属性查询
     *
     * @param searchType 以图搜图入参
     * @return List<Attribute>
     */
    @ApiOperation(value = "属性特征查询", response = Attribute.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful response"),
            @ApiResponse(code = 404, message = "404")})
    @RequestMapping(value = BigDataPath.DYNREPO_ATTRIBUTE, method = RequestMethod.GET)
    public ResponseResult<List<Attribute>> getAttribute(
            @RequestBody @ApiParam(value = "以图搜图入参") SearchType searchType) {
        List<Attribute> attributeList;
        if (searchType != null) {
            attributeList = attributeService.getAttribute(searchType);
        } else {
            attributeList = null;
        }
        return ResponseResult.init(attributeList);
    }

    /**
     * 抓拍历史记录查询
     *
     * @param searchOption 以图搜图入参
     * @return List<SearchResult>
     */
    @ApiOperation(value = "抓拍历史记录查询", response = SearchResult.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful response"),
            @ApiResponse(code = 404, message = "404")})
    @RequestMapping(value = BigDataPath.DYNREPO_HISTORY, method = RequestMethod.POST)
    public ResponseResult<List<SearchResult>> getCaptureHistory(
            @RequestBody @ApiParam(value = "以图搜图入参") SearchOption searchOption) {
        List<SearchResult> searchResultList;
        if (searchOption != null) {
            searchResultList = captureHistoryService.getCaptureHistory(searchOption);
        } else {
            searchResultList = null;
        }
        return ResponseResult.init(searchResultList);
    }
}
