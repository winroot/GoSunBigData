package com.hzgc.service.visual.service;

import com.hzgc.common.util.json.JSONUtil;
import com.hzgc.service.clustering.bean.ClusterStatistics;
import com.hzgc.service.starepo.bean.export.EmigrationCount;
import com.hzgc.service.util.rest.BigDataPath;
import com.hzgc.service.visual.bean.PeopleManager;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PeopleManagerService {
    @Autowired
    @SuppressWarnings("unused")
    private RestTemplate restTemplate;

    public List<PeopleManager> peopleCount(String start_time, String end_time) {
        List<EmigrationCount> emigrationCounts = emigrationCount(start_time, end_time);
        List<ClusterStatistics> clusterStatistics = clusteringCount(start_time, end_time);
        System.out.println(JSONUtil.toJson(emigrationCounts));
        System.out.println(JSONUtil.toJson(clusterStatistics));
        Map<String, ClusterStatistics> statisticsMapping =
                clusterStatistics.stream().collect(Collectors.toMap(ClusterStatistics::getMonth, data -> data));

        List<PeopleManager> peopleManagers = new ArrayList<>();
        emigrationCounts.forEach(x -> {
            PeopleManager peopleManager = new PeopleManager();
            peopleManager.setMoveOutCount(x.getCount());
            peopleManager.setMonth(x.getMonth());
            peopleManager.setMoveInCount(statisticsMapping.get(x.getMonth()).getNum());
            peopleManagers.add(peopleManager);
        });
        return peopleManagers;
    }

    @HystrixCommand(fallbackMethod = "emigrationCountError")
    private List<EmigrationCount> emigrationCount(String start_time, String end_time) {
        ResponseEntity<EmigrationCount[]> responseEntity = restTemplate.getForEntity(
                "http://starepo/" + BigDataPath.STAREPO_COUNT_EMIGRATION
                        + "?start_time=" + start_time + "&end_time=" + end_time,
                EmigrationCount[].class
        );
        return Arrays.asList(responseEntity.getBody());
    }

    @SuppressWarnings("unused")
    private List<EmigrationCount> emigrationCountError(String start_time, String end_time) {
        log.error("Emigration count faild");
        return null;
    }

    @HystrixCommand(fallbackMethod = "clusteringCountError")
    private List<ClusterStatistics> clusteringCount(String start_time, String end_time) {
        ResponseEntity<ClusterStatistics[]> responseEntity = restTemplate.getForEntity(
                "http://clustering/" + BigDataPath.CLUSTERING_TOTLE
                        + "?start_time=" + start_time + "&end_time=" + end_time,
                ClusterStatistics[].class);
        return Arrays.asList(responseEntity.getBody());
    }

    @SuppressWarnings("unused")
    private List<ClusterStatistics> clusteringCountError(String start_time, String end_time) {
        log.error("Clustering count error");
        return null;
    }
}