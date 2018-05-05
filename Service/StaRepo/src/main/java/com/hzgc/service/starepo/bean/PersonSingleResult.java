package com.hzgc.service.starepo.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 多个人的的情况下，每个图片对应的
 */
@ApiModel(value = "多个人的的情况下，每个图片对应的")
public class PersonSingleResult implements Serializable {

    /**
     * 子搜索Id
     */
    @ApiModelProperty(value = "子搜索Id")
    private String searchRowkey;

    /**
     * 搜索的总数
     */
    @ApiModelProperty(value = "搜索的总数")
    private int searchNums;

    /**
     * 搜索图片
     */
    @ApiModelProperty(value = "搜索图片")
    private List<byte[]> searchPhotos;

    /**
     * 不用聚类的时候的返回结果
     */
    @ApiModelProperty(value = "不用聚类的时候的返回结果")
    private List<PersonObject> persons;

    /**
     * 根据 pkey 分类后的返回结果
     */
    @ApiModelProperty(value = "根据 pkey 分类后的返回结果")
    private List<GroupByPkey> groupByPkeys;

    public String getSearchRowkey() {
        return searchRowkey;
    }

    public void setSearchRowkey(String searchRowkey) {
        this.searchRowkey = searchRowkey;
    }

    public int getSearchNums() {
        return searchNums;
    }

    public void setSearchNums(int searchNums) {
        this.searchNums = searchNums;
    }

    public List<byte[]> getSearchPhotos() {
        return searchPhotos;
    }

    public void setSearchPhotos(List<byte[]> searchPhotos) {
        this.searchPhotos = searchPhotos;
    }

    public List<PersonObject> getPersons() {
        return persons;
    }

    public void setPersons(List<PersonObject> persons) {
        this.persons = persons;
    }

    public List<GroupByPkey> getGroupByPkeys() {
        return groupByPkeys;
    }

    public void setGroupByPkeys(List<GroupByPkey> groupByPkeys) {
        this.groupByPkeys = groupByPkeys;
    }

    @Override
    public String toString() {
        return "PersonSingleResult{" +
                "searchRowkey='" + searchRowkey + '\'' +
                ", searchNums=" + searchNums +
                ", searchPhotos=" + searchPhotos +
                ", persons=" + persons +
                ", groupByPkeys=" + groupByPkeys +
                '}';
    }
}
