package com.hzgc.service.face.controller;

import com.hzgc.common.attribute.bean.Attribute;
import com.hzgc.common.attribute.service.AttributeService;
import com.hzgc.common.util.uuid.UuidUtil;
import com.hzgc.jni.FaceAttribute;
import com.hzgc.jni.PictureData;
import com.hzgc.service.face.bean.PictureUrl;
import com.hzgc.service.face.service.FaceExtractService;
import com.hzgc.service.face.util.FtpDownloadUtils;
import com.hzgc.service.util.error.RestErrorCode;
import com.hzgc.service.util.response.ResponseResult;
import com.hzgc.service.util.rest.BigDataPath;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@Api(value = "/face", tags = "特征提取")
@Slf4j
public class FaceController {

    @Autowired
    @SuppressWarnings("unused")
    private FaceExtractService faceExtractService;

    @SuppressWarnings("unused")
    private AttributeService attributeService = new AttributeService();

    //特征值获取
    @ApiOperation(value = "图片的特征值提取", response = ResponseResult.class, responseContainer = "List")
    @ApiImplicitParam(name = "image", value = "图片", required = true, dataType = "file", paramType = "form")
    @ApiResponses(
            {@ApiResponse(code = 200, message = "successful response")})
    @RequestMapping(value = BigDataPath.FEATURE_EXTRACT_BIN, method = RequestMethod.POST)
    public ResponseResult<PictureData> featureExtract(@ApiParam(name = "image", value = "图片", required = true) MultipartFile image) {
        PictureData pictureData = new PictureData();
        pictureData.setImageID(UuidUtil.getUuid());
        byte[] imageBin = null;
        if (image == null) {
            log.error("Start extract feature by binary, image is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
        }
        try {
            imageBin = image.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        pictureData.setFeature(faceExtractService.featureExtract(imageBin));
        log.info("faceAttribute acquires is successed");
        return ResponseResult.init(pictureData);
    }

    /**
     * 人/车属性查询
     *
     * @return List<Attribute>
     */
    @ApiOperation(value = "属性特征查询", response = Attribute.class, responseContainer = "List")
    @RequestMapping(value = BigDataPath.FACE_ATTRIBUTE, method = RequestMethod.GET)
    @SuppressWarnings("unused")
    public ResponseResult<List<Attribute>> getAttribute() {
        List<Attribute> attributeList;
        attributeList = attributeService.getAttribute();
        return ResponseResult.init(attributeList);
    }

    //URL提取特征值
    @ApiOperation(value = "根据url提取图片特征值", response = ResponseResult.class)
    @ApiResponses(
            {@ApiResponse(code = 200, message = "successful response")})
    @RequestMapping(value = "/feature_url", method = RequestMethod.POST)
    public ResponseResult<PictureData> getFeatureExtract(@RequestBody PictureUrl pictureUrl) {
        ResponseResult<PictureData> response = ResponseResult.ok();
        PictureData pictureData = new PictureData();
        FaceAttribute faceAttribute = null;
        if (null != pictureUrl) {
            //FTP匿名账号Anonymous和密码
            byte[] bytes = FtpDownloadUtils.downloadftpFile2Bytes(pictureUrl.getUrl(), "anonymous", null);
            if (null != bytes) {
                pictureData.setImageData(bytes);
                //调用大数据接口
                faceAttribute = faceExtractService.featureExtract(bytes);
                if (null != faceAttribute) {
                    pictureData.setFeature(faceAttribute);
                    pictureData.setImageID(UuidUtil.getUuid());
                    response.setBody(pictureData);
                    log.info("faceAttribute acquires is successed");
                    return response;
                }
            }
        }
        log.info("faceAttribute acquires is failed");
        return null;
    }
}
