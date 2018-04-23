package com.hzgc.service.starepo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class StaRepoApplication {

    public static void main(String[] args) {
        SpringApplication.run(StaRepoApplication.class, args);
    }
}
