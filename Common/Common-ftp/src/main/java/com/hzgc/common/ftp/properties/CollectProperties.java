package com.hzgc.common.ftp.properties;

import com.hzgc.common.ftp.Sharpness;
import com.hzgc.common.util.file.ResourceFileUtil;
import com.hzgc.common.util.io.IOUtil;
import com.hzgc.common.util.properties.ProperHelper;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

public class CollectProperties extends ProperHelper implements Serializable {

    private static Logger LOG = Logger.getLogger(CollectProperties.class);

    private static Properties props = new Properties();
    private static String logSize;
    private static String receiveQueueCapacity;
    private static String receiveLogDir;
    private static String processLogDir;
    private static String successLogDir;
    private static String mergeLogDir;
    private static String receiveNumber;
    private static String mergeScanTime;
    private static String faceDetectorNumber;
    private static String port;
    private static String dataPorts;
    private static String implicitSsl;
    private static Sharpness sharpness;
    private static String zookeeperSessionTimeout;
    private static String zookeeperAddress;
    private static String zookeeperPathSubscribe;
    private static String zookeeperWatcher;
    private static String ftpSwitch;

    static {
        String properName = "collect.properties";
        FileInputStream in = null;
        try {
            File file = ResourceFileUtil.loadResourceFile(properName);
            in = new FileInputStream(file);
            props.load(in);
            setLogSize();
            setReceiveQueueCapacity();
            setReceiveLogDir();
            setProcessLogDir();
            setSuccessLogDir();
            setMergeLogDir();
            setReceiveNumber();
            setMergeScanTime();
            setFaceDetectorNumber();
            setPort();
            setDataPorts();
            setImplicitSsl();
            setSharpness();
            setZookeeperSessionTimeout();
            setZookeeperAddress();
            setZookeeperPathSubscribe();
            setZookeeperWatcher();
            setFtpSwitch();
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
    private static void setSharpness() {
        String str = props.getProperty("sharpness");
        String[] strArr = str.split(":");
        sharpness = new Sharpness();
        try {
            sharpness.setWeight(Integer.parseInt(strArr[0]));
            sharpness.setWeight(Integer.parseInt(strArr[1]));
        } catch (Exception e) {
            LOG.error("Sharpness value is error, please check");
            e.printStackTrace();
            System.exit(1);
        }

    }

    private static void setPort() {
        port = verifyPort("listener-port", "2121", props, LOG);
    }

    private static void setDataPorts() {
        dataPorts = verifyCommonValue("data-ports", "2223-2225", props, LOG);
    }

    private static void setImplicitSsl() {
        implicitSsl = verifyBooleanValue("implicitSsl", "false", props, LOG);
    }

    private static void setReceiveQueueCapacity() {
        receiveQueueCapacity = verifyPositiveIntegerValue("receive.queue.capacity", String.valueOf(Integer.MAX_VALUE), props, LOG);
    }

    private static void setReceiveLogDir() {
        receiveLogDir = verifyCommonValue("receive.log.dir", "/opt/RealTimeFaceCompare/ftp/data/receive", props, LOG);
    }

    private static void setProcessLogDir() {
        processLogDir = verifyCommonValue("process.log.dir", "/opt/RealTimeFaceCompare/ftp/data/process", props, LOG);
    }

    private static void setSuccessLogDir() {
        successLogDir = verifyCommonValue("success.log.dir", "/opt/RealTimeFaceCompare/ftp/success", props, LOG);
    }

    private static void setMergeLogDir() {
        mergeLogDir = verifyCommonValue("merge.log.dir", "/opt/RealTimeFaceCompare/ftp/merge", props, LOG);
    }

    private static void setMergeScanTime() {
        mergeScanTime = verifyPositiveIntegerValue("merge.scan.time", "", props, LOG);
    }

    private static void setReceiveNumber() {
        receiveNumber = verifyCommonValue("receive.number", "6", props, LOG);
    }

    private static void setLogSize() {
        logSize = verifyPositiveIntegerValue("log.Size", "300000", props, LOG);
    }

    private static void setFaceDetectorNumber() {
        faceDetectorNumber = verifyPositiveIntegerValue("face.detector.number", "", props, LOG);
    }

    private static void setZookeeperSessionTimeout() {
        zookeeperSessionTimeout = verifyPositiveIntegerValue("zk.session.timeout", "6000", props, LOG);
    }

    private static void setZookeeperAddress() {
        zookeeperAddress = verifyIpPlusPortList("zookeeper.address", props, LOG);
    }

    private static void setZookeeperPathSubscribe() {
        zookeeperPathSubscribe = verifyCommonValue("zk.path.subscribe", "/ftp_subscribe", props, LOG);;
    }

    private static void setZookeeperWatcher() {
        zookeeperWatcher = verifyBooleanValue("zk.watcher", "true", props, LOG);;
    }

    private static void setFtpSwitch() {
        ftpSwitch = verifyBooleanValue("ftp.switch", "true", props, LOG);
    }

    /**
     * get方法。提供获取配置文件中的值的方法。
     */

    public static Integer getPort() {
        return Integer.valueOf(port);
    }

    public static String getDataPorts() {
        return dataPorts;
    }

    public static Boolean getImplicitSsl() {
        return Boolean.valueOf(implicitSsl);
    }

    public static Integer getReceiveQueueCapacity() {
        return Integer.valueOf(receiveQueueCapacity);
    }

    public static String getReceiveLogDir() {
        return receiveLogDir;
    }

    public static String getProcessLogDir() {
        return processLogDir;
    }

    public static Integer getReceiveNumber() {
        return Integer.valueOf(receiveNumber);
    }

    public static String getSuccessLogDir() {
        return successLogDir;
    }

    public static String getMergeLogDir() {
        return mergeLogDir;
    }

    public static Integer getMergeScanTime() {
        return Integer.valueOf(mergeScanTime);
    }

    public static Integer getLogSize() {
        return Integer.valueOf(logSize);
    }

    public static Integer getFaceDetectorNumber() {
        return Integer.valueOf(faceDetectorNumber);
    }

    public static Sharpness getSharpness() {
        return sharpness;
    }

    public static String getZookeeperSessionTimeout() {
        return zookeeperSessionTimeout;
    }

    public static String getZookeeperAddress() {
        return zookeeperAddress;
    }

    public static String getZookeeperPathSubscribe() {
        return zookeeperPathSubscribe;
    }

    public static String getZookeeperWatcher() {
        return zookeeperWatcher;
    }

    public static String getFtpSwitch() {
        return ftpSwitch;
    }

    /**
     * 获取Properties属性的资源文件变量
     */
    public static Properties getProps() {
        return props;
    }
}
