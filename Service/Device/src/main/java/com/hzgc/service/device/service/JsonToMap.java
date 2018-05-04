package com.hzgc.service.device.service;

import com.alibaba.fastjson.JSON;

import java.util.Map;

public class JsonToMap<T> {

    //String转换成Map
    public static<T> Map<T,Map<String,Integer>> stringToMap(String jsonString){
        Map<T,Map<String,Integer>> map = JSON.parseObject(jsonString,Map.class);
        for (T t:map.keySet()) {
            String str = JSON.toJSONString(map.get(t));
            Map<String,Integer> map1 = JSON.parseObject(str,Map.class);
            map.put(t,map1);
        }
        return map;
    }
}
