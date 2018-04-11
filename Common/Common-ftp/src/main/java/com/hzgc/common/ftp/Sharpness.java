package com.hzgc.common.ftp;

import java.io.Serializable;

public class Sharpness implements Serializable {
    //图片宽
    private int width;
    //图片高
    private int height;

    public int getWeight() {
        return width;
    }

    public void setWeight(int weight) {
        this.width = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
