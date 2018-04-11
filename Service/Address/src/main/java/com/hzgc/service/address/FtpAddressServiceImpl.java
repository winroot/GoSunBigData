package com.hzgc.service.address;

import com.hzgc.common.file.ResourceFileUtil;
import com.hzgc.common.io.IOUtil;
import com.hzgc.dubbo.address.FtpAddressService;

import java.io.FileInputStream;
import java.io.Serializable;
import java.util.Properties;

public class FtpAddressServiceImpl implements FtpAddressService, Serializable {
    private static Properties proper = new Properties();

    public FtpAddressServiceImpl() {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(ResourceFileUtil.loadResourceFile("service_address.properties"));
            proper.load(fis);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtil.closeStream(fis);
        }
    }

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

