package com.hzgc.cluster.spark.util;

import com.google.gson.Gson;
import com.hzgc.cluster.spark.consumer.FaceObject;

public class FaceObjectUtil {
    private static Gson gson;
    static {
        gson = new Gson();
    }
    public static FaceObject jsonToObject(String json) {
        return gson.fromJson(json, FaceObject.class);
    }
}
