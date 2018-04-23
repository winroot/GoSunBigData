package com.hzgc.service.device;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableEurekaClient
public class DeviceApplication {
    public static void main(String [] args){
        SpringApplication.run(DeviceApplication.class,args);
    }
}
