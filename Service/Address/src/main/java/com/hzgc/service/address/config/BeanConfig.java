package com.hzgc.service.address.config;

import com.hzgc.collect.zk.register.RegisterWatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class BeanConfig {
    @Autowired
    private Environment environment;

    @Bean
    public RegisterWatcher registerWatcher() {
        return new RegisterWatcher(environment.getProperty("zk.address"));
    }
}

