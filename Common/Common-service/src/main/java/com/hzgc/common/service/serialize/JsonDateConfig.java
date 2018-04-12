package com.hzgc.common.service.serialize;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import java.text.SimpleDateFormat;

/**
 * 用于配置 dubbox 中 date json 序列化时的格式
 * <p>创建时间：2017/5/8 15:42</p>
 *
 * @author 娄存银
 * @version 1.0
 */
@Provider
public class JsonDateConfig implements ContextResolver<ObjectMapper> {
    private static final String FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private final ObjectMapper mapper;

    public JsonDateConfig() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        // 设置日期的格式
        mapper.setDateFormat(new SimpleDateFormat(FORMAT_PATTERN));
        // 设置为空不输出到 json
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 反序列化时，如果没有相关字段，不报错
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    }

    @Override
    public ObjectMapper getContext(Class<?> aClass) {
        return mapper;
    }
}
