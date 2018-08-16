package com.hzgc.service.starepo;

import com.hzgc.common.service.auth.config.EnableAuthSynchronize;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
@EnableAuthSynchronize
@MapperScan("com.hzgc.service.starepo.dao")
public class StaRepoApplication {
    public static void main(String[] args) {
        SpringApplication.run(StaRepoApplication.class, args);
    }

    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
