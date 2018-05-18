package com.hzgc.service.face;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.PropertySource;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableEurekaClient
@PropertySource("application.properties")
@EnableSwagger2
public class FaceApplication {

    public static void main(String[] args){
        SpringApplication.run(FaceApplication.class,args);
    }
}
