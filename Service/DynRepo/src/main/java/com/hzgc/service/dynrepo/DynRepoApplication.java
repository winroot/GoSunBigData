package com.hzgc.service.dynrepo;

import com.hzgc.common.service.api.config.EnableDeviceQueryService;
import com.hzgc.common.service.auth.config.EnableAuthSynchronize;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

@EnableEurekaClient
@SpringBootApplication
@EnableHystrix
@EnableAuthSynchronize
@EnableDeviceQueryService
public class DynRepoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DynRepoApplication.class, args);
    }
}
