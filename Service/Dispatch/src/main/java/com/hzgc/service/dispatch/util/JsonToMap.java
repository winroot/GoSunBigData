package com.hzgc.service.dispatch.util;

import com.alibaba.fastjson.JSON;
import com.hzgc.common.util.json.JSONUtil;
import com.hzgc.common.util.object.ObjectUtil;
import com.hzgc.service.dispatch.bean.Dispatch;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class JsonToMap {

    //大数据jsonString转换成Map
    public static <T> Map<T, Map<String, String>> stringToMap(String jsonString) {
        Map<T, Map<String, String>> map = JSON.parseObject(jsonString, Map.class);
        for (T t : map.keySet()) {
            String str = JSON.toJSONString(map.get(t));
            Map<String, String> map1 = JSON.parseObject(str, Map.class);
            map.put(t, map1);
        }
        return map;
    }

    //平台jsonString转换成Map
    public static LinkedHashMap<String, Dispatch> dispatchStringToMap(String hbaseString) {
        LinkedHashMap<String, Dispatch> map = JSON.parseObject(hbaseString, LinkedHashMap.class);
        for (String s : map.keySet()) {
            String str = JSON.toJSONString(map.get(s));
            Dispatch dispatch = JSON.parseObject(str, Dispatch.class);
            map.put(s, dispatch);
        }
        return map;
    }
}
