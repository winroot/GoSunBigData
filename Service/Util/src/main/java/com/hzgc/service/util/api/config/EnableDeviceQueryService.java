package com.hzgc.service.util.api.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(DeviceQueryServiceConfig.class)
public @interface EnableDeviceQueryService {
}
