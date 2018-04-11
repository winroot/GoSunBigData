package com.hzgc.service.address;

import com.hzgc.common.ftp.properties.FTPAddressProperHelper;
import com.hzgc.dubbo.address.FtpAddressService;

import java.io.Serializable;
import java.util.Properties;

public class FtpAddressServiceImpl implements FtpAddressService, Serializable {
    private static Properties proper = FTPAddressProperHelper.getProps();

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
        if (hostname != null && hostname.length() > 0) {
            ftpIpAddress = proper.getProperty(hostname);
        }
        return ftpIpAddress;
    }
}

