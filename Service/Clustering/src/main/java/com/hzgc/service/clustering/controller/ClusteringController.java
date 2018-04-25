package com.hzgc.service.clustering.controller;

import com.hzgc.common.service.BigDataPath;
import com.hzgc.common.service.ResponseResult;
import com.hzgc.common.service.clustering.AlarmInfo;
import com.hzgc.service.clustering.service.ClusteringInfo;
import com.hzgc.service.clustering.dto.ClusteringSearchDTO;
import com.hzgc.service.clustering.service.ClusteringSearchServiceImpl;
import com.hzgc.service.clustering.vo.ClusteringSaveVO;
import com.hzgc.service.clustering.vo.ClusteringSearchVO;
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
    private ClusteringSearchServiceImpl clusteringSearchService;

    @ApiOperation(value = "聚类信息查询", response = ClusteringSearchDTO.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful response"),
            @ApiResponse(code = 404, message = "404")})
    @RequestMapping(value = BigDataPath.CLUSTERING_SEARCH, method = RequestMethod.POST)
    public ResponseResult<ClusteringSearchDTO> clusteringSearch(
            @RequestBody @ApiParam(value = "聚类信息查询入参") ClusteringSearchVO clusteringSearchVO) {
        String region;
        String time;
        int start;
        int limit;
        String sortParam;
        if (clusteringSearchVO != null) {
            region = clusteringSearchVO.getRegion();
            time = clusteringSearchVO.getTime();
            start = clusteringSearchVO.getStart();
            limit = clusteringSearchVO.getLimt();
            sortParam = clusteringSearchVO.getSortParam();
        } else {
            return null;
        }
        ClusteringInfo clusteringInfo = clusteringSearchService.clusteringSearch(region, time, start, limit, sortParam);
        ClusteringSearchDTO clusteringSearchDTO = new ClusteringSearchDTO();
        clusteringSearchDTO.setClusteringInfo(clusteringInfo);
        return ResponseResult.init(clusteringSearchDTO);
    }

    @ApiOperation(value = "单个聚类信息详细查询", response = ClusteringSearchDTO.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful response"),
            @ApiResponse(code = 404, message = "404")})
    @RequestMapping(value = BigDataPath.CLUSTERING_DETAILSEARCH, method = RequestMethod.POST)
    public ResponseResult<ClusteringSearchDTO> detailClusteringSearch(
            @RequestBody @ApiParam(value = "聚类信息查询入参") ClusteringSearchVO clusteringSearchVO) {
        String clusterId;
        String time;
        int start;
        int limit;
        String sortParam;
        if (clusteringSearchVO != null) {
            clusterId = clusteringSearchVO.getClusterId();
            time = clusteringSearchVO.getTime();
            start = clusteringSearchVO.getStart();
            limit = clusteringSearchVO.getLimt();
            sortParam = clusteringSearchVO.getSortParam();
        } else {
            return null;
        }
        List<AlarmInfo> alarmInfoList = clusteringSearchService.detailClusteringSearch(clusterId, time, start, limit, sortParam);
        ClusteringSearchDTO clusteringSearchDTO = new ClusteringSearchDTO();
        clusteringSearchDTO.setAlarmInfoList(alarmInfoList);
        return ResponseResult.init(clusteringSearchDTO);
    }

    @ApiOperation(value = "单个聚类信息详细查询(告警ID)", response = ClusteringSearchDTO.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful response"),
            @ApiResponse(code = 404, message = "404")})
    @RequestMapping(value = BigDataPath.CLUSTERING_DETAILSEARCH_V1, method = RequestMethod.POST)
    public ResponseResult<ClusteringSearchDTO> detailClusteringSearch_v1(
            @RequestBody @ApiParam(value = "聚类信息查询入参") ClusteringSearchVO clusteringSearchVO) {
        String clusterId;
        String time;
        int start;
        int limit;
        String sortParam;
        if (clusteringSearchVO != null) {
            clusterId = clusteringSearchVO.getClusterId();
            time = clusteringSearchVO.getTime();
            start = clusteringSearchVO.getStart();
            limit = clusteringSearchVO.getLimt();
            sortParam = clusteringSearchVO.getSortParam();
        } else {
            return null;
        }
        List<Integer> alarmIdList = clusteringSearchService.detailClusteringSearch_v1(clusterId, time, start, limit, sortParam);
        ClusteringSearchDTO clusteringSearchDTO = new ClusteringSearchDTO();
        clusteringSearchDTO.setAlarmIdList(alarmIdList);
        return ResponseResult.init(clusteringSearchDTO);
    }

    @ApiOperation(value = "删除聚类信息", response = ClusteringSearchDTO.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful response"),
            @ApiResponse(code = 404, message = "404")})
    @RequestMapping(value = BigDataPath.CLUSTERING_DELETE, method = RequestMethod.DELETE)
    public ResponseResult<Boolean> deleteClustering(
            @RequestBody @ApiParam(value = "聚类信息存储入参") ClusteringSaveVO clusteringSaveVO) {
        List<String> clusterIdList;
        String time;
        String flag;
        if (clusteringSaveVO != null) {
            clusterIdList = clusteringSaveVO.getClusterIdList();
            time = clusteringSaveVO.getTime();
            flag = clusteringSaveVO.getFlag();
        } else {
            return null;
        }
        boolean succeed = clusteringSearchService.deleteClustering(clusterIdList, time, flag);
        return ResponseResult.init(succeed);
    }

    @ApiOperation(value = "忽视聚类信息", response = ClusteringSearchDTO.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful response"),
            @ApiResponse(code = 404, message = "404")})
    @RequestMapping(value = BigDataPath.CLUSTERING_IGNORE, method = RequestMethod.POST)
    public ResponseResult<Boolean> ignoreClustering(
            @RequestBody @ApiParam(value = "聚类信息存储入参") ClusteringSaveVO clusteringSaveVO) {
        List<String> clusterIdList;
        String time;
        String flag;
        if (clusteringSaveVO != null) {
            clusterIdList = clusteringSaveVO.getClusterIdList();
            time = clusteringSaveVO.getTime();
            flag = clusteringSaveVO.getFlag();
        } else {
            return null;
        }
        boolean succeed = clusteringSearchService.ignoreClustering(clusterIdList, time, flag);
        return ResponseResult.init(succeed);
    }
}
