package com.hzgc.service.visual.service;

import com.hzgc.common.util.json.JSONUtil;
import com.hzgc.service.util.bean.PeopleManagerCount;
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
        List<PeopleManagerCount> staRepo = emigrationCount(start_time, end_time);
        List<PeopleManagerCount> clustering = clusteringCount(start_time, end_time);
        Map<String, PeopleManagerCount> statisticsMapping =
                clustering.stream().collect(Collectors.toMap(PeopleManagerCount::getMonth, data -> data));

        List<PeopleManager> peopleManagers = new ArrayList<>();
        staRepo.forEach(x -> {
            PeopleManager peopleManager = new PeopleManager();
            peopleManager.setMoveOutCount(x.getRemovePeople());
            peopleManager.setMonth(x.getMonth());
            peopleManager.setMoveInCount(statisticsMapping.get(x.getMonth()).getAddPeople());
            peopleManagers.add(peopleManager);
        });
        return peopleManagers;
    }

    @HystrixCommand(fallbackMethod = "emigrationCountError")
    private List<PeopleManagerCount> emigrationCount(String start_time, String end_time) {
        ResponseEntity<PeopleManagerCount[]> responseEntity = restTemplate.getForEntity(
                "http://starepo/" + BigDataPath.STAREPO_COUNT_EMIGRATION
                        + "?start_time=" + start_time + "&end_time=" + end_time,
                PeopleManagerCount[].class
        );
        return Arrays.asList(responseEntity.getBody());
    }

    @SuppressWarnings("unused")
    private List<PeopleManagerCount> emigrationCountError(String start_time, String end_time) {
        log.error("Emigration count faild");
        return null;
    }

    @HystrixCommand(fallbackMethod = "clusteringCountError")
    private List<PeopleManagerCount> clusteringCount(String start_time, String end_time) {
        ResponseEntity<PeopleManagerCount[]> responseEntity = restTemplate.getForEntity(
                "http://clustering/" + BigDataPath.CLUSTERING_TOTLE
                        + "?start_time=" + start_time + "&end_time=" + end_time,
                PeopleManagerCount[].class);
        return Arrays.asList(responseEntity.getBody());
    }

    @SuppressWarnings("unused")
    private List<PeopleManagerCount> clusteringCountError(String start_time, String end_time) {
        log.error("Clustering count error");
        return null;
    }
}