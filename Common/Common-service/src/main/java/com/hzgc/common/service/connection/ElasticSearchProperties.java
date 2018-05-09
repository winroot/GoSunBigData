package com.hzgc.common.service.connection;

import com.hzgc.common.util.file.ResourceFileUtil;
import com.hzgc.common.util.io.IOUtil;
import com.hzgc.common.util.properties.ProperHelper;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

public class ElasticSearchProperties extends ProperHelper implements Serializable {

    private static Logger LOG = Logger.getLogger(ElasticSearchProperties.class);

    private static Properties props = new Properties();
    private static String es_cluster_name;
    private static String es_hosts;
    private static String es_cluster_port;

    static {
        String properName = "common_service_es.properties";
        FileInputStream in = null;
        try {
            props.load(ResourceFileUtil.loadResourceInputStream(properName));
            setEs_cluster_name();
            setEs_hosts();
            setEs_cluster_port();
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("Catch an unknown error, can't load the configuration file" + properName);
        }
    }

    private static void setEs_cluster_name() {
        ElasticSearchProperties.es_cluster_name = verifyCommonValue("es.cluster.name", "hbase2es-cluster", props, LOG);
    }

    private static void setEs_hosts() {
        ElasticSearchProperties.es_hosts = verifyIp("es.hosts", props, LOG);
    }

    private static void setEs_cluster_port() {
        ElasticSearchProperties.es_cluster_port = verifyPort("es.cluster.port", "9300", props, LOG);
    }

    public static Properties getProps() {
        return props;
    }

    public static String getEs_cluster_name() {
        return es_cluster_name;
    }

    public static String getEs_hosts() {
        return es_hosts;
    }

    public static String getEs_cluster_port() {
        return es_cluster_port;
    }
}
