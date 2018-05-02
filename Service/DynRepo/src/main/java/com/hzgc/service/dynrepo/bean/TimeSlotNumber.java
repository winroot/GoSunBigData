package com.hzgc.service.dynrepo.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class TimeSlotNumber implements Serializable {
    private Map<String, Integer> timeSlotNumber;

    public TimeSlotNumber() {
        this.timeSlotNumber = new HashMap<>();
    }

    public Map<String, Integer> getTimeSlotNumber() {
        return timeSlotNumber;
    }

    public void setTimeSlotNumber(Map<String, Integer> timeSlotNumber) {
        this.timeSlotNumber = timeSlotNumber;
    }
}
