package com.hzgc.service.dynrepo;

import com.hzgc.common.attribute.service.AttributeService;
import com.hzgc.service.util.auth.config.EnableAuthSynchronize;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@EnableEurekaClient
@SpringBootApplication
@EnableHystrix
@PropertySource("application.properties")
@ComponentScan(basePackages = {"com.hzgc.service.dynrepo", "com.hzgc.service.util"})
@EnableAuthSynchronize
public class DynRepoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DynRepoApplication.class, args);
    }
}
