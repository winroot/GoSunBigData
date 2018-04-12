package com.hzgc.common.service.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * json序列化/反序列化工具类
 *
 * @author liuzhikun
 */
public class JsonUtils {
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    static {
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        OBJECT_MAPPER.setDateFormat(new SimpleDateFormat(FORMAT_PATTERN));
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private JsonUtils() {
    }

    public static ObjectMapper getInstance() {
        return OBJECT_MAPPER;
    }

    /**
     * javaBean,list,array convert to json string
     */
    public static String obj2json(Object obj) throws Exception {
        return OBJECT_MAPPER.writeValueAsString(obj);
    }

    /**
     * json string convert to javaBean
     */
    public static <T> T json2pojo(String jsonStr, Class<T> clazz) throws Exception {
        return OBJECT_MAPPER.readValue(jsonStr, clazz);
    }

    /**
     * json string convert to map
     */
    @SuppressWarnings("unchecked")
    public static <T> Map<String, Object> json2map(String jsonStr) throws Exception {
        return OBJECT_MAPPER.readValue(jsonStr, Map.class);
    }

    /**
     * json string convert to map with javaBean
     */
    public static <T> Map<String, T> json2map(String jsonStr, Class<T> clazz) throws Exception {
        Map<String, Map<String, Object>> map = OBJECT_MAPPER.readValue(jsonStr, new TypeReference<Map<String, T>>() {
        });
        Map<String, T> result = new HashMap<String, T>();
        for (Entry<String, Map<String, Object>> entry : map.entrySet()) {
            result.put(entry.getKey(), map2pojo(entry.getValue(), clazz));
        }
        return result;
    }

    /**
     * json array string convert to list with javaBean
     */
    public static <T> List<T> json2list(String jsonArrayStr, Class<T> clazz) throws Exception {
        List<Map<String, Object>> list = OBJECT_MAPPER.readValue(jsonArrayStr, new TypeReference<List<T>>() {
        });
        List<T> result = new ArrayList<T>();
        for (Map<String, Object> map : list) {
            result.add(map2pojo(map, clazz));
        }
        return result;
    }

    /**
     * map convert to javaBean
     */
    public static <T> T map2pojo(Map<?, ?> map, Class<T> clazz) {
        return OBJECT_MAPPER.convertValue(map, clazz);
    }
}