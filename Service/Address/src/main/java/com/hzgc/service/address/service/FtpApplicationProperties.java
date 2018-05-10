package com.hzgc.service.address.service;

import com.hzgc.common.util.file.ResourceFileUtil;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

public class FtpApplicationProperties implements Serializable {

    private static Properties props = new Properties();

    static {
        try {
            props.load(ResourceFileUtil.loadResourceInputStream("ftpApplication.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ftp 相关配置
     */
    @Value("${ftp.proxy.ip}")
    private static String ftp_proxy_ip;
    @Value("${ftp.proxy.hostname}")
    private static String ftp_proxy_hostname;
    @Value("${ftp.proxy.port}")
    private static String ftp_proxy_port;
    @Value("${ftp.username}")
    private static String ftp_username;
    @Value("${ftp.password}")
    private static String ftp_password;
    @Value("${ftp.pathRule}")
    private static String ftp_pathRule;
    @Value("${ftp.hostname.mapping}")
    private static String ftp_hostname_mapping;

    /**
     * ftp subscription 相关配置
     */
    @Value("${zk.session.timeout}")
    private static String zk_session_timeout;
    @Value("${zk.address}")
    private static String zk_address;
    @Value("${zk.path.subscribe}")
    private static String zk_path_subscribe;
    @Value("${zk.watcher}")
    private static String zk_watcher;

    public static Properties getProps() {
        return props;
    }

    public static String getFtp_proxy_ip() {
        return ftp_proxy_ip;
    }

    public static String getFtp_proxy_hostname() {
        return ftp_proxy_hostname;
    }

    public static String getFtp_proxy_port() {
        return ftp_proxy_port;
    }

    public static String getFtp_username() {
        return ftp_username;
    }

    public static String getFtp_password() {
        return ftp_password;
    }

    public static String getFtp_pathRule() {
        return ftp_pathRule;
    }

    public static String getFtp_hostname_mapping() {
        return ftp_hostname_mapping;
    }

    public static String getZk_session_timeout() {
        return zk_session_timeout;
    }

    public static String getZk_address() {
        return zk_address;
    }

    public static String getZk_path_subscribe() {
        return zk_path_subscribe;
    }

    public static String getZk_watcher() {
        return zk_watcher;
    }
}
