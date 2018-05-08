package com.hzgc.service.device.controller;

import com.hzgc.common.service.BigDataPath;
import com.hzgc.common.service.ResponseResult;
import com.hzgc.service.device.bean.DeviceDataParam;
import com.hzgc.service.device.service.DeviceService;
import io.swagger.annotations.*;
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

    @Autowired
    private DeviceService deviceService;

    @ApiOperation(value = "设备绑定",response = Boolean.class)
    @ApiResponses({
            @ApiResponse(code = 200,message = "successful response")
    })
    @RequestMapping(value = BigDataPath.DEVICE_BIND,method = RequestMethod.POST)
    public ResponseResult<Boolean> bindDevice(@RequestBody @ApiParam(value = "设备参数") DeviceDataParam deviceDataParam) {
        String platformId;
        String ipcId;
        String notes;
        if (null != deviceDataParam) {
            platformId = deviceDataParam.getPlatformId();
            ipcId = deviceDataParam.getIpcID();
            notes = deviceDataParam.getNotes();
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
    public ResponseResult<Boolean> unbindDevice(@RequestBody @ApiParam(value = "设备参数") DeviceDataParam deviceDataParam) {
        String platformId;
        String ipcId;
        if (null != deviceDataParam) {
            platformId = deviceDataParam.getPlatformId();
            ipcId = deviceDataParam.getIpcID();
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
    public ResponseResult<Boolean> renameNotes(@RequestBody @ApiParam(value = "设备参数") DeviceDataParam deviceDataParam) {
        String notes;
        String ipcId;
        if (null != deviceDataParam) {
            notes = deviceDataParam.getNotes();
            ipcId = deviceDataParam.getIpcID();
        } else {
            return null;
        }
        Boolean succeed = deviceService.renameNotes(notes, ipcId);
        return ResponseResult.init(succeed);
    }
}
