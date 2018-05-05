package com.hzgc.service.starepo.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 按照对象类型分类，排序返回
 */
@ApiModel(value = "按照对象类型分类，排序返回")
public class GroupByPkey implements Serializable {

    /**
     * 对象类型Key
     */
    @ApiModelProperty(value = "对象类型Key")
    private String pkey;

    /**
     * 底库信息
     */
    @ApiModelProperty(value = "底库信息")
    private List<PersonObject> persons;

    /**
     * 当前 pkey 下的人的总数
     */
    @ApiModelProperty(value = "当前 pkey 下的人的总数")
    private int total;

    public String getPkey() {
        return pkey;
    }

    public void setPkey(String pkey) {
        this.pkey = pkey;
    }

    public List<PersonObject> getPersons() {
        return persons;
    }

    public void setPersons(List<PersonObject> persons) {
        this.persons = persons;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "GroupByPkey{" +
                "pkey='" + pkey + '\'' +
                ", persons=" + persons +
                ", total=" + total +
                '}';
    }
}
