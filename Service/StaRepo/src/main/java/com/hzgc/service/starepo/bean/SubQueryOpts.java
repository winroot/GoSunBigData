package com.hzgc.service.starepo.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 子查询Id,以及子查询中按照对象类型分类的对象类型类表
 */
@ApiModel(value = "子查询Id,以及子查询中按照对象类型分类的对象类型类表")
public class SubQueryOpts implements Serializable {

    /**
     * 子查询ID
     */
    @ApiModelProperty(value = "子查询ID")
    private String queryId;

    /**
     * 对象类型列表，用于按照对象类型分类
     */
    @ApiModelProperty(value = "对象类型列表，用于按照对象类型分类")
    private List<String> pkeys;

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public List<String> getPkeys() {
        return pkeys;
    }

    public void setPkeys(List<String> pkeys) {
        this.pkeys = pkeys;
    }

    @Override
    public String toString() {
        return "SubQueryOpts{" +
                "queryId='" + queryId + '\'' +
                ", pkeys=" + pkeys +
                '}';
    }
}
