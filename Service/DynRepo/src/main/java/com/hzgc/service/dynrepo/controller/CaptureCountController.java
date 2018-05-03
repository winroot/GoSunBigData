package com.hzgc.service.dynrepo.controller;

import com.hzgc.common.service.BigDataPath;
import com.hzgc.common.service.ResponseResult;
import com.hzgc.common.util.searchtype.SearchType;
import com.hzgc.service.dynrepo.attribute.AttributeCount;
import com.hzgc.service.dynrepo.bean.CaptureCount;
import com.hzgc.service.dynrepo.bean.TimeSlotNumber;
import com.hzgc.service.dynrepo.bean.TotalAndTodayCount;
import com.hzgc.service.dynrepo.service.CaptureCountService;
import com.hzgc.service.dynrepo.bean.CaptureCountParam;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@FeignClient(name = "dynRepo")
@RequestMapping(value = BigDataPath.CAPTURECOUNT, consumes = "application/json", produces = "application/json")
@Api(value = "/captureCount", tags = "动态库抓拍统计服务")
public class CaptureCountController {

    @Autowired
    private CaptureCountService captureCountService;

    /**
     * 抓拍统计与今日抓拍统计
     * 查询es的动态库，返回总抓拍数量和今日抓拍数量
     *
     * @param captureCountParam 抓拍统计入参
     * @return Map
     */
    @ApiOperation(value = "抓拍统计与今日抓拍统计", response = Map.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful response"),
            @ApiResponse(code = 404, message = "404")})
    @RequestMapping(value = BigDataPath.CAPTURECOUNT_DYNREPO, method = RequestMethod.GET)
    public ResponseResult<TotalAndTodayCount> dynaicNumberService(
            @RequestBody @ApiParam(value = "抓拍统计入参") CaptureCountParam captureCountParam) {
        List<String> ipcIdList;
        if (captureCountParam != null) {
            ipcIdList = captureCountParam.getIpcIdList();
        } else {
            return null;
        }
        TotalAndTodayCount count = captureCountService.dynamicNumberService(ipcIdList);
        return ResponseResult.init(count);
    }

    /**
     * 多设备每小时抓拍统计
     * 返回某段时间内，这些ipcid的抓拍的总数量
     *
     * @param captureCountParam 抓拍统计入参
     * @return Map
     */
    @ApiOperation(value = "多设备每小时抓拍统计", response = Map.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful response"),
            @ApiResponse(code = 404, message = "404")})
    @RequestMapping(value = BigDataPath.CAPTURECOUNT_IPCIDS_TIME, method = RequestMethod.GET)
    public ResponseResult<TimeSlotNumber> timeSoltNumber(
            @RequestBody @ApiParam(value = "抓拍统计入参") CaptureCountParam captureCountParam) {
        List<String> ipcIdList;
        String startTime;
        String endTime;
        if (captureCountParam != null) {
            ipcIdList = captureCountParam.getIpcIdList();
            startTime = captureCountParam.getStartTime();
            endTime = captureCountParam.getEndTime();
        } else {
            return null;
        }
        TimeSlotNumber count = captureCountService.timeSoltNumber(ipcIdList, startTime, endTime);
        return ResponseResult.init(count);
    }

    /**
     * 单设备抓拍统计
     * 查询指定时间段内，指定设备抓拍的图片数量、该设备最后一次抓拍时间
     *
     * @param captureCountParam 抓拍统计入参
     * @return CaptureCount
     */
    @ApiOperation(value = "单设备抓拍统计", response = CaptureCount.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful response"),
            @ApiResponse(code = 404, message = "404")})
    @RequestMapping(value = BigDataPath.CAPTURECOUNT_IPCID, method = RequestMethod.GET)
    public ResponseResult<CaptureCount> captureCountQuery(
            @RequestBody @ApiParam(value = "抓拍统计入参") CaptureCountParam captureCountParam) {
        String startTime;
        String endTime;
        String ipcId;
        if (captureCountParam != null) {
            startTime = captureCountParam.getStartTime();
            endTime = captureCountParam.getEndTime();
            ipcId = captureCountParam.getIpcId();
        } else {
            return null;
        }
        CaptureCount captureCount = captureCountService.captureCountQuery(startTime, endTime, ipcId);
        return ResponseResult.init(captureCount);
    }

    /**
     * 多设备抓拍统计
     * 查询指定时间段内，指定的多个设备抓拍的图片数量
     *
     * @param captureCountParam 抓拍统计入参
     * @return Long
     */
    @ApiOperation(value = "多设备抓拍统计", response = Long.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful response"),
            @ApiResponse(code = 404, message = "404")})
    @RequestMapping(value = BigDataPath.CAPTURECOUNT_IPCIDS, method = RequestMethod.GET)
    public ResponseResult<Long> getCaptureCount(
            @RequestBody @ApiParam(value = "抓拍统计入参") CaptureCountParam captureCountParam) {
        String startTime;
        String endTime;
        List<String> ipcIdList;
        if (captureCountParam != null) {
            startTime = captureCountParam.getStartTime();
            endTime = captureCountParam.getEndTime();
            ipcIdList = captureCountParam.getIpcIdList();
        } else {
            return null;
        }
        Long count = captureCountService.getCaptureCount(startTime, endTime, ipcIdList);
        return ResponseResult.init(count);
    }

    /**
     * 抓拍属性统计
     * 查询指定时间段内，单个或某组设备中某种属性在抓拍图片中的数量
     *
     * @param captureCountParam 抓拍统计入参
     * @return List<AttributeCount>
     */
    @ApiOperation(value = "抓拍属性统计", response = AttributeCount.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful response"),
            @ApiResponse(code = 404, message = "404")})
    @RequestMapping(value = BigDataPath.CAPTURECOUNT_ATTRIBUTE, method = RequestMethod.POST)
    public ResponseResult<List<AttributeCount>> captureAttributeQuery(
            @RequestBody @ApiParam(value = "抓拍统计入参") CaptureCountParam captureCountParam) {
        String startTime;
        String endTime;
        List<String> ipcIdList;
        SearchType type;
        if (captureCountParam != null) {
            startTime = captureCountParam.getStartTime();
            endTime = captureCountParam.getEndTime();
            ipcIdList = captureCountParam.getIpcIdList();
            type = captureCountParam.getType();
        } else {
            return null;
        }
        List<AttributeCount> attributeCountList = captureCountService.captureAttributeQuery(startTime, endTime, ipcIdList, type);
        return ResponseResult.init(attributeCountList);
    }
}
