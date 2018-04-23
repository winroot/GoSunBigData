package com.hzgc.service.clustering.controller;

import com.hzgc.common.service.BigDataPath;
import com.hzgc.common.service.ResponseResult;
import com.hzgc.dubbo.clustering.AlarmInfo;
import com.hzgc.service.clustering.service.ClusteringInfo;
import com.hzgc.service.clustering.dto.ClusteringSearchDTO;
import com.hzgc.service.clustering.service.ClusteringSearchServiceImpl;
import com.hzgc.service.clustering.vo.ClusteringSaveVO;
import com.hzgc.service.clustering.vo.ClusteringSearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@FeignClient(name = "clustering")
@RequestMapping(value = BigDataPath.CLUSTERING)
public class ClusteringController {

    @Autowired
    private ClusteringSearchServiceImpl clusteringSearchService;

    @RequestMapping(value = BigDataPath.CLUSTERING_SEARCH)
    public ResponseResult clusteringSearch(ClusteringSearchVO clusteringSearchVO) {
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

    @RequestMapping(value = BigDataPath.CLUSTERING_DETAILSEARCH)
    public ResponseResult detailClusteringSearch(ClusteringSearchVO clusteringSearchVO) {
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

    @RequestMapping(value = BigDataPath.CLUSTERING_DETAILSEARCH_V1)
    public ResponseResult detailClusteringSearch_v1(ClusteringSearchVO clusteringSearchVO) {
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

    @RequestMapping(value = BigDataPath.CLUSTERING_DELETE)
    public ResponseResult deleteClustering(ClusteringSaveVO clusteringSaveVO) {
        List<String> clusterIdList;
        String time;
        String flag;
        if (clusteringSaveVO != null){
            clusterIdList = clusteringSaveVO.getClusterIdList();
            time = clusteringSaveVO.getTime();
            flag = clusteringSaveVO.getFlag();
        }else {
            return null;
        }
        boolean succeed = clusteringSearchService.deleteClustering(clusterIdList,time,flag);
        return ResponseResult.init(succeed);
    }

    @RequestMapping(value = BigDataPath.CLUSTERING_IGNORE)
    public ResponseResult ignoreClustering(ClusteringSaveVO clusteringSaveVO) {
        List<String> clusterIdList;
        String time;
        String flag;
        if (clusteringSaveVO != null){
            clusterIdList = clusteringSaveVO.getClusterIdList();
            time = clusteringSaveVO.getTime();
            flag = clusteringSaveVO.getFlag();
        }else {
            return null;
        }
        boolean succeed = clusteringSearchService.ignoreClustering(clusterIdList,time,flag);
        return ResponseResult.init(succeed);
    }
}
