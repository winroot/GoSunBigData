package com.hzgc.service.address.config;

import com.hzgc.common.collect.facedis.FtpRegisterClient;
import com.hzgc.common.collect.facesub.FtpSubscribeClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class BeanConfig {
    @Autowired
    private Environment environment;

    @Bean
    public FtpRegisterClient register() {
        return new FtpRegisterClient(environment.getProperty("zk.address"));
    }

    @Bean
    public FtpSubscribeClient subscribe() {
        return new FtpSubscribeClient(environment.getProperty("zk.address"));
    }
}

