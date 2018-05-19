package com.hzgc.service.dynrepo.service;

import com.hzgc.service.dynrepo.bean.platform.DeviceDTO;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DeviceQueryService {
    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "getDeviceNameError")
    public DeviceDTO getDeviceInfo(String ipcId) {
        if (!StringUtils.isBlank(ipcId)) {
            return restTemplate.getForObject("http://device-service/devices/query_by_serial/" + ipcId, DeviceDTO.class);
        }
        return new DeviceDTO();
    }

    public DeviceDTO getDeviceInfoError(String ipcId) {
        return new DeviceDTO();
    }

    @HystrixCommand
    @SuppressWarnings("unchecked")
    public Map<String, DeviceDTO> getDeviceInfoByBatch(List<String> ipcList) {
        if (ipcList != null && ipcList.size() > 0) {
            return restTemplate.postForObject("http://device-service/devices/batch_query_by_serial", ipcList,
                    Map.class);
        }
        return new HashMap<>();
    }

    public Map<String, DeviceDTO> getDeviceInfoByBatchError(List<String> ipcList) {
        return new HashMap<>();
    }
}
