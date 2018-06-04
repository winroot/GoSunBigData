package com.hzgc.service.visual;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@EnableEurekaClient
@SpringBootApplication
@ComponentScan(basePackages = {"com.hzgc.service.visual", "com.hzgc.service.util"})
public class VisualApplication {
    public static void main(String[] args) {
        SpringApplication.run(VisualApplication.class, args);
    }

    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

