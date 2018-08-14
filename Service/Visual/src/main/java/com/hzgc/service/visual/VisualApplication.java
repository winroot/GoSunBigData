package com.hzgc.service.visual;

import com.hzgc.common.service.api.config.EnableDeviceQueryService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

@EnableEurekaClient
@SpringBootApplication
@EnableHystrix
@EnableDeviceQueryService
public class VisualApplication {
    public static void main(String[] args) {
        SpringApplication.run(VisualApplication.class, args);
    }
}

