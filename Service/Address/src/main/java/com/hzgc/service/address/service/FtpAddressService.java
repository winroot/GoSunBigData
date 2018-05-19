package com.hzgc.service.address.service;

import com.hzgc.common.util.empty.IsEmpty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Service
public class FtpAddressService implements Serializable {

    /**
     * ftp 相关配置
     */
    @Value("${ftp.proxy.ip}")
    private String ftp_proxy_ip;
    @Value("${ftp.proxy.hostname}")
    private String ftp_proxy_hostname;
    @Value("${ftp.proxy.port}")
    private String ftp_proxy_port;
    @Value("${ftp.username}")
    private String ftp_username;
    @Value("${ftp.password}")
    private String ftp_password;
    @Value("${ftp.pathRule}")
    private String ftp_pathRule;
    @Value("${ftp.hostname.mapping}")
    private String ftp_hostname_mapping;

    /**
     * 获取Ftp相关配置参数
     *
     * @return ftp相关配置参数
     */
    public Map<String, String> getProperties() {
        Map<String, String> map = new HashMap<>();
        map.put("ip",ftp_proxy_ip);
        map.put("hostname", ftp_proxy_hostname);
        map.put("port", ftp_proxy_port);
        map.put("username", ftp_username);
        map.put("password", ftp_password);
        map.put("pathRule", ftp_pathRule);
        map.put("hostname.mapping", ftp_hostname_mapping);
        return map;
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
            String[] ftp_hostname = ftp_hostname_mapping.split(";");
            for (String str : ftp_hostname) {
                if (str.contains(hostname)) {
                    ftpIpAddress = str.split(":")[1];
                    if (IsEmpty.strIsRight(ftpIpAddress)){
                        return ftpIpAddress;
                    }
                }
            }
        }
        return ftpIpAddress;
    }
}

