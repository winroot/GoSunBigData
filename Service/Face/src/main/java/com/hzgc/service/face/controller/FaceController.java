package com.hzgc.service.face.controller;

import com.hzgc.common.service.BigDataPath;
import com.hzgc.common.service.ResponseResult;
import com.hzgc.service.face.service.FaceExtract;
import com.hzgc.jni.FaceAttribute;
import com.hzgc.service.face.service.FaceExtractImpl;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@FeignClient(name = "face")
@RequestMapping(value = BigDataPath.FACE)
@Api(value = "/face",tags = "特征提取" )
public class FaceController {

    private static Logger logger = LoggerFactory.getLogger(FaceController.class);

    @Autowired
    FaceExtractImpl faceExtractService;

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
            logger.info("file is null or content is null");
        }
        //调用大数据接口，提取特征值
        try{
            faceAttribute = faceExtractService.featureExtract(pictureBody);
        }catch (Exception e){
            logger.info("faceAttribute acquires is failed");
        }

        response.setBody(faceAttribute);
        return response;
    }
}
