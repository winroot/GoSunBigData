package com.hzgc.service.face.controller;

import com.hzgc.common.jni.FaceAttribute;
import com.hzgc.service.face.service.FaceExtractService;
import com.hzgc.service.util.response.ResponseResult;
import com.hzgc.service.util.rest.BigDataPath;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@FeignClient(name = "face")
@RequestMapping(value = BigDataPath.FACE)
@Api(value = "/face",tags = "特征提取" )
@Slf4j
public class FaceController {

    @Autowired
    FaceExtractService faceExtractService;

    //特征值获取
    @ApiOperation(value = "图片的特征值提取",response =FaceAttribute.class)
    @ApiImplicitParam(name = "image",value = "图片",required =  true,dataType = "file", paramType = "form")

    @ApiResponses(
            {@ApiResponse(code = 200,message = "successful response")})
    @RequestMapping(value = BigDataPath.FEATURE_EXTRACT,method = RequestMethod.POST)
    public ResponseResult<FaceAttribute> featureExtract(@ApiParam(name = "image",value = "图片",required = true) MultipartFile image) throws IOException {
        ResponseResult<FaceAttribute> response = ResponseResult.ok();
        byte[] pictureBody = null;
        FaceAttribute faceAttribute = null;

        //非空判断
        if(null != image){
            pictureBody = image.getBytes();
        }else {
            log.info("file is null or content is null");
        }
        //调用大数据接口，提取特征值
        try{
            faceAttribute = faceExtractService.featureExtract(pictureBody);
        }catch (Exception e){
            log.info("faceAttribute acquires is failed");
        }

        response.setBody(faceAttribute);
        return response;
    }
}
