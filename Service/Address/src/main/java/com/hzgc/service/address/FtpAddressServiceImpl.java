package com.hzgc.service.address;

import com.hzgc.common.ftp.properties.FTPAddressProperties;
import com.hzgc.common.util.empty.IsEmpty;

import java.io.Serializable;
import java.util.Properties;

public class FtpAddressServiceImpl implements FtpAddressService, Serializable {

    private static Properties proper = FTPAddressProperties.getProps();

    @Override
    public Properties getFtpAddress() {
        return proper;
    }

    /**
     * 通过主机名获取FTP的IP地址
     *
     * @param hostname 主机名
     * @return IP地址
     */
    @Override
    public String getIPAddress(String hostname) {
        String ftpIpAddress = "";
        if (IsEmpty.strIsRight(hostname)) {
            ftpIpAddress = proper.getProperty(hostname);
        }
        return ftpIpAddress;
    }
}

