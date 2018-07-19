package com.hzgc.service.alarm.bean;

import lombok.Data;

@Data
public class AlarmData {
    private String smallPictureURL;
    private String bigPictureURL;
    private String staticId;
    private String lastAppearanceTime;
    private String objType;
    private String similarity;
}
