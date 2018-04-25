package com.hzgc.service.dynrepo.controller;

import com.hzgc.common.service.BigDataPath;
import com.hzgc.common.service.ResponseResult;
import com.hzgc.service.dynrepo.service.CaptureNumberImpl;
import com.hzgc.service.dynrepo.vo.CaptureNumberVO;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@FeignClient(name = "dynRepo")
@RequestMapping(value = BigDataPath.CAPTURENUM, consumes = "application/json", produces = "application/json")
@Api(value = "/captureCount", tags = "大数据可视化服务")
public class CaptureNumberController {

    @Autowired
    private CaptureNumberImpl captureNumberService;

    @ApiOperation(value = "总抓拍统计", response = Map.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful response"),
            @ApiResponse(code = 404, message = "404")})
    @RequestMapping(value = BigDataPath.CAPTURENUM_SEARCHDYNREPO, method = RequestMethod.GET)
    public ResponseResult<Map<String,Integer>> dynaicNumberService(
            @RequestBody @ApiParam(value = "大数据可视化入参") CaptureNumberVO captureNumberVO) {
        List<String> ipcIdList;
        if (captureNumberVO != null){
            ipcIdList = captureNumberVO.getIpcIdList();
        }else {
            return null;
        }
        Map<String,Integer> count = captureNumberService.dynaicNumberService(ipcIdList);
        return ResponseResult.init(count);
    }

    @ApiOperation(value = "对象库人员统计", response = Map.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful response"),
            @ApiResponse(code = 404, message = "404")})
    @RequestMapping(value = BigDataPath.CAPTURENUM_SEARCHSTAREPO, method = RequestMethod.GET)
    public ResponseResult<Map<String,Integer>> staticNumberService(
            @RequestBody @ApiParam(value = "大数据可视化入参") CaptureNumberVO captureNumberVO) {
        String platformId;
        if (captureNumberVO != null){
            platformId = captureNumberVO.getPlatformId();
        }else {
            return null;
        }
        Map<String,Integer> count = captureNumberService.staticNumberService(platformId);
        return ResponseResult.init(count);
    }

    @ApiOperation(value = "设备抓拍统计", response = Map.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful response"),
            @ApiResponse(code = 404, message = "404")})
    @RequestMapping(value = BigDataPath.CAPTURENUM_FILTER, method = RequestMethod.GET)
    public ResponseResult<Map<String,Integer>> timeSoltNumber(
            @RequestBody @ApiParam(value = "大数据可视化入参") CaptureNumberVO captureNumberVO) {
        List<String> ipcIdList;
        String startTime;
        String endTime;
        if (captureNumberVO != null){
            ipcIdList = captureNumberVO.getIpcIdList();
            startTime = captureNumberVO.getStartTime();
            endTime = captureNumberVO.getEndTime();
        }else {
            return null;
        }
        Map<String,Integer> count = captureNumberService.timeSoltNumber(ipcIdList,startTime,endTime);
        return ResponseResult.init(count);
    }
}
