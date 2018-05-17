package com.hzgc.service.face.service;

import com.hzgc.jni.FaceAttribute;
import com.hzgc.jni.FaceFunction;
import com.hzgc.jni.NativeFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FaceExtractService {

    private FaceExtractService() {
        try {
            log.info("Start NativeFunction init....");
            NativeFunction.init();
            log.info("Init NativeFunction successful!");
        } catch (Exception e) {
            log.error("Init NativeFunction failure!");
            e.printStackTrace();
        }
    }

    /**
     * 特征提取
     *
     * @param imageBytes 图片的字节数组
     * @return float[] 特征值:长度为512的float[]数组
     */
    public FaceAttribute featureExtract(byte[] imageBytes) {
        if (imageBytes != null && imageBytes.length > 0) {
            return FaceFunction.featureExtract(imageBytes);
        }
        return null;
    }
}
