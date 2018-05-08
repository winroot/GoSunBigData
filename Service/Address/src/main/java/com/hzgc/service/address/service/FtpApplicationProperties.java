package com.hzgc.service.address.service;

import com.hzgc.common.util.file.ResourceFileUtil;
import com.hzgc.common.util.io.IOUtil;
import com.hzgc.common.util.properties.ProperHelper;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

public class FtpApplicationProperties extends ProperHelper implements Serializable {

    private static Logger LOG = Logger.getLogger(FtpApplicationProperties.class);

    private static Properties props = new Properties();

    /**
     * ftp 相关配置
     */
    private static String ip;
    private static String hostname;
    private static String port;
    private static String user;
    private static String password;
    private static String pathRule;

    /**
     * ftp subscription 相关配置
     */
    private static String zk_session_timeout;
    private static String zk_address;
    private static String zk_path_subscribe;
    private static String zk_watcher;

    static {
        String properName = "ftpApplication.properties";
        FileInputStream in = null;
        try {
            File file = ResourceFileUtil.loadResourceFile(properName);
            in = new FileInputStream(file);
            props.load(in);
            setIp();
            setHostname();
            setPort();
            setUser();
            setPassword();
            setPathRule();
            setZk_session_timeout();
            setZk_address();
            setZk_path_subscribe();
            setZk_watcher();
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("Catch an unknown error, can't load the configuration file" + properName);
        } finally {
            IOUtil.closeStream(in);
        }
    }

    private static void setIp() {
        ip = ProperHelper.verifyIp("ip", props, LOG);
    }

    private static void setHostname() {
        hostname = ProperHelper.verifyCommonValue("hostname", "", props, LOG);
    }

    private static void setPort() {
        port = ProperHelper.verifyPort("port", "2121", props, LOG);
    }

    private static void setUser() {
        user = ProperHelper.verifyCommonValue("user", "admin", props, LOG);
    }

    private static void setPassword() {
        password = ProperHelper.verifyCommonValue("password", "123456", props, LOG);
    }

    private static void setPathRule() {
        pathRule = ProperHelper.verifyCommonValue("pathRule", "%f/%Y/%m/%d/%H", props, LOG);
    }

    private static void setZk_session_timeout() {
        zk_session_timeout = ProperHelper.verifyPositiveIntegerValue("zk.session.timeout", "6000", props, LOG);
    }

    private static void setZk_address() {
        zk_address = verifyIpPlusPortList("zk.address", props, LOG);
    }

    private static void setZk_path_subscribe() {
        zk_path_subscribe = verifyCommonValue("zk.path.subscribe", "/ftp_subscribe", props, LOG);;
    }

    private static void setZk_watcher() {
        zk_watcher = verifyBooleanValue("zk.watcher", "true", props, LOG);
    }

    public static String getIp() {
        return ip;
    }

    public static String getHostname() {
        return hostname;
    }

    public static Integer getPort() {
        return Integer.valueOf(port);
    }

    public static String getUser() {
        return user;
    }

    public static String getPassword() {
        return password;
    }

    public static String getPathRule() {
        return pathRule;
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

    public static Properties getProps() {
        return props;
    }

}
