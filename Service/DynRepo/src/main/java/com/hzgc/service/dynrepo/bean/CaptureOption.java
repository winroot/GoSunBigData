package com.hzgc.service.dynrepo.bean;

import com.hzgc.common.faceattribute.bean.Attribute;
import com.hzgc.service.util.api.bean.DeviceDTO;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class CaptureOption implements Serializable {
    //搜索的设备ID列表
    private List<Long> deviceIds;
    //搜索的设备IPC列表
    private List<String> deviceIpcs;
    // ipc mapping device id
    private Map<String, DeviceDTO> ipcMappingDevice;
    //开始日期,格式：xxxx-xx-xx xx:xx:xx
    private String startTime;
    //截止日期,格式：xxxx-xx-xx xx:xx:xx
    private String endTime;
    //参数筛选选项
    private List<Attribute> attributes;
    //排序参数
    private List<Integer> sort;
    //分页查询开始行
    private int start;
    //查询条数
    private int limit;

    public List<String> getDeviceIpcs() {
        return deviceIpcs;
    }

    public void setDeviceIpcs(List<String> deviceIpcs) {
        this.deviceIpcs = deviceIpcs;
    }

    public List<Long> getDeviceIds() {
        return deviceIds;
    }

    public void setDeviceIds(List<Long> deviceIds) {
        this.deviceIds = deviceIds;
    }

    public Map<String, DeviceDTO> getIpcMappingDevice() {
        return ipcMappingDevice;
    }

    public void setIpcMappingDevice(Map<String, DeviceDTO> ipcMappingDevice) {
        this.ipcMappingDevice = ipcMappingDevice;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public List<Integer> getSort() {
        return sort;
    }

    public void setSort(List<Integer> sort) {
        this.sort = sort;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
