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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@FeignClient(name = "dynRepo")
@RequestMapping(value = BigDataPath.CAPTUREPICTURESEARCH)
public class CapturePictureSearchController {

    @Autowired
    private CapturePictureSearchServiceImpl capturePictureSearchService;

    @RequestMapping(value = BigDataPath.CAPTUREPICTURESEARCH_SEARCH)
    public ResponseResult search(CapturePictureSearchVO capturePictureSearchVO) {
        SearchOption searchOption;
        if (capturePictureSearchVO != null) {
            searchOption = capturePictureSearchVO.getSearchOption();
        } else {
            return null;
        }
        SearchResult searchResult = capturePictureSearchService.search(searchOption);
        return ResponseResult.init(searchResult);
    }

    @RequestMapping(value = BigDataPath.CAPTUREPICTURESEARCH_SEARCHRESULT)
    public ResponseResult getSearchResult(CapturePictureSearchVO capturePictureSearchVO) {
        SearchResultOption searchResultOption;
        if (capturePictureSearchVO != null) {
            searchResultOption = capturePictureSearchVO.getSearchResultOption();
        } else {
            return null;
        }
        SearchResult searchResult = capturePictureSearchService.getSearchResult(searchResultOption);
        return ResponseResult.init(searchResult);
    }

    @RequestMapping(value = BigDataPath.CAPTUREPICTURESEARCH_ATTRIBUTE)
    public ResponseResult getAttribute(CapturePictureSearchVO capturePictureSearchVO) {
        SearchType type;
        if (capturePictureSearchVO != null) {
            type = capturePictureSearchVO.getType();
        } else {
            return null;
        }
        List<Attribute> attributeList = capturePictureSearchService.getAttribute(type);
        return ResponseResult.init(attributeList);
    }

    @RequestMapping(value = BigDataPath.CAPTUREPICTURESEARCH_COUNT)
    public ResponseResult captureCountQuery(CapturePictureSearchVO capturePictureSearchVO) {
        String startTime;
        String endTime;
        String ipcId;
        if (capturePictureSearchVO != null) {
            startTime = capturePictureSearchVO.getStartTime();
            endTime = capturePictureSearchVO.getEndTime();
            ipcId = capturePictureSearchVO.getIpcId();
        } else {
            return null;
        }
        CaptureCount captureCount = capturePictureSearchService.captureCountQuery(startTime, endTime, ipcId);
        return ResponseResult.init(captureCount);
    }

    @RequestMapping(value = BigDataPath.CAPTUREPICTURESEARCH_COUNTS)
    public ResponseResult getCaptureNumber(CapturePictureSearchVO capturePictureSearchVO) {
        String startTime;
        String endTime;
        List<String> ipcIdList;
        if (capturePictureSearchVO != null) {
            startTime = capturePictureSearchVO.getStartTime();
            endTime = capturePictureSearchVO.getEndTime();
            ipcIdList = capturePictureSearchVO.getIpcIdList();
        } else {
            return null;
        }
        Long count = capturePictureSearchService.getCaptureNumber(startTime, endTime, ipcIdList);
        return ResponseResult.init(count);
    }

    @RequestMapping(value = BigDataPath.CAPTUREPICTURESEARCH_HISTORY)
    public ResponseResult getCaptureHistory(CapturePictureSearchVO capturePictureSearchVO) {
        SearchOption searchOption;
        if (capturePictureSearchVO != null) {
            searchOption = capturePictureSearchVO.getSearchOption();
        } else {
            return null;
        }
        List<SearchResult> searchResultList = capturePictureSearchService.getCaptureHistory(searchOption);
        return ResponseResult.init(searchResultList);
    }

    @RequestMapping(value = BigDataPath.CAPTUREPICTURESEARCH_ATTRIBUTECOUNT)
    public ResponseResult captureAttributeQuery(CapturePictureSearchVO capturePictureSearchVO) {
        String startTime;
        String endTime;
        List<String> ipcIdList;
        SearchType type;
        if (capturePictureSearchVO != null) {
            startTime = capturePictureSearchVO.getStartTime();
            endTime = capturePictureSearchVO.getEndTime();
            ipcIdList = capturePictureSearchVO.getIpcIdList();
            type = capturePictureSearchVO.getType();
        } else {
            return null;
        }
        List<AttributeCount> attributeCountList = capturePictureSearchService.captureAttributeQuery(startTime, endTime, ipcIdList, type);
        return ResponseResult.init(attributeCountList);
    }
}
