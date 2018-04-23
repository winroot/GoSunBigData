package com.hzgc.service.dynrepo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.PropertySource;

@EnableEurekaClient
@SpringBootApplication
@PropertySource("dynrepoApplication.properties")
public class DynRepoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DynRepoApplication.class, args);
    }
}
