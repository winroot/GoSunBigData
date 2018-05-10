package com.hzgc.collect.expand.subscribe;

import com.hzgc.collect.expand.util.FtpServerProperties;

import java.io.Serializable;

/**
 * FTP接收处理数据总开关
 */
public class FtpSwitch implements Serializable{

    private static boolean ftpSwitch;

    public FtpSwitch(){
        ftpSwitch = Boolean.parseBoolean(FtpServerProperties.getFtp_switch());
    }

    public static boolean isFtpSwitch() {
        return ftpSwitch;
    }
}
