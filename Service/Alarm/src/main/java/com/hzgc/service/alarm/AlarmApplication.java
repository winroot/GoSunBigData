package com.hzgc.service.alarm;

import com.hzgc.service.util.api.config.EnableDeviceQueryService;
import com.hzgc.service.util.auth.config.EnableAuthSynchronize;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableEurekaClient
@EnableSwagger2
@EnableAuthSynchronize
public class AlarmApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlarmApplication.class,args);
    }
}
