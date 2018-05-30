package com.hzgc.service.dynrepo.controller;

import com.hzgc.common.attribute.service.AttributeService;
import com.hzgc.common.util.json.JSONUtil;
import com.hzgc.common.util.uuid.UuidUtil;
import com.hzgc.service.dynrepo.bean.*;
import com.hzgc.service.dynrepo.service.CaptureHistoryService;
import com.hzgc.service.dynrepo.service.CaptureSearchService;
import com.hzgc.service.dynrepo.service.CaptureServiceHelper;
import com.hzgc.service.util.error.RestErrorCode;
import com.hzgc.service.util.response.ResponseResult;
import com.hzgc.service.util.rest.BigDataPath;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
//@RequestMapping(value = BigDataPath.DYNREPO)
@Api(tags = "动态库服务")
@Slf4j
@SuppressWarnings("unused")
public class CaptureSearchController {

    @Autowired
    @SuppressWarnings("unused")
    private AttributeService attributeService;
    @Autowired
    @SuppressWarnings("unused")
    private CaptureHistoryService captureHistoryService;
    @Autowired
    @SuppressWarnings("unused")
    private CaptureSearchService captureSearchService;
    @Autowired
    @SuppressWarnings("unused")
    private CaptureServiceHelper captureServiceHelper;

    /**
     * 以图搜图
     *
     * @param searchOption 以图搜图入参
     * @return SearchResult
     */
    @ApiOperation(value = "以图搜图", response = SearchResult.class)
    @RequestMapping(value = BigDataPath.DYNREPO_SEARCH, method = RequestMethod.POST)
    @SuppressWarnings("unused")
    public ResponseResult<SearchResult> searchPicture(
            @RequestBody @ApiParam(value = "以图搜图查询参数") SearchOption searchOption) throws SQLException {
        SearchResult searchResult;
        if (searchOption == null) {
            log.error("Start search picture, but search option is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
        }

        if (searchOption.getImages() == null && searchOption.getImages().size() < 1) {
            log.error("Start search picture, but images is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
        }

        if (searchOption.getSimilarity() < 0.0) {
            log.error("Start search picture, but threshold is error");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
        }
        log.info("Start convert device id to ipc id");
        captureServiceHelper.capturOptionConver(searchOption);
        if (searchOption.getDeviceIpcs() == null
                || searchOption.getDeviceIpcs().size() <= 0
                || searchOption.getDeviceIpcs().get(0) == null) {
            log.error("Start search picture, but deviceIpcs option is error");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
        }
        log.info("Start search picture, set search id");
        String searchId = UuidUtil.getUuid();
        log.info("Start search picture, search option is:" + JSONUtil.toJson(searchOption));
        searchResult = captureSearchService.searchPicture(searchOption, searchId);
        return ResponseResult.init(searchResult);
    }

    /**
     * 获取搜索原图
     *
     * @param image_name 原图ID
     * @return 图片二进制
     */
    @ApiOperation(value = "获取原图", produces = "image/jpeg")
    @ApiImplicitParam(name = "image_name", value = "原图ID", paramType = "query")
    @RequestMapping(value = BigDataPath.DYNREPO_GETPICTURE, method = RequestMethod.GET)
    public ResponseEntity<byte[]> getSearchPicture(String image_name) {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(captureSearchService.getSearchPicture(image_name));
    }

    /**
     * 历史搜索记录查询
     *
     * @param searchResultOption 以图搜图入参
     * @return SearchResult
     */
    @ApiOperation(value = "获取历史搜图结果", response = SearchResult.class)
    @RequestMapping(value = BigDataPath.DYNREPO_SEARCHRESULT, method = RequestMethod.POST)
    @ApiImplicitParam(name = "searchResultOption", value = "历史结果查询参数", paramType = "body")
    @SuppressWarnings("unused")
    public ResponseResult<SearchResult> getSearchResult(
            @RequestBody SearchResultOption searchResultOption) {
        SearchResult searchResult;
        if (searchResultOption != null) {
            searchResult = captureSearchService.getSearchResult(searchResultOption);
        } else {
            searchResult = null;
        }
        return ResponseResult.init(searchResult);
    }

    /**
     * 查询搜索记录
     *
     * @param start_time 历史记录起始时间
     * @param end_time   历史记录结束时间
     * @param sort       排序参数
     * @param start      起始位置
     * @param limit      返回条数
     * @return 返回查询结果
     */
    @ApiOperation(value = "搜图记录查询", response = SearchHisotry.class, responseContainer = "List")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "start_time", value = "起始时间", paramType = "query"),
            @ApiImplicitParam(name = "end_time", value = "结束时间", paramType = "query"),
            @ApiImplicitParam(name = "sort", value = "排序参数", paramType = "query"),
            @ApiImplicitParam(name = "start", value = "起始位置", paramType = "query", required = true),
            @ApiImplicitParam(name = "limit", value = "返回条数", paramType = "query", required = true)
    })
    @SuppressWarnings("unused")
    @RequestMapping(value = BigDataPath.DYNREPO_SEARCHHISTORY, method = RequestMethod.GET)
    public ResponseResult<List<SearchHisotry>> getSearchHistory(
            String start_time,
            String end_time,
            String sort,
            @RequestParam int start,
            @RequestParam int limit) {
        if (start < 0 || limit <= 0) {
            log.error("Start get search history, start is:" + start + ", limit is:" + limit);
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
        }
        if (!StringUtils.isBlank(start_time) && !StringUtils.isBlank(end_time) && !StringUtils.isBlank(sort)) {
            return ResponseResult.init(captureSearchService.getSearchHistory(start_time, end_time, sort, start, limit));
        } else {
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String endTime = format.format(System.currentTimeMillis());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, -7);
            String startTime = format.format(calendar.getTime());
            log.info("Start get search history, start time is:" + startTime +
                    ", end time is:" + endTime +
                    ", start is:" + start +
                    ", limit is:" + limit);
            return ResponseResult.init(captureSearchService.getSearchHistory(startTime, endTime, sort, start, limit));
        }
    }

    /**
     * 抓拍历史记录查询
     *
     * @param captureOption 以图搜图入参
     * @return List<SearchResult>
     */
    @ApiOperation(value = "抓拍历史查询", response = SearchResult.class, responseContainer = "List")
    @ApiImplicitParam(name = "searchOption", value = "抓拍历史查询参数", paramType = "body")
    @RequestMapping(value = BigDataPath.DYNREPO_HISTORY, method = RequestMethod.POST)
    @SuppressWarnings("unused")
    public ResponseResult<List<SingleCaptureResult>> getCaptureHistory(
            @RequestBody @ApiParam(value = "以图搜图入参") CaptureOption captureOption) {
        if (captureOption == null) {
            log.error("Start query capture history, capture option is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
        }
        log.info("Start convert device id to ipc id");
        captureServiceHelper.capturOptionConver(captureOption);
        if (captureOption.getDeviceIpcs() == null ||
                captureOption.getDeviceIpcs().size() <= 0 ||
                captureOption.getDeviceIpcs().get(0) == null) {
            log.error("Start query capture history, deviceIpcs option is error");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
        }
        log.info("Start query capture history, search option is:" + JSONUtil.toJson(captureOption));
        List<SingleCaptureResult> searchResultList =
                captureHistoryService.getCaptureHistory(captureOption);
        return ResponseResult.init(searchResultList);
    }
}
