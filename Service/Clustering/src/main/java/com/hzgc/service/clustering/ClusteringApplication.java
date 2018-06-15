package com.hzgc.service.clustering;

import com.hzgc.service.util.auth.config.EnableAuthSynchronize;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@EnableEurekaClient
@SpringBootApplication
@EnableAuthSynchronize
public class ClusteringApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClusteringApplication.class, args);
    }
}
