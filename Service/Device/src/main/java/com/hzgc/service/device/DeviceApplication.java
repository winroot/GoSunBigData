package com.hzgc.service.device;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@EnableEurekaClient
@PropertySource("deviceApplication.properties")
public class DeviceApplication {

    public static void main(String [] args){
        SpringApplication.run(DeviceApplication.class,args);
    }
}
