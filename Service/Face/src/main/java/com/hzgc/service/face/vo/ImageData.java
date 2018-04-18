package com.hzgc.service.face.vo;

import com.hzgc.jni.FaceAttribute;

import java.io.Serializable;

public class ImageData  implements Serializable{

    private byte[] binImage;    //图片二进制数据
    private FaceAttribute faceAttr;    //人脸特征对象,包括特征值和人脸属性

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
