package com.hzgc.service.visual.bean;

import lombok.Data;

@Data
public class FaceDayStatistic{
        private String id;
        private Integer number;
        private String date;
        public FaceDayStatistic(String id, Integer number){
            this.id = id;
            this.number = number;
        }
    }