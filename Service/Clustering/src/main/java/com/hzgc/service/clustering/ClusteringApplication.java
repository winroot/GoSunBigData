package com.hzgc.service.clustering;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class ClusteringApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClusteringApplication.class, args);
    }
}
