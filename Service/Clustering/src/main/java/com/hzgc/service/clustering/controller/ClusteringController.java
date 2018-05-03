package com.hzgc.service.clustering.controller;

import com.hzgc.common.service.BigDataPath;
import com.hzgc.common.service.ResponseResult;
import com.hzgc.common.service.clustering.AlarmInfo;
import com.hzgc.service.clustering.bean.ClusteringVO;
import com.hzgc.service.clustering.service.ClusteringInfo;
import com.hzgc.service.clustering.service.ClusteringSearchService;
import com.hzgc.service.clustering.bean.ClusteringSaveDTO;
import com.hzgc.service.clustering.bean.ClusteringSearchDTO;
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

    @ApiOperation(value = "聚类信息查询", response = ClusteringVO.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful response"),
            @ApiResponse(code = 404, message = "404")})
    @RequestMapping(value = BigDataPath.CLUSTERING_SEARCH, method = RequestMethod.POST)
    public ResponseResult<ClusteringVO> clusteringSearch(
            @RequestBody @ApiParam(value = "聚类信息查询入参") ClusteringSearchDTO clusteringSearchDTO) {
        String region;
        String time;
        int start;
        int limit;
        String sortParam;
        if (clusteringSearchDTO != null) {
            region = clusteringSearchDTO.getRegion();
            time = clusteringSearchDTO.getTime();
            start = clusteringSearchDTO.getStart();
            limit = clusteringSearchDTO.getLimt();
            sortParam = clusteringSearchDTO.getSortParam();
        } else {
            return null;
        }
        ClusteringInfo clusteringInfo = clusteringSearchService.clusteringSearch(region, time, start, limit, sortParam);
        ClusteringVO clusteringVO = new ClusteringVO();
        clusteringVO.setClusteringInfo(clusteringInfo);
        return ResponseResult.init(clusteringVO);
    }

    @ApiOperation(value = "单个聚类信息详细查询", response = ClusteringVO.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful response"),
            @ApiResponse(code = 404, message = "404")})
    @RequestMapping(value = BigDataPath.CLUSTERING_DETAILSEARCH, method = RequestMethod.POST)
    public ResponseResult<ClusteringVO> detailClusteringSearch(
            @RequestBody @ApiParam(value = "聚类信息查询入参") ClusteringSearchDTO clusteringSearchDTO) {
        String clusterId;
        String time;
        int start;
        int limit;
        String sortParam;
        if (clusteringSearchDTO != null) {
            clusterId = clusteringSearchDTO.getClusterId();
            time = clusteringSearchDTO.getTime();
            start = clusteringSearchDTO.getStart();
            limit = clusteringSearchDTO.getLimt();
            sortParam = clusteringSearchDTO.getSortParam();
        } else {
            return null;
        }
        List<AlarmInfo> alarmInfoList = clusteringSearchService.detailClusteringSearch(clusterId, time, start, limit, sortParam);
        ClusteringVO clusteringVO = new ClusteringVO();
        clusteringVO.setAlarmInfoList(alarmInfoList);
        return ResponseResult.init(clusteringVO);
    }

    @ApiOperation(value = "单个聚类信息详细查询(告警ID)", response = ClusteringVO.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful response"),
            @ApiResponse(code = 404, message = "404")})
    @RequestMapping(value = BigDataPath.CLUSTERING_DETAILSEARCH_V1, method = RequestMethod.POST)
    public ResponseResult<ClusteringVO> detailClusteringSearch_v1(
            @RequestBody @ApiParam(value = "聚类信息查询入参") ClusteringSearchDTO clusteringSearchDTO) {
        String clusterId;
        String time;
        int start;
        int limit;
        String sortParam;
        if (clusteringSearchDTO != null) {
            clusterId = clusteringSearchDTO.getClusterId();
            time = clusteringSearchDTO.getTime();
            start = clusteringSearchDTO.getStart();
            limit = clusteringSearchDTO.getLimt();
            sortParam = clusteringSearchDTO.getSortParam();
        } else {
            return null;
        }
        List<Integer> alarmIdList = clusteringSearchService.detailClusteringSearch_v1(clusterId, time, start, limit, sortParam);
        ClusteringVO clusteringVO = new ClusteringVO();
        clusteringVO.setAlarmIdList(alarmIdList);
        return ResponseResult.init(clusteringVO);
    }

    @ApiOperation(value = "删除聚类信息", response = ClusteringVO.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful response"),
            @ApiResponse(code = 404, message = "404")})
    @RequestMapping(value = BigDataPath.CLUSTERING_DELETE, method = RequestMethod.DELETE)
    public ResponseResult<Boolean> deleteClustering(
            @RequestBody @ApiParam(value = "聚类信息存储入参") ClusteringSaveDTO clusteringSaveDTO) {
        List<String> clusterIdList;
        String time;
        String flag;
        if (clusteringSaveDTO != null) {
            clusterIdList = clusteringSaveDTO.getClusterIdList();
            time = clusteringSaveDTO.getTime();
            flag = clusteringSaveDTO.getFlag();
        } else {
            return null;
        }
        boolean succeed = clusteringSearchService.deleteClustering(clusterIdList, time, flag);
        return ResponseResult.init(succeed);
    }

    @ApiOperation(value = "忽视聚类信息", response = ClusteringVO.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful response"),
            @ApiResponse(code = 404, message = "404")})
    @RequestMapping(value = BigDataPath.CLUSTERING_IGNORE, method = RequestMethod.POST)
    public ResponseResult<Boolean> ignoreClustering(
            @RequestBody @ApiParam(value = "聚类信息存储入参") ClusteringSaveDTO clusteringSaveDTO) {
        List<String> clusterIdList;
        String time;
        String flag;
        if (clusteringSaveDTO != null) {
            clusterIdList = clusteringSaveDTO.getClusterIdList();
            time = clusteringSaveDTO.getTime();
            flag = clusteringSaveDTO.getFlag();
        } else {
            return null;
        }
        boolean succeed = clusteringSearchService.ignoreClustering(clusterIdList, time, flag);
        return ResponseResult.init(succeed);
    }
}
