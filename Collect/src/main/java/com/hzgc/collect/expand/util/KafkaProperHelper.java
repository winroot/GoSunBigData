package com.hzgc.collect.expand.util;

import com.hzgc.common.util.file.ResourceFileUtil;
import com.hzgc.common.util.io.IOUtil;
import com.hzgc.common.util.properties.ProperHelper;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

public class KafkaProperHelper extends ProperHelper implements Serializable {
    private static Logger LOG = Logger.getLogger(KafkaProperHelper.class);
    private static Properties props = new Properties();

    private static String bootstrapServers;
    private static String requestRequiredAcks;
    private static String retries;
    private static String keySerializer;
    private static String valueSerializer;
    private static String topicFeature;

    static {
        String properName = "kafka-producer.properties";
        FileInputStream in = null;
        try {
            File file = ResourceFileUtil.loadResourceFile(properName);
            in = new FileInputStream(file);
            props.load(in);
            setBootstrapServers();
            setRequestRequiredAcks();
            setRetries();
            setKeySerializer();
            setValueSerializer();
            setTopicFeature();
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("Catch an unknown error, can't load the configuration file" + properName);
        } finally {
            IOUtil.closeStream(in);
        }
    }

    /**
     * set方法。验证配置文件中的值是否为符合条件的格式。
     */
    private static void setBootstrapServers() {
        bootstrapServers = verifyIpPlusPortList("bootstrap.servers", props, LOG);
    }

    private static void setRequestRequiredAcks() {
        requestRequiredAcks = verifyIntegerValue("request.required.acks", "1", props, LOG);
    }

    private static void setRetries() {
        retries = verifyIntegerValue("retries", "0", props, LOG);
    }

    private static void setKeySerializer() {
        keySerializer = verifyCommonValue("key.serializer", "org.apache.kafka.common.serialization.StringSerializer", props, LOG);
    }

    private static void setValueSerializer() {
        valueSerializer = verifyCommonValue("value.serializer", "com.hzgc.ftpserver.producer.FaceObjectEncoder", props, LOG);
    }

    private static void setTopicFeature() {
        topicFeature = verifyCommonValue("topic-feature", "feature", props, LOG);
    }

    /**
     * get方法。提供获取配置文件中的值的方法。
     */
    public static String getBootstrapServers() {
        return bootstrapServers;
    }

    public static Integer getRequestRequiredAcks() {
        return Integer.valueOf(requestRequiredAcks);
    }

    public static Integer getRetries() {
        return Integer.valueOf(retries);
    }

    public static String getKeySerializer() {
        return keySerializer;
    }

    public static String getValueSerializer() {
        return valueSerializer;
    }

    public static String getTopicFeature() {
        return topicFeature;
    }

    /**
     * 获取Properties属性的资源文件变量
     */
    public static Properties getProps() {
        return props;
    }

}

