package com.hzgc.service.starepo.bean.prison;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PrisonCountResults implements Serializable{

    private List<PrisonCountResult> results;
}
