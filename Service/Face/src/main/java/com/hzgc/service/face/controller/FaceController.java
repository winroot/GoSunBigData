package com.hzgc.service.face.controller;

import com.hzgc.jni.FaceAttribute;
import com.hzgc.jni.PictureData;
import com.hzgc.service.face.service.FaceExtractService;
import com.hzgc.service.util.response.ResponseResult;
import com.hzgc.service.util.rest.BigDataPath;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(value = BigDataPath.FACE)
@Api(value = "/face",tags = "特征提取" )
@Slf4j
public class FaceController {

    @Autowired
    FaceExtractService faceExtractService;

    //特征值获取
    @ApiOperation(value = "图片的特征值提取",response = ResponseResult.class,responseContainer = "List")
    @ApiImplicitParam(name = "image",value = "图片",required =  true,dataType = "file", paramType = "form")

    @ApiResponses(
            {@ApiResponse(code = 200,message = "successful response")})
    @RequestMapping(value = BigDataPath.FEATURE_EXTRACT,method = RequestMethod.POST)
    public ResponseResult<PictureData> featureExtract(@ApiParam(name = "image",value = "图片",required = true) MultipartFile image) throws IOException {
        ResponseResult<PictureData> response = ResponseResult.ok();
        PictureData pictureData = new PictureData();
        byte[] pictureBody = null;
        FaceAttribute faceAttribute = null;

        //非空判断
        if(null != image){
            pictureBody = image.getBytes();
            pictureData.setImageData(pictureBody);
        }else {
            log.info("image is null");
            return null;
        }
        //调用大数据接口，提取特征值
        try{
            faceAttribute = faceExtractService.featureExtract(pictureBody);
            if (null != faceAttribute){
                pictureData.setFeature(faceAttribute);
            }else {
            log.info("faceAttribute is null");
            }
        }catch (Exception e){
            log.info("faceAttribute acquires is failed");
            e.printStackTrace();
        }
        response.setBody(pictureData);
        log.info("特征值提取成功");
        return response;
    }
}
