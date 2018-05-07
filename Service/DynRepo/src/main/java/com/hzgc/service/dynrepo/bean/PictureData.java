package com.hzgc.service.dynrepo.bean;

import com.hzgc.common.jni.FaceAttribute;

import java.io.Serializable;

public class PictureData implements Serializable {

    /**
     * 图片ID
     */
    private String imageID;

    /**
     * 图片二进制数据
     */
    private byte[] binImage;

    /**
     * 人脸特征对象,包括特征值和人脸属性
     */
    private FaceAttribute faceAttr;

    public String getImageID() {
        return imageID;
    }

    public void setImageID(String imageID) {
        this.imageID = imageID;
    }

    public byte[] getBinImage() {
        return binImage;
    }

    public void setBinImage(byte[] binImage) {
        this.binImage = binImage;
    }

    public FaceAttribute getFaceAttr() {
        return faceAttr;
    }

    public void setFaceAttr(FaceAttribute faceAttr) {
        this.faceAttr = faceAttr;
    }
}
