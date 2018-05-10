package com.hzgc.collect.expand.util;

import lombok.Data;

import java.io.Serializable;

@Data
public class FtpUrlMessage extends FtpPathMessage implements Serializable {

    private String hostname;
    private String port;
    private String filePath;
}
