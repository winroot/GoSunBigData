package com.hzgc.service.device.controller;

import com.hzgc.common.service.BigDataPath;
import com.hzgc.common.service.ResponseResult;
import com.hzgc.service.device.service.DeviceServiceImpl;
import com.hzgc.service.device.bean.DeviceData;
import io.swagger.annotations.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FeignClient(name = "device")
@RequestMapping(value = BigDataPath.DEVICE,consumes = "application/json",produces = "application/json")
@Api(value = "device",tags = "设备管理")
public class DeviceController {

    private static Logger LOG = Logger.getLogger(DeviceController.class);

    @Autowired
    private DeviceServiceImpl deviceService;

    @ApiOperation(value = "设备绑定",response = Boolean.class)
    @ApiResponses({
            @ApiResponse(code = 200,message = "successful response")
    })
    @RequestMapping(value = BigDataPath.DEVICE_BIND,method = RequestMethod.POST)
    public ResponseResult<Boolean> bindDevice(@RequestBody @ApiParam(value = "设备参数") DeviceData deviceData) {
        String platformId = null;
        String ipcId = null;
        String notes = null;
        if (null != deviceData) {
            platformId = deviceData.getPlatformId();
            ipcId = deviceData.getIpcID();
            notes = deviceData.getNotes();
        } else {
            return null;
        }
        Boolean succeed = deviceService.bindDevice(platformId, ipcId, notes);
        return ResponseResult.init(succeed);
    }

    @ApiOperation(value = "设备解绑",response = Boolean.class)
    @ApiResponses({
            @ApiResponse(code = 200,message = "successful response")
    })
    @RequestMapping(value = BigDataPath.DEVICE_UNBIND,method = RequestMethod.POST)
    public ResponseResult<Boolean> unbindDevice(@RequestBody @ApiParam(value = "设备参数") DeviceData deviceData) {
        String platformId = null;
        String ipcId = null;
        if (null != deviceData) {
            platformId = deviceData.getPlatformId();
            ipcId = deviceData.getIpcID();
        } else {
            return null;
        }
        Boolean succeed = deviceService.unbindDevice(platformId, ipcId);
        return ResponseResult.init(succeed);
    }

    @ApiOperation(value = "设备修改备注",response = Boolean.class)
    @ApiResponses({
            @ApiResponse(code = 200,message = "successful response")
    })
    @RequestMapping(value = BigDataPath.DEVICE_RENAMENOTES,method = RequestMethod.POST)
    public ResponseResult<Boolean> renameNotes(@RequestBody @ApiParam(value = "设备参数") DeviceData deviceData) {
        String notes = null;
        String ipcId = null;
        if (null != deviceData) {
            notes = deviceData.getNotes();
            ipcId = deviceData.getIpcID();
        } else {
            return null;
        }
        Boolean succeed = deviceService.renameNotes(notes, ipcId);
        return ResponseResult.init(succeed);
    }
}
