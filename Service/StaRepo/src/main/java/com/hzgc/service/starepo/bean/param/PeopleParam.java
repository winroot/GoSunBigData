package com.hzgc.service.starepo.bean.param;

import java.io.Serializable;
import java.util.List;

public class PeopleParam implements Serializable {

    private List<String> objectTypeKeyList;
    private String offTime;
    private int start;
    private int limit;

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

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
