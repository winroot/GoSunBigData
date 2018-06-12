package com.hzgc.service.util.journal.dto;

/**
 * @author liuzhikun
 * @date 2018/05/03
 */
public class OperateLogDTO {
    private String operator = "";

    private String operTime = "";

    private String ipAddress = "";

    private byte serviceType;

    private byte operType;

    private String description = "";

    private boolean success = true;

    private String failureCause = "";

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperTime() {
        return operTime;
    }

    public void setOperTime(String operTime) {
        this.operTime = operTime;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public byte getServiceType() {
        return serviceType;
    }

    public void setServiceType(byte serviceType) {
        this.serviceType = serviceType;
    }

    public byte getOperType() {
        return operType;
    }

    public void setOperType(byte operType) {
        this.operType = operType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getFailureCause() {
        return failureCause;
    }

    public void setFailureCause(String failureCause) {
        this.failureCause = failureCause;
    }

    @Override
    public String toString() {
        return "OperateLogDTO{" +
                "operator='" + operator + '\'' +
                ", operTime='" + operTime + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", serviceType=" + serviceType +
                ", operType=" + operType +
                ", description='" + description + '\'' +
                ", success=" + success +
                ", failureCause='" + failureCause + '\'' +
                '}';
    }
}
