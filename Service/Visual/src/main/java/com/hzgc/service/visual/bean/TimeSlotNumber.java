package com.hzgc.service.visual.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeSlotNumber implements Serializable {
    private Map<String, Integer> timeSlotNumber;
}
