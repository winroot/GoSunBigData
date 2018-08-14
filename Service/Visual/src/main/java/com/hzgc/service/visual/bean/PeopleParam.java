package com.hzgc.service.visual.bean;

import java.io.Serializable;
import java.util.List;

public class PeopleParam implements Serializable {

    private List<String> objectTypeKeyList;
    private String offTime;

    public List<String> getObjectTypeKeyList() {
        return objectTypeKeyList;
    }

    public void setObjectTypeKeyList(List<String> objectTypeKeyList) {
        this.objectTypeKeyList = objectTypeKeyList;
    }

    public String getOffTime() {
        return offTime;
    }

    public void setOffTime(String offTime) {
        this.offTime = offTime;
    }
}
