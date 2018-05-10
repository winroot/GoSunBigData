package com.hzgc.collect.expand.util;

import com.hzgc.collect.expand.processer.Sharpness;
import com.hzgc.common.util.file.ResourceFileUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

@Slf4j
public class FtpServerProperties implements Serializable {

    private static Properties props = new Properties();

    static {
        String properName = "ftpserver.properties";
        try {
            props.load(ResourceFileUtil.loadResourceInputStream(properName));

            // FTP 相关配置
            ftp_proxy_ip = props.getProperty("ftp.proxy.ip");
            ftp_proxy_hostname = props.getProperty("ftp.proxy.hostname");
            ftp_proxy_port = props.getProperty("ftp.proxy.port");
            ftp_username = props.getProperty("ftp.username");
            ftp_password = props.getProperty("ftp.password");
            ftp_pathRule = props.getProperty("ftp.pathRule");
            ftp_hostname_mapping = props.getProperty("ftp.hostname.mapping");

            // FTP Server 相关配置
            log_Size = props.getProperty("log.Size");
            receive_queue_capacity = props.getProperty("receive.queue.capacity");
            receive_log_dir = props.getProperty("receive.log.dir");
            process_log_dir = props.getProperty("process.log.dir");
            success_log_dir = props.getProperty("success.log.dir");
            merge_log_dir = props.getProperty("merge.log.dir");
            merge_scan_time = props.getProperty("merge.scan.time");
            receive_number = props.getProperty("receive.number");
            face_detector_number = props.getProperty("face.detector.number");
            listener_port = props.getProperty("listener.port");
            data_ports = props.getProperty("data.ports");
            implicitSsl = props.getProperty("implicitSsl");
            setSharpness();

            // FTP Subscription 相关配置
            zk_session_timeout = props.getProperty("zk.session.timeout");
            zk_address = props.getProperty("zk.address");
            zk_path_subscribe = props.getProperty("zk.path.subscribe");
            zk_watcher = props.getProperty("zk.watcher");
            ftp_switch = props.getProperty("ftp.switch");

            // RocketMQ Producer 相关配置
            rocketmq_address = props.getProperty("rocketmq.address");
            rocketmq_topic = props.getProperty("rocketmq.topic");
            rocketmq_group = props.getProperty("rocketmq.group");
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Can't load the configuration file" + properName);
        }
    }

    private static void setSharpness() {
        String str = props.getProperty("sharpness");
        String[] strArr = str.split(":");
        sharpness = new Sharpness();
        try {
            sharpness.setWeight(Integer.parseInt(strArr[0]));
            sharpness.setWeight(Integer.parseInt(strArr[1]));
        } catch (Exception e) {
            log.error("Sharpness value is error, please check");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * FTP 相关配置
     */
    private static String ftp_proxy_ip;
    private static String ftp_proxy_hostname;
    private static String ftp_proxy_port;
    private static String ftp_username;
    private static String ftp_password;
    private static String ftp_pathRule;
    private static String ftp_hostname_mapping;

    /**
     * FTP Server 相关配置
     */
    private static String log_Size;
    private static String receive_queue_capacity;
    private static String receive_log_dir;
    private static String process_log_dir;
    private static String success_log_dir;
    private static String merge_log_dir;
    private static String merge_scan_time;
    private static String receive_number;
    private static String face_detector_number;
    private static String listener_port;
    private static String data_ports;
    private static String implicitSsl;
    private static Sharpness sharpness;

    /**
     * FTP Subscription 相关配置
     */
    private static String zk_session_timeout;
    private static String zk_address;
    private static String zk_path_subscribe;
    private static String zk_watcher;
    private static String ftp_switch;

    /**
     * RocketMQ 相关配置
     */
    private static String rocketmq_address;
    private static String rocketmq_topic;
    private static String rocketmq_group;

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

    public static String getLog_Size() {
        return log_Size;
    }

    public static String getReceive_queue_capacity() {
        return receive_queue_capacity;
    }

    public static String getReceive_log_dir() {
        return receive_log_dir;
    }

    public static String getProcess_log_dir() {
        return process_log_dir;
    }

    public static String getSuccess_log_dir() {
        return success_log_dir;
    }

    public static String getMerge_log_dir() {
        return merge_log_dir;
    }

    public static String getMerge_scan_time() {
        return merge_scan_time;
    }

    public static String getReceive_number() {
        return receive_number;
    }

    public static String getFace_detector_number() {
        return face_detector_number;
    }

    public static String getListener_port() {
        return listener_port;
    }

    public static String getData_ports() {
        return data_ports;
    }

    public static String getImplicitSsl() {
        return implicitSsl;
    }

    public static Sharpness getSharpness() {
        return sharpness;
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

    public static String getFtp_switch() {
        return ftp_switch;
    }

    public static String getRocketmq_address() {
        return rocketmq_address;
    }

    public static String getRocketmq_topic() {
        return rocketmq_topic;
    }

    public static String getRocketmq_group() {
        return rocketmq_group;
    }
}
