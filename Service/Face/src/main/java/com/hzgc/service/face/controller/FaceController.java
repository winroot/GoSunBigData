package com.hzgc.service.face.controller;

import com.hzgc.common.service.BigDataPath;
import com.hzgc.common.service.ResponseResult;
import com.hzgc.service.face.service.FaceExtract;
import com.hzgc.jni.FaceAttribute;
import com.hzgc.service.face.vo.ImageDataVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
/*
    以图搜图
 */
@RestController
@FeignClient(name = "face")
@RequestMapping(value = BigDataPath.FACE)
public class FaceController {
    private static Logger logger = LoggerFactory.getLogger(FaceController.class);

    @Autowired
    FaceExtract faceExtract;

    //特征值获取
    @RequestMapping(value = BigDataPath.FEATURE_EXTRACT,method = RequestMethod.POST)
    public ResponseResult<ImageDataVo> featureExtract(MultipartFile image) throws IOException {
        ResponseResult<ImageDataVo> response = ResponseResult.ok();
        ImageDataVo imageData = new ImageDataVo();
        byte[] pictureBody = null;
        FaceAttribute faceAttribute = null;

        //非空判断
        if(null != image){
            pictureBody = image.getBytes();
        }else {
            logger.info("file is null or content is null");
        }
        //调用大数据接口，提取特征值
        try{
            faceAttribute = faceExtract.featureExtract(pictureBody);
        }catch (Exception e){
            logger.info("faceAttribute acquires is failed");
        }

        imageData.setBinImage(pictureBody);
        imageData.setFaceAttr(faceAttribute);

        response.setBody(imageData);

        return response;
    }
}
