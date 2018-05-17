package com.hzgc.service.face.bean;

import com.hzgc.jni.FaceAttribute;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "图片信息")
public class PictureData {

    @ApiModelProperty(value = "图片原始数据")
    private byte[] imageData;
    @ApiModelProperty(value = "图片特征值")
    private FaceAttribute feature;

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public FaceAttribute getFeature() {
        return feature;
    }

    public void setFeature(FaceAttribute feature) {
        this.feature = feature;
    }
}
