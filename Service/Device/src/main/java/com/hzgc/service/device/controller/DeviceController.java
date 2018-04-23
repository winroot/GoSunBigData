package com.hzgc.service.device.controller;

import com.hzgc.common.service.BigDataPath;
import com.hzgc.common.service.ResponseResult;
import com.hzgc.service.device.impl.DeviceServiceImpl;
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
    public ResponseResult<Boolean> bindDevice(DeviceDataVO deviceDataVO) {
        if (null != deviceDataVO && null != deviceDataVO.getPlatformId() && null != deviceDataVO.getIpcID() && null != deviceDataVO.getNotes()) {
            boolean b = deviceService.bindDevice(deviceDataVO.getPlatformId(), deviceDataVO.getIpcID(), deviceDataVO.getNotes());
            return ResponseResult.init(b);
        }
        return null;
    }

    @RequestMapping(value = BigDataPath.DEVICE_UNBIND)
    public ResponseResult<Boolean> unbindDevice(DeviceDataVO deviceDataVO) {
        if (null != deviceDataVO && null != deviceDataVO.getPlatformId() && null != deviceDataVO.getIpcID()){
            boolean b = deviceService.unbindDevice(deviceDataVO.getPlatformId(), deviceDataVO.getIpcID());
            return ResponseResult.init(b);
        }
        return null;
    }

    @RequestMapping(value = BigDataPath.DEVICE_RENAMENOTES)
    public ResponseResult<Boolean> renameNotes(DeviceDataVO deviceDataVO) {
        if(null != deviceDataVO && null != deviceDataVO.getNotes() && null != deviceDataVO.getIpcID()){
            boolean b = deviceService.renameNotes(deviceDataVO.getNotes(), deviceDataVO.getIpcID());
            return ResponseResult.init(b);
        }
        return null;
    }
}
