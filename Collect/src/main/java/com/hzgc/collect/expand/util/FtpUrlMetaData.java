package com.hzgc.collect.expand.util;

import com.hzgc.collect.expand.parser.FtpPathMetaData;

import java.io.Serializable;

public class FtpUrlMetaData extends FtpPathMetaData implements Serializable {

    private String hostname;
    private String port;
    private String filePath;

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
