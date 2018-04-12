package com.hzgc.common.service.statistics;


/**
 * @author wangdl
 */
public class StatisticsBean {
    private String groupId;
    private String number = "0";
    private String confirm = "0";

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }
}
