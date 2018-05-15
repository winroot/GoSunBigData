package com.hzgc.service.dynrepo.bean;

import com.hzgc.jni.FaceAttribute;
import lombok.Data;

import java.io.Serializable;

@Data
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
}
