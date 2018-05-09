package com.hzgc.service.clustering.bean;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Arrays;

@Data
@ToString
public class SortParam implements Serializable {
    //排序参数名称
    private String[] sortNameArr;

    //排序方式（升序or降序）
    private boolean[] isAscArr;
}
