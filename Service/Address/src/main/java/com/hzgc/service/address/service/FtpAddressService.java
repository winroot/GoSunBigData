package com.hzgc.service.address.service;

import com.hzgc.collect.zk.register.RegisterWatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Service
public class FtpAddressService implements Serializable {
    @Autowired
    @SuppressWarnings("unused")
    RegisterWatcher registerWatcher;

    /**
     * 获取Ftp相关配置参数
     *
     * @return ftp相关配置参数
     */
    public Map<String, String> getProperties() {
        Map<String, String> map = new HashMap<>();
        for (String key : registerWatcher.getRegisterInfo().getProxyInfo().keySet()) {
            map.put("ip", key);
            map.put("port", registerWatcher.getRegisterInfo().getProxyInfo().get(key));
        }
        map.put("username", registerWatcher.getRegisterInfo().getFtpAccountName());
        map.put("password", registerWatcher.getRegisterInfo().getFtpPassword());
        map.put("pathRule", registerWatcher.getRegisterInfo().getPathRule());
        return map;
    }

    /**
     * 通过主机名获取FTP的IP地址
     *
     * @param hostname 主机名
     * @return IP地址
     */
    public String getIPAddress(String hostname) {
        return registerWatcher.getRegisterInfo().getHostNameMapping().get(hostname);
    }
}

