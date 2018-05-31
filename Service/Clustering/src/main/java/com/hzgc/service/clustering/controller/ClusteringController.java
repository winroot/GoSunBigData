package com.hzgc.service.clustering.controller;

import com.hzgc.common.util.json.JSONUtil;
import com.hzgc.service.clustering.bean.ClusteringInfo;
import com.hzgc.service.clustering.bean.ClusteringSaveParam;
import com.hzgc.service.clustering.bean.ClusteringSearchParam;
import com.hzgc.service.clustering.service.ClusteringSearchService;
import com.hzgc.service.util.error.RestErrorCode;
import com.hzgc.service.util.response.ResponseResult;
import com.hzgc.service.util.rest.BigDataPath;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@Api(tags = "人口实名制管理")
public class ClusteringController {

    @Autowired
    private ClusteringSearchService clusteringSearchService;

    @ApiOperation(value = "建议迁入信息查询", response = ClusteringInfo.class)
    @RequestMapping(value = BigDataPath.CLUSTERING_SEARCH, method = RequestMethod.POST)
    public ResponseResult<ClusteringInfo> clusteringSearch(
            @RequestBody @ApiParam(value = "聚类信息查询参数") ClusteringSearchParam clusteringSearchParam) {
        if (clusteringSearchParam != null && clusteringSearchParam.getRegion() != null &&
                clusteringSearchParam.getTime() != null) {
            //若传入的start和limit为空，则默认从第一条开始，取所有
            int start = clusteringSearchParam.getStart();
            int limit = clusteringSearchParam.getLimit();
            if(start == 0){
                start = 1;
            }
            if(limit == 0){
                limit = Integer.MAX_VALUE;
            }
            log.info("Start searching param : " + JSONUtil.toJson(clusteringSearchParam));
            ClusteringInfo clusteringInfo = clusteringSearchService.clusteringSearch(clusteringSearchParam.getRegion()
                    , clusteringSearchParam.getTime() , start, limit, clusteringSearchParam.getSortParam());
            return ResponseResult.init(clusteringInfo);
        } else {
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
        }
    }


    @ApiOperation(value = "单个聚类信息详细查询(告警ID)", response = Integer.class, responseContainer = "List")
    @RequestMapping(value = BigDataPath.CLUSTERING_DETAILSEARCH_V1, method = RequestMethod.POST)
    public ResponseResult<List<Integer>> detailClusteringSearch_v1(
            @RequestBody @ApiParam(value = "聚类信息查询参数") ClusteringSearchParam clusteringSearchParam) {
        if (clusteringSearchParam != null && clusteringSearchParam.getClusterId() != null &&
                clusteringSearchParam.getTime() != null) {
            int start = clusteringSearchParam.getStart();
            int limit = clusteringSearchParam.getLimit();
            if(start == 0){
                start = 1;
            }
            if(limit == 0){
                limit = Integer.MAX_VALUE;
            }
            log.info("Starting searching param : " + JSONUtil.toJson(clusteringSearchParam));
            List<Integer> alarmIdList = clusteringSearchService.detailClusteringSearch_v1(
                    clusteringSearchParam.getClusterId(), clusteringSearchParam.getTime(), start, limit);
            return ResponseResult.init(alarmIdList);
        } else {
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
        }

    }

    @ApiOperation(value = "删除聚类信息", response = Boolean.class)
    @RequestMapping(value = BigDataPath.CLUSTERING_DELETE, method = RequestMethod.DELETE)
    public ResponseResult<Boolean> deleteClustering(
            @RequestBody @ApiParam(value = "聚类信息存储参数") ClusteringSaveParam clusteringSaveParam) {
        if (clusteringSaveParam != null && clusteringSaveParam.getClusterIdList() != null &&
                clusteringSaveParam.getTime() != null && clusteringSaveParam.getClusterIdList().size() > 0 &&
                clusteringSaveParam.getFlag() != null) {
            log.info("Starting delete param : " + JSONUtil.toJson(clusteringSaveParam));
            boolean succeed = clusteringSearchService.deleteClustering(clusteringSaveParam.getClusterIdList(),
                    clusteringSaveParam.getTime(), clusteringSaveParam.getFlag());
            return ResponseResult.init(succeed);
        } else {
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
        }
    }

    @ApiOperation(value = "忽视聚类信息", response = Boolean.class)
    @RequestMapping(value = BigDataPath.CLUSTERING_IGNORE, method = RequestMethod.POST)
    public ResponseResult<Boolean> ignoreClustering(
            @RequestBody @ApiParam(value = "聚类信息存储参数") ClusteringSaveParam clusteringSaveParam) {
        if (clusteringSaveParam != null && clusteringSaveParam.getClusterIdList() != null &&
                clusteringSaveParam.getTime() != null && clusteringSaveParam.getClusterIdList().size() > 0 &&
                clusteringSaveParam.getFlag() != null) {
            log.info("Starting ignore param : " + JSONUtil.toJson(clusteringSaveParam));
            boolean succeed = clusteringSearchService.ignoreClustering(clusteringSaveParam.getClusterIdList(),
                    clusteringSaveParam.getTime(), clusteringSaveParam.getFlag());
            return ResponseResult.init(succeed);
        } else {
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
        }
    }
}
