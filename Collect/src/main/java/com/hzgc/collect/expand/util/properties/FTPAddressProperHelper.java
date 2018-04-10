package com.hzgc.collect.expand.util.properties;

import com.hzgc.common.file.ResourceFileUtil;
import com.hzgc.common.properties.ProperHelper;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

public class FTPAddressProperHelper extends ProperHelper implements Serializable {

    private static Logger LOG = Logger.getLogger(FTPAddressProperHelper.class);
    private static Properties props = new Properties();

    private static String ip;
    private static String port;
    private static String user;
    private static String password;
    private static String pathRule;
    private static String hostname;

    static {
        String properName = "ftpAddress.properties";
        FileInputStream in = null;
        try {
            File file = ResourceFileUtil.loadResourceFile(properName);
            in = new FileInputStream(file);
            props.load(in);
            setIp();
            setPort();
            setUser();
            setPassword();
            setPathRule();
            setHostname();
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("Catch an unknown error, can't load the configuration file" + properName);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * set方法。验证配置文件中的值是否为符合条件的格式。
     */
    private static void setIp() {
        ip = verifyIp("ip", props, LOG);
    }

    private static void setPort() {
        port = verifyPort("port", "2121", props, LOG);
    }

    private static void setUser() {
        user = verifyCommonValue("user", "admin", props, LOG);
    }

    private static void setPassword() {
        password = verifyCommonValue("password", "123456", props, LOG);
    }

    private static void setPathRule() {
        pathRule = verifyCommonValue("pathRule", "%f/%Y/%m/%d/%H", props, LOG);
    }

    private static void setHostname() {
        hostname = verifyCommonValue("hostname", "", props, LOG);
    }

    /**
     * get方法。提供获取配置文件中的值的方法。
     */

    public static String getIp() {
        return ip;
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

    public static String getHostname() {
        return hostname;
    }

    /**
     * 获取Properties属性的资源文件变量
     */
    public static Properties getProps() {
        return props;
    }

}
