package com.hzgc.collect.expand.util;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.Properties;

public class CollectProperties implements Serializable {

    private static Logger LOG = Logger.getLogger(CollectProperties.class);

    private static Properties props = new Properties();
    private static int receiveQueueCapacity;
    private static int receiveNumber;
    private static int faceDetectorNumber;
    private static String ftpType;
    private static String ftpIp;
    private static int ftpPort;
    private static String dataPorts;
    private static String ftpImplicitSsl;
    private static String zookeeperAddress;
    private static boolean ftpSubscribeSwitch;
    private static String kafkaFaceObjectTopic;
    private static String rocketmqAddress;
    private static String rocketmqCaptureTopic;
    private static String rokcetmqCaptureGroup;
    private static String hostname;
    private static String ftpVersion;
    private static String proxyIpAddress;
    private static String proxyPort;
    private static String registerPath;
    private static String ftpPathRule;
    private static String ftpAccount;
    private static String ftpPassword;
    private static String homeDirs;
    private static float diskUsageRate;
    private static long period;

    public static String getFtpAccount() {
        return ftpAccount;
    }

    private static void setFtpAccount(String ftpAccount) {
        CollectProperties.ftpAccount = ftpAccount;
    }

    public static String getFtpPassword() {
        return ftpPassword;
    }

    private static void setFtpPassword(String ftpPassword) {
        CollectProperties.ftpPassword = ftpPassword;
    }

    static {
        try {
            props.load(ClassLoader.getSystemResourceAsStream("collect.properties"));
            setReceiveQueueCapacity(Integer.parseInt(props.getProperty("receive.queue.capacity")));
            setReceiveNumber(Integer.parseInt(props.getProperty("receive.number")));
            setFaceDetectorNumber(Integer.parseInt(props.getProperty("face.detector.number")));
            setFtpType(props.getProperty("ftp.type"));
            setFtpIp(props.getProperty("ftp.ip"));
            setFtpPort(Integer.parseInt(props.getProperty("ftp.port")));
            setDataPorts(props.getProperty("data.ports"));
            setFtpImplicitSsl(props.getProperty("ftp.implicitSsl"));
            setZookeeperAddress(props.getProperty("zookeeper.address"));
            setFtpSubscribeSwitch(Boolean.parseBoolean(props.getProperty("ftp.subscribe.switch")));
            setKafkaFaceObjectTopic(props.getProperty("kafka.faceobject.topic"));
            setRocketmqAddress(props.getProperty("rocketmq.address"));
            setRocketmqCaptureTopic(props.getProperty("rocketmq.capture.topic"));
            setRokcetmqCaptureGroup(props.getProperty("rocketmq.capture.group"));
            setHostname(InetAddress.getLocalHost().getHostName());
            setFtpVersion(props.getProperty("ftp.version"));
            setProxyIpAddress(props.getProperty("proxy.ip"));
            setProxyPort(props.getProperty("proxy.port"));
            setRegisterPath(props.getProperty("zookeeper.register.path"));
            setFtpPathRule(props.getProperty("ftp.pathRule"));
            setFtpAccount(props.getProperty("ftp.account"));
            setFtpPassword(props.getProperty("ftp.password"));
            setHomeDirs(props.getProperty("homeDirs"));
            setDiskUsageRate(Float.parseFloat(props.getProperty("diskUsageRate")));
            setPeriod(Long.parseLong(props.getProperty("period")));
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("Catch an unknown error, can't load the configuration file collect.properties");
        }
    }

    public static String getFtpPathRule() {
        return ftpPathRule;
    }

    private static void setFtpPathRule(String ftpPathRule) {
        CollectProperties.ftpPathRule = ftpPathRule;
    }

    public static String getRegisterPath() {
        return registerPath;
    }

    private static void setRegisterPath(String registerPath) {
        CollectProperties.registerPath = registerPath;
    }

    public static String getProxyPort() {
        return proxyPort;
    }

    private static void setProxyPort(String proxyPort) {
        CollectProperties.proxyPort = proxyPort;
    }

    public static String getProxyIpAddress() {
        return proxyIpAddress;
    }

    public static Properties getProps() {
        return props;
    }

    public static boolean isFtpSubscribeSwitch() {
        return ftpSubscribeSwitch;
    }

    private static void setFtpSubscribeSwitch(boolean ftpSubscribeSwitch) {
        CollectProperties.ftpSubscribeSwitch = ftpSubscribeSwitch;
    }

    public static void setProps(Properties props) {
        CollectProperties.props = props;
    }

    public static String getFtpType() {
        return ftpType;
    }

    public static void setFtpType(String ftpType) {
        CollectProperties.ftpType = ftpType;
    }

    public static String getFtpIp() {
        return ftpIp;
    }

    private static void setFtpIp(String ftpIp) {
        CollectProperties.ftpIp = ftpIp;
    }

    static String getFtpVersion() {
        return ftpVersion;
    }

    private static void setFtpVersion(String ftpVersion) {
        CollectProperties.ftpVersion = ftpVersion;
    }

    public static String getHostname() {
        return hostname;
    }

    private static void setHostname(String hostname) {
        CollectProperties.hostname = hostname;
    }

    public static int getReceiveQueueCapacity() {
        return receiveQueueCapacity;
    }

    private static void setReceiveQueueCapacity(int receiveQueueCapacity) {
        CollectProperties.receiveQueueCapacity = receiveQueueCapacity;
    }

    public static int getReceiveNumber() {
        return receiveNumber;
    }

    private static void setReceiveNumber(int receiveNumber) {
        CollectProperties.receiveNumber = receiveNumber;
    }

    public static int getFaceDetectorNumber() {
        return faceDetectorNumber;
    }

    private static void setFaceDetectorNumber(int faceDetectorNumber) {
        CollectProperties.faceDetectorNumber = faceDetectorNumber;
    }

    public static int getFtpPort() {
        return ftpPort;
    }

    private static void setFtpPort(int ftpPort) {
        CollectProperties.ftpPort = ftpPort;
    }

    public static String getDataPorts() {
        return dataPorts;
    }

    private static void setDataPorts(String dataPorts) {
        CollectProperties.dataPorts = dataPorts;
    }

    public static String getFtpImplicitSsl() {
        return ftpImplicitSsl;
    }

    private static void setFtpImplicitSsl(String ftpImplicitSsl) {
        CollectProperties.ftpImplicitSsl = ftpImplicitSsl;
    }

    public static String getZookeeperAddress() {
        return zookeeperAddress;
    }

    private static void setZookeeperAddress(String zookeeperAddress) {
        CollectProperties.zookeeperAddress = zookeeperAddress;
    }

    public static String getKafkaFaceObjectTopic() {
        return kafkaFaceObjectTopic;
    }

    private static void setKafkaFaceObjectTopic(String kafkaFaceObjectTopic) {
        CollectProperties.kafkaFaceObjectTopic = kafkaFaceObjectTopic;
    }

    public static String getRocketmqAddress() {
        return rocketmqAddress;
    }

    private static void setRocketmqAddress(String rocketmqAddress) {
        CollectProperties.rocketmqAddress = rocketmqAddress;
    }

    public static String getRocketmqCaptureTopic() {
        return rocketmqCaptureTopic;
    }

    private static void setRocketmqCaptureTopic(String rocketmqCaptureTopic) {
        CollectProperties.rocketmqCaptureTopic = rocketmqCaptureTopic;
    }

    public static String getRokcetmqCaptureGroup() {
        return rokcetmqCaptureGroup;
    }

    private static void setRokcetmqCaptureGroup(String rokcetmqCaptureGroup) {
        CollectProperties.rokcetmqCaptureGroup = rokcetmqCaptureGroup;
    }

    private static void setProxyIpAddress(String proxyIpAddress) {
        CollectProperties.proxyIpAddress = proxyIpAddress;
    }

    public static String getHomeDirs() {
        return homeDirs;
    }

    private static void setHomeDirs(String homeDirs) {
        CollectProperties.homeDirs = homeDirs;
    }

    public static float getDiskUsageRate() {
        return diskUsageRate;
    }

    private static void setDiskUsageRate(float diskUsageRate) {
        CollectProperties.diskUsageRate = diskUsageRate;
    }

    public static long getPeriod() {
        return period;
    }

    public static void setPeriod(long period) {
        CollectProperties.period = period;
    }
}
