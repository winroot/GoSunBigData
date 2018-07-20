package com.hzgc.service.address.service;

import com.hzgc.common.collect.facedis.FtpRegisterClient;
import com.hzgc.common.collect.facedis.FtpRegisterInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Service
public class FtpAddressService implements Serializable {
    @Autowired
    @SuppressWarnings("unused")
    FtpRegisterClient register;

    /**
     * 获取Ftp相关配置参数
     *
     * @return ftp相关配置参数
     */
    public Map<String, String> getProperties() {
        Map<String, String> map = new HashMap<>();
        for (FtpRegisterInfo registerInfo : register.getFtpRegisterInfoList()) {
            map.put("ip", registerInfo.getFtpIPAddress());
            map.put("port", registerInfo.getFtpPort());
            map.put("username", registerInfo.getFtpAccountName());
            map.put("password", registerInfo.getFtpPassword());
            map.put("pathRule", registerInfo.getPathRule());
        }
        return map;
    }

    /**
     * 通过主机名获取FTP的IP地址
     *
     * @param hostname 主机名
     * @return IP地址
     */
    public String getIPAddress(String hostname) {
        for (FtpRegisterInfo registerInfo : register.getFtpRegisterInfoList()) {
            if (hostname.equals(registerInfo.getFtpHomeName())){
                return registerInfo.getFtpIPAddress();
            }
        }
        return null;
    }
}

