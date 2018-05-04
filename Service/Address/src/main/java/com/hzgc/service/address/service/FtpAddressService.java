package com.hzgc.service.address.service;

import com.hzgc.common.ftp.properties.FTPAddressProperties;
import com.hzgc.common.util.empty.IsEmpty;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Properties;

@Service
public class FtpAddressService implements Serializable {

    private static Properties proper = FTPAddressProperties.getProps();

    /**
     * 获取Ftp相关配置参数
     *
     * @return ftp相关配置参数
     */
    public Properties getProperties() {
        return proper;
    }

    /**
     * 通过主机名获取FTP的IP地址
     *
     * @param hostname 主机名
     * @return IP地址
     */
    public String getIPAddress(String hostname) {
        String ftpIpAddress = "";
        if (IsEmpty.strIsRight(hostname)) {
            ftpIpAddress = proper.getProperty(hostname);
        }
        return ftpIpAddress;
    }
}

