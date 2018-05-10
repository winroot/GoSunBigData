package com.hzgc.service.clustering.controller;

import com.hzgc.common.clustering.AlarmInfo;
import com.hzgc.service.clustering.bean.ClusteringSaveParam;
import com.hzgc.service.clustering.bean.ClusteringSearchParam;
import com.hzgc.service.clustering.bean.ClusteringInfo;
import com.hzgc.service.clustering.service.ClusteringSearchService;
import com.hzgc.service.util.response.ResponseResult;
import com.hzgc.service.util.rest.BigDataPath;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@FeignClient(name = "clustering")
@RequestMapping(value = BigDataPath.CLUSTERING, consumes = "application/json", produces = "application/json")
@Api(value = "/clustering", tags = "聚类服务")
public class ClusteringController {

    @Autowired
    private ClusteringSearchService clusteringSearchService;

    @ApiOperation(value = "聚类信息查询", response = ClusteringInfo.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful response"),
            @ApiResponse(code = 404, message = "404")})
    @RequestMapping(value = BigDataPath.CLUSTERING_SEARCH, method = RequestMethod.POST)
    public ResponseResult<ClusteringInfo> clusteringSearch(
            @RequestBody @ApiParam(value = "聚类信息查询入参") ClusteringSearchParam clusteringSearchParam) {
        String region;
        String time;
        int start;
        int limit;
        String sortParam;
        if (clusteringSearchParam != null) {
            region = clusteringSearchParam.getRegion();
            time = clusteringSearchParam.getTime();
            start = clusteringSearchParam.getStart();
            limit = clusteringSearchParam.getLimt();
            sortParam = clusteringSearchParam.getSortParam();
        } else {
            return null;
        }
        ClusteringInfo clusteringInfo = clusteringSearchService.clusteringSearch(region, time, start, limit, sortParam);
        return ResponseResult.init(clusteringInfo);
    }

    @ApiOperation(value = "单个聚类信息详细查询", response = AlarmInfo.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful response"),
            @ApiResponse(code = 404, message = "404")})
    @RequestMapping(value = BigDataPath.CLUSTERING_DETAILSEARCH, method = RequestMethod.POST)
    public ResponseResult<List<AlarmInfo>> detailClusteringSearch(
            @RequestBody @ApiParam(value = "聚类信息查询入参") ClusteringSearchParam clusteringSearchParam) {
        String clusterId;
        String time;
        int start;
        int limit;
        String sortParam;
        if (clusteringSearchParam != null) {
            clusterId = clusteringSearchParam.getClusterId();
            time = clusteringSearchParam.getTime();
            start = clusteringSearchParam.getStart();
            limit = clusteringSearchParam.getLimt();
            sortParam = clusteringSearchParam.getSortParam();
        } else {
            return null;
        }
        List<AlarmInfo> alarmInfoList = clusteringSearchService.detailClusteringSearch(clusterId, time, start, limit, sortParam);
        return ResponseResult.init(alarmInfoList);
    }

    @ApiOperation(value = "单个聚类信息详细查询(告警ID)", response = Integer.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful response"),
            @ApiResponse(code = 404, message = "404")})
    @RequestMapping(value = BigDataPath.CLUSTERING_DETAILSEARCH_V1, method = RequestMethod.POST)
    public ResponseResult<List<Integer>> detailClusteringSearch_v1(
            @RequestBody @ApiParam(value = "聚类信息查询入参") ClusteringSearchParam clusteringSearchParam) {
        String clusterId;
        String time;
        int start;
        int limit;
        String sortParam;
        if (clusteringSearchParam != null) {
            clusterId = clusteringSearchParam.getClusterId();
            time = clusteringSearchParam.getTime();
            start = clusteringSearchParam.getStart();
            limit = clusteringSearchParam.getLimt();
            sortParam = clusteringSearchParam.getSortParam();
        } else {
            return null;
        }
        List<Integer> alarmIdList = clusteringSearchService.detailClusteringSearch_v1(clusterId, time, start, limit, sortParam);
        return ResponseResult.init(alarmIdList);
    }

    @ApiOperation(value = "删除聚类信息", response = Boolean.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful response"),
            @ApiResponse(code = 404, message = "404")})
    @RequestMapping(value = BigDataPath.CLUSTERING_DELETE, method = RequestMethod.DELETE)
    public ResponseResult<Boolean> deleteClustering(
            @RequestBody @ApiParam(value = "聚类信息存储入参") ClusteringSaveParam clusteringSaveParam) {
        List<String> clusterIdList;
        String time;
        String flag;
        if (clusteringSaveParam != null) {
            clusterIdList = clusteringSaveParam.getClusterIdList();
            time = clusteringSaveParam.getTime();
            flag = clusteringSaveParam.getFlag();
        } else {
            return null;
        }
        boolean succeed = clusteringSearchService.deleteClustering(clusterIdList, time, flag);
        return ResponseResult.init(succeed);
    }

    @ApiOperation(value = "忽视聚类信息", response = Boolean.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful response"),
            @ApiResponse(code = 404, message = "404")})
    @RequestMapping(value = BigDataPath.CLUSTERING_IGNORE, method = RequestMethod.POST)
    public ResponseResult<Boolean> ignoreClustering(
            @RequestBody @ApiParam(value = "聚类信息存储入参") ClusteringSaveParam clusteringSaveParam) {
        List<String> clusterIdList;
        String time;
        String flag;
        if (clusteringSaveParam != null) {
            clusterIdList = clusteringSaveParam.getClusterIdList();
            time = clusteringSaveParam.getTime();
            flag = clusteringSaveParam.getFlag();
        } else {
            return null;
        }
        boolean succeed = clusteringSearchService.ignoreClustering(clusterIdList, time, flag);
        return ResponseResult.init(succeed);
    }
}
