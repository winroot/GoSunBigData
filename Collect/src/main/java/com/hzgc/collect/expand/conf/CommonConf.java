package com.hzgc.collect.expand.conf;

import com.hzgc.collect.expand.util.FTPConstants;
import com.hzgc.collect.expand.util.CollectProperties;

import java.io.Serializable;

public class CommonConf implements Serializable {

    /**
     * 队列日志名称
     */
    private String logName = FTPConstants.LOG_NAME;

    /**
     * 日志文件大小
     */
    private int logSize = FTPConstants.LOG_SIZE_THREE_HUNDRED_THOUSAND;

    /**
     * 当前队列缓冲容量
     */
    private int capacity;

    /**
     * 接收队列日志目录
     */
    private String receiveLogDir;

    /**
     * 处理队列日志目录
     */
    private String processLogDir;

    /**
     * 接收队列个数
     */
    private int receiveNumber;

    /**
     * 备份日志目录
     * 用于存放已处理过的process和receive目录下的日志
     */
    private String successLogDir;

    /**
     * merge模块处理日志目录
     */
    private String mergeLogDir;

    /**
     * merge模块扫描时间
     */
    private int mergeScanTime;

    /**
     * 默认加载类路径下的cluster-over-ftp.properties文件
     */
    public CommonConf() {
        //HelperFactory.regist();
        this.logSize = CollectProperties.getLogSize();
        this.capacity = CollectProperties.getReceiveQueueCapacity();
        this.receiveLogDir = CollectProperties.getReceiveLogDir();
        this.processLogDir = CollectProperties.getProcessLogDir();
        this.receiveNumber = CollectProperties.getReceiveNumber();
        this.successLogDir = CollectProperties.getSuccessLogDir();
        this.mergeLogDir = CollectProperties.getMergeLogDir();
        this.mergeScanTime = CollectProperties.getMergeScanTime();
    }

    /**
     * @param properName 指定类路径下资源文件名称
     */
    public CommonConf(String properName) {
    }

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public int getLogSize() {
        return logSize;
    }

    public void setLogSize(int logSize) {
        this.logSize = logSize;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getReceiveLogDir() {
        return receiveLogDir;
    }

    public void setReceiveLogDir(String receiveLogDir) {
        this.receiveLogDir = receiveLogDir;
    }

    public String getProcessLogDir() {
        return processLogDir;
    }

    public void setProcessLogDir(String processLogDir) {
        this.processLogDir = processLogDir;
    }

    public String getMergeLogDir() {
        return mergeLogDir;
    }

    public void setSuccessLogDir(String successLogDir) {
        this.successLogDir = successLogDir;
    }

    public String getSuccessLogDir() {
        return successLogDir;
    }

    public void setMergeLogDir(String mergeLogDir) {
        this.mergeLogDir = mergeLogDir;
    }

    public int getReceiveNumber() {
        return receiveNumber;
    }

    public void setReceiveNumber(int receiveNumber) {
        this.receiveNumber = receiveNumber;
    }

    public int getMergeScanTime() {
        return mergeScanTime;
    }

    public void setMergeScanTime(int mergeScanTime) {
        this.mergeScanTime = mergeScanTime;
    }
}
