package com.hzgc.service.dynrepo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class DynRepoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DynRepoApplication.class, args);
    }
}
