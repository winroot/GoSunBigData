package com.hzgc.cluster.warn;

import com.hzgc.common.util.file.ResourceFileUtil;
import com.hzgc.common.util.properties.ProperHelper;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

public class JDBCProperties extends ProperHelper implements Serializable {

    private static Logger LOG = Logger.getLogger(JDBCProperties.class);

    private static Properties props = new Properties();
    private static String hiveJDBCURL;
    private static String phoenixJDBCURL;

    static {
        String properName = "common_service_jdbc.properties";
        FileInputStream in = null;
        try {
            props.load(ResourceFileUtil.loadResourceInputStream(properName));
            setHiveJDBCURL();
            setPhoenixJDBCURL();
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("Catch an unknown error, can't load the configuration file" + properName);
        }
    }

    private static void setHiveJDBCURL() {
        JDBCProperties.hiveJDBCURL = verifyCommonValue("url",
                "jdbc:hive2://s103:2181,s104:2181,s108:2181/;serviceDiscoveryMode=zooKeeper;zooKeeperNamespace=thriftserver",
                props, LOG);
    }

    private static void setPhoenixJDBCURL() {
        JDBCProperties.phoenixJDBCURL = verifyCommonValue("phoenixJDBCURL", "jdbc:phoenix:host_replace:2181", props, LOG);
    }

    public static Properties getProps() {
        return props;
    }

    public static String getHiveJDBCURL() {
        return hiveJDBCURL;
    }

    public static String getPhoenixJDBCURL() {
        return phoenixJDBCURL;
    }
}