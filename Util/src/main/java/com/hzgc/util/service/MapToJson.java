package com.hzgc.util.service;

import com.google.gson.Gson;

import java.util.Map;


public class MapToJson {
    public static String mapToJson(Map<String, Object> map) {
        Gson gson = new Gson();
        return gson.toJson(map);
    }
}
