package com.hzgc.service.dispatch;

import com.hzgc.service.util.api.config.EnableDeviceQueryService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

@SpringBootApplication
@EnableEurekaClient
@EnableHystrix
@EnableDeviceQueryService
public class DispatchApplication {

    public static void main(String [] args){
        SpringApplication.run(DispatchApplication.class,args);
    }
}
