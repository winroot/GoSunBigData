package com.hzgc.service.visual.service;

import com.hzgc.service.starepo.bean.export.EmigrationCount;
import com.hzgc.service.util.rest.BigDataPath;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@Slf4j
public class PeopleManagerService {
    @Autowired
    private RestTemplate restTemplate;

    public Object peopleCount(String start_time, String end_time) {
        List<EmigrationCount> emigrationCounts = emigrationCount(start_time, end_time);
        return null;
    }

    @HystrixCommand(fallbackMethod = "emigrationCountError")
    private List<EmigrationCount> emigrationCount(String start_time, String end_time) {
        Map<String, String> keyValue = new HashMap<>();
        keyValue.put("start_time", start_time);
        keyValue.put("end_time", end_time);
        EmigrationCount[] countArr = restTemplate.getForObject(
                "http://starepo/" + BigDataPath.STAREPO_COUNT_EMIGRATION,
                EmigrationCount[].class,
                keyValue
        );
        if (countArr != null) {
            return Arrays.asList(countArr);
        } else {
            return null;
        }
    }

    private List<EmigrationCount> emigrationCountError(String start_time, String end_time) {
        log.info("Emigration count faild");
        return null;
    }
}
