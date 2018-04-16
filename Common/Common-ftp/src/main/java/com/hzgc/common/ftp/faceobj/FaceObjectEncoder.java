package com.hzgc.common.ftp.faceobj;

import com.hzgc.common.util.object.ObjectUtil;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class FaceObjectEncoder implements Serializer<FaceObject> {

    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public byte[] serialize(String s, FaceObject faceObject) {
        return ObjectUtil.objectToByte(faceObject);
    }

    @Override
    public void close() {

    }
}
