package com.hzgc.service.face.service;

import com.hzgc.common.util.uuid.UuidUtil;
import com.hzgc.jni.FaceAttribute;
import com.hzgc.jni.FaceFunction;
import com.hzgc.jni.NativeFunction;
import com.hzgc.jni.PictureData;
import com.hzgc.service.face.util.FtpDownloadUtils;
import com.hzgc.service.util.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FaceExtractService {

    @Autowired
    FaceExtractService faceExtractService;

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
    public PictureData featureExtractByImage(byte[] imageBytes) {
        PictureData pictureData = new PictureData();
        pictureData.setImageID(UuidUtil.getUuid());
        pictureData.setImageData(imageBytes);
        FaceAttribute faceAttribute = FaceFunction.featureExtract(imageBytes);
        if (faceAttribute != null) {
            log.info("Face extract successful, image contains feature");
            pictureData.setFeature(faceAttribute);
            return pictureData;
        } else {
            log.info("Face extract failed, image not contains feature");
            return null;
        }
    }

    //ftp获取特征值
    public PictureData getFeatureExtractByFtp(String pictureUrl){
        PictureData pictureData;
        //FTP匿名账号Anonymous和密码
        byte[] bytes = FtpDownloadUtils.downloadftpFile2Bytes(pictureUrl,"anonymous",null);
        if (null != bytes){
            log.info("Face extract successful, pictureUrl contains feature");
            pictureData = faceExtractService.featureExtractByImage(bytes);
            return pictureData;
        }else {
            log.info("Face extract failed, pictureUrl not contains feature");
            return null;
        }
    }
}
