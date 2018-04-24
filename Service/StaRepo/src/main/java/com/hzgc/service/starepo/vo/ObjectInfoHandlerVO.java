package com.hzgc.service.starepo.vo;

import com.hzgc.service.starepo.object.PSearchArgsModel;
import com.hzgc.service.starepo.object.SearchRecordOpts;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 静态库前台入参
 */
public class ObjectInfoHandlerVO implements Serializable{

    /**
     * 平台ID
     */
    private String platformId;

    /**
     * K-V 对，里面存放的是字段和值之间的一一对应关系,
     * 例如：传入一个Map 里面的值如下map.put("idcard", "450722199502196939")
     * 表示的是身份证号（idcard）是450722199502196939，
     * 其中的K 的具体，请参考给出的数据库字段设计
     */
    private Map<String, Object> personObject;

    /**
     * 具体的一个人员信息的ID，值唯一
     */
    private List<String> rowkeys;

    /**
     * 搜索参数的封装
     */
    private PSearchArgsModel pSearchArgsModel;

    /**
     * 标记一条对象信息的唯一标志
     */
    private String rowkey;

    /**
     * 历史查询参数
     */
    private SearchRecordOpts searchRecordOpts;

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public Map<String, Object> getPersonObject() {
        return personObject;
    }

    public void setPersonObject(Map<String, Object> personObject) {
        this.personObject = personObject;
    }

    public List<String> getRowkeys() {
        return rowkeys;
    }

    public void setRowkeys(List<String> rowkeys) {
        this.rowkeys = rowkeys;
    }

    public PSearchArgsModel getpSearchArgsModel() {
        return pSearchArgsModel;
    }

    public void setpSearchArgsModel(PSearchArgsModel pSearchArgsModel) {
        this.pSearchArgsModel = pSearchArgsModel;
    }

    public String getRowkey() {
        return rowkey;
    }

    public void setRowkey(String rowkey) {
        this.rowkey = rowkey;
    }

    public SearchRecordOpts getSearchRecordOpts() {
        return searchRecordOpts;
    }

    public void setSearchRecordOpts(SearchRecordOpts searchRecordOpts) {
        this.searchRecordOpts = searchRecordOpts;
    }
}
