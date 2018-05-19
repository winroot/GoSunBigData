package com.hzgc.service.dynrepo.bean;

import com.hzgc.common.attribute.bean.Attribute;
import lombok.Data;

import java.util.List;

@Data
public class CaptureOption {
    //搜索的设备范围
    public List<String> deviceIds;
    //开始日期,格式：xxxx-xx-xx xx:xx:xx
    public String startTime;
    //截止日期,格式：xxxx-xx-xx xx:xx:xx
    public String endTime;
    //参数筛选选项
    public List<Attribute> attributes;
    //排序参数
    public List<Integer> sort;
    //分页查询开始行
    public int start;
    //查询条数
    public int limit;

}
