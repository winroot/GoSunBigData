package com.hzgc.service.dynrepo.controller;

import com.hzgc.common.service.BigDataPath;
import com.hzgc.common.service.ResponseResult;
import com.hzgc.common.util.searchtype.SearchType;
import com.hzgc.service.dynrepo.attribute.Attribute;
import com.hzgc.service.dynrepo.attribute.AttributeCount;
import com.hzgc.service.dynrepo.object.CaptureCount;
import com.hzgc.service.dynrepo.object.SearchOption;
import com.hzgc.service.dynrepo.object.SearchResult;
import com.hzgc.service.dynrepo.object.SearchResultOption;
import com.hzgc.service.dynrepo.service.CapturePictureSearchServiceImpl;
import com.hzgc.service.dynrepo.vo.CapturePictureSearchVO;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@FeignClient(name = "dynRepo")
@RequestMapping(value = BigDataPath.CAPTUREPICTURESEARCH, consumes = "application/json", produces = "application/json")
@Api(value = "/capturePictureSearch", tags = "以图搜图服务")
public class CapturePictureSearchController {

    @Autowired
    private CapturePictureSearchServiceImpl capturePictureSearchService;

    /**
     * 以图搜图
     *
     * @param capturePictureSearchVO 以图搜图入参
     * @return SearchResult
     */
    @ApiOperation(value = "以图搜图", response = SearchResult.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful response"),
            @ApiResponse(code = 404, message = "404")})
    @RequestMapping(value = BigDataPath.CAPTUREPICTURESEARCH_SEARCH, method = RequestMethod.POST)
    public ResponseResult<SearchResult> search(
            @RequestBody @ApiParam(value = "以图搜图入参") CapturePictureSearchVO capturePictureSearchVO) {
        SearchOption searchOption;
        if (capturePictureSearchVO != null) {
            searchOption = capturePictureSearchVO.getSearchOption();
        } else {
            return null;
        }
        SearchResult searchResult = capturePictureSearchService.search(searchOption);
        return ResponseResult.init(searchResult);
    }

    /**
     * 历史搜索记录查询
     *
     * @param capturePictureSearchVO 以图搜图入参
     * @return SearchResult
     */
    @ApiOperation(value = "历史搜索记录查询", response = SearchResult.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful response"),
            @ApiResponse(code = 404, message = "404")})
    @RequestMapping(value = BigDataPath.CAPTUREPICTURESEARCH_SEARCHRESULT, method = RequestMethod.POST)
    public ResponseResult<SearchResult> getSearchResult(
            @RequestBody @ApiParam(value = "以图搜图入参") CapturePictureSearchVO capturePictureSearchVO) {
        SearchResultOption searchResultOption;
        if (capturePictureSearchVO != null) {
            searchResultOption = capturePictureSearchVO.getSearchResultOption();
        } else {
            return null;
        }
        SearchResult searchResult = capturePictureSearchService.getSearchResult(searchResultOption);
        return ResponseResult.init(searchResult);
    }

    /**
     * 人/车属性查询
     *
     * @param capturePictureSearchVO 以图搜图入参
     * @return List<Attribute>
     */
    @ApiOperation(value = "属性特征查询", response = Attribute.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful response"),
            @ApiResponse(code = 404, message = "404")})
    @RequestMapping(value = BigDataPath.CAPTUREPICTURESEARCH_ATTRIBUTE, method = RequestMethod.GET)
    public ResponseResult<List<Attribute>> getAttribute(
            @RequestBody @ApiParam(value = "以图搜图入参") CapturePictureSearchVO capturePictureSearchVO) {
        SearchType type;
        if (capturePictureSearchVO != null) {
            type = capturePictureSearchVO.getType();
        } else {
            return null;
        }
        List<Attribute> attributeList = capturePictureSearchService.getAttribute(type);
        return ResponseResult.init(attributeList);
    }

    /**
     * 抓拍历史记录查询
     *
     * @param capturePictureSearchVO 以图搜图入参
     * @return List<SearchResult>
     */
    @ApiOperation(value = "抓拍历史记录查询", response = SearchResult.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful response"),
            @ApiResponse(code = 404, message = "404")})
    @RequestMapping(value = BigDataPath.CAPTUREPICTURESEARCH_HISTORY, method = RequestMethod.POST)
    public ResponseResult<List<SearchResult>> getCaptureHistory(
            @RequestBody @ApiParam(value = "以图搜图入参") CapturePictureSearchVO capturePictureSearchVO) {
        SearchOption searchOption;
        if (capturePictureSearchVO != null) {
            searchOption = capturePictureSearchVO.getSearchOption();
        } else {
            return null;
        }
        List<SearchResult> searchResultList = capturePictureSearchService.getCaptureHistory(searchOption);
        return ResponseResult.init(searchResultList);
    }
}
