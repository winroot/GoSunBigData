package com.hzgc.service.address;

import com.hzgc.service.util.auth.config.EnableAuthSynchronize;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableEurekaClient
@EnableAuthSynchronize
public class FtpApplication {

    public static void main(String[] args) {
        SpringApplication.run(FtpApplication.class, args);
    }
}
