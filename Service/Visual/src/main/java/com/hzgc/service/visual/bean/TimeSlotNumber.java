package com.hzgc.service.visual.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class TimeSlotNumber implements Serializable {
    private List<FaceDayStatistic> faceList;

    public TimeSlotNumber(){
        faceList = new ArrayList<>();
    }
}
