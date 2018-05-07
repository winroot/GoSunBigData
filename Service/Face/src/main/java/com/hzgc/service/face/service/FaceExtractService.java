package com.hzgc.service.face.service;

import com.hzgc.common.jni.FaceAttribute;
import com.hzgc.common.jni.FaceFunction;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class FaceExtractService {

    private static Logger LOG = Logger.getLogger(FaceExtractService.class);

    private FaceExtractService() {
        try {
            LOG.info("Start NativeFunction init....");
            //NativeFunction.init();
            LOG.info("Init NativeFunction successful!");
        } catch (Exception e) {
            LOG.error("Init NativeFunction failure!");
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
