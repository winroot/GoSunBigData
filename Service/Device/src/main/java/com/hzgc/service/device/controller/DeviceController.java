package com.hzgc.service.device.controller;

import com.hzgc.common.service.BigDataPath;
import com.hzgc.common.service.ResponseResult;
import com.hzgc.service.device.service.DeviceServiceImpl;
import com.hzgc.service.device.vo.DeviceDataVO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FeignClient(name = "device")
@RequestMapping(value = BigDataPath.DEVICE)
public class DeviceController {

    private static Logger LOG = Logger.getLogger(DeviceController.class);

    @Autowired
    private DeviceServiceImpl deviceService;

    @RequestMapping(value = BigDataPath.DEVICE_BIND)
    public ResponseResult bindDevice(DeviceDataVO deviceDataVO) {
        String platformId;
        String ipcId;
        String notes;
        if (null != deviceDataVO) {
            platformId = deviceDataVO.getPlatformId();
            ipcId = deviceDataVO.getIpcID();
            notes = deviceDataVO.getNotes();
        } else {
            return null;
        }
        boolean succeed = deviceService.bindDevice(platformId, ipcId, notes);
        return ResponseResult.init(succeed);
    }

    @RequestMapping(value = BigDataPath.DEVICE_UNBIND)
    public ResponseResult unbindDevice(DeviceDataVO deviceDataVO) {
        String platformId;
        String ipcId;
        if (null != deviceDataVO) {
            platformId = deviceDataVO.getPlatformId();
            ipcId = deviceDataVO.getIpcID();
        } else {
            return null;
        }
        boolean succeed = deviceService.unbindDevice(platformId, ipcId);
        return ResponseResult.init(succeed);
    }

    @RequestMapping(value = BigDataPath.DEVICE_RENAMENOTES)
    public ResponseResult renameNotes(DeviceDataVO deviceDataVO) {
        String notes;
        String ipcId;
        if (null != deviceDataVO) {
            notes = deviceDataVO.getNotes();
            ipcId = deviceDataVO.getIpcID();
        } else {
            return null;
        }
        boolean succeed = deviceService.renameNotes(notes, ipcId);
        return ResponseResult.init(succeed);
    }
}
