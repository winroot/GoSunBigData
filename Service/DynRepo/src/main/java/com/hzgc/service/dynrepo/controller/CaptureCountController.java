package com.hzgc.service.dynrepo.controller;

import com.hzgc.common.service.BigDataPath;
import com.hzgc.common.service.ResponseResult;
import com.hzgc.common.util.searchtype.SearchType;
import com.hzgc.service.dynrepo.attribute.AttributeCount;
import com.hzgc.service.dynrepo.object.CaptureCount;
import com.hzgc.service.dynrepo.service.CaptureCountServiceImpl;
import com.hzgc.service.dynrepo.vo.CaptureCountVO;
import com.hzgc.service.dynrepo.vo.CapturePictureSearchVO;
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
    private CaptureCountServiceImpl captureCountService;

    /**
     * 抓拍统计与今日抓拍统计
     * 查询es的动态库，返回总抓拍数量和今日抓拍数量
     *
     * @param captureCountVO 抓拍统计入参
     * @return Map
     */
    @ApiOperation(value = "抓拍统计与今日抓拍统计", response = Map.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful response"),
            @ApiResponse(code = 404, message = "404")})
    @RequestMapping(value = BigDataPath.CAPTURECOUNT_DYNREPO, method = RequestMethod.GET)
    public ResponseResult<Map<String, Integer>> dynaicNumberService(
            @RequestBody @ApiParam(value = "抓拍统计入参") CaptureCountVO captureCountVO) {
        List<String> ipcIdList;
        if (captureCountVO != null) {
            ipcIdList = captureCountVO.getIpcIdList();
        } else {
            return null;
        }
        Map<String, Integer> count = captureCountService.dynaicNumberService(ipcIdList);
        return ResponseResult.init(count);
    }

    /**
     * 单平台下对象库人员统计
     * 查询es的静态库，返回每个平台下（对应platformId），每个对象库（对应pkey）下的人员的数量
     *
     * @param captureCountVO 抓拍统计入参
     * @return Map
     */
    @ApiOperation(value = "单平台下对象库人员统计", response = Map.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful response"),
            @ApiResponse(code = 404, message = "404")})
    @RequestMapping(value = BigDataPath.CAPTURECOUNT_STAREPO, method = RequestMethod.GET)
    public ResponseResult<Map<String, Integer>> staticNumberService(
            @RequestBody @ApiParam(value = "抓拍统计入参") CaptureCountVO captureCountVO) {
        String platformId;
        if (captureCountVO != null) {
            platformId = captureCountVO.getPlatformId();
        } else {
            return null;
        }
        Map<String, Integer> count = captureCountService.staticNumberService(platformId);
        return ResponseResult.init(count);
    }

    /**
     * 多设备每小时抓拍统计
     * 返回某段时间内，这些ipcid的抓拍的总数量
     *
     * @param captureCountVO 抓拍统计入参
     * @return Map
     */
    @ApiOperation(value = "多设备每小时抓拍统计", response = Map.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful response"),
            @ApiResponse(code = 404, message = "404")})
    @RequestMapping(value = BigDataPath.CAPTURECOUNT_IPCIDS_TIME, method = RequestMethod.GET)
    public ResponseResult<Map<String, Integer>> timeSoltNumber(
            @RequestBody @ApiParam(value = "抓拍统计入参") CaptureCountVO captureCountVO) {
        List<String> ipcIdList;
        String startTime;
        String endTime;
        if (captureCountVO != null) {
            ipcIdList = captureCountVO.getIpcIdList();
            startTime = captureCountVO.getStartTime();
            endTime = captureCountVO.getEndTime();
        } else {
            return null;
        }
        Map<String, Integer> count = captureCountService.timeSoltNumber(ipcIdList, startTime, endTime);
        return ResponseResult.init(count);
    }

    /**
     * 单设备抓拍统计
     * 查询指定时间段内，指定设备抓拍的图片数量、该设备最后一次抓拍时间
     *
     * @param capturePictureSearchVO 抓拍统计入参
     * @return CaptureCount
     */
    @ApiOperation(value = "单设备抓拍统计", response = CaptureCount.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful response"),
            @ApiResponse(code = 404, message = "404")})
    @RequestMapping(value = BigDataPath.CAPTURECOUNT_IPCID, method = RequestMethod.GET)
    public ResponseResult<CaptureCount> captureCountQuery(
            @RequestBody @ApiParam(value = "抓拍统计入参") CapturePictureSearchVO capturePictureSearchVO) {
        String startTime;
        String endTime;
        String ipcId;
        if (capturePictureSearchVO != null) {
            startTime = capturePictureSearchVO.getStartTime();
            endTime = capturePictureSearchVO.getEndTime();
            ipcId = capturePictureSearchVO.getIpcId();
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
     * @param capturePictureSearchVO 抓拍统计入参
     * @return Long
     */
    @ApiOperation(value = "多设备抓拍统计", response = Long.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful response"),
            @ApiResponse(code = 404, message = "404")})
    @RequestMapping(value = BigDataPath.CAPTURECOUNT_IPCIDS, method = RequestMethod.GET)
    public ResponseResult<Long> getCaptureCount(
            @RequestBody @ApiParam(value = "抓拍统计入参") CapturePictureSearchVO capturePictureSearchVO) {
        String startTime;
        String endTime;
        List<String> ipcIdList;
        if (capturePictureSearchVO != null) {
            startTime = capturePictureSearchVO.getStartTime();
            endTime = capturePictureSearchVO.getEndTime();
            ipcIdList = capturePictureSearchVO.getIpcIdList();
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
     * @param capturePictureSearchVO 抓拍统计入参
     * @return List<AttributeCount>
     */
    @ApiOperation(value = "抓拍属性统计", response = AttributeCount.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful response"),
            @ApiResponse(code = 404, message = "404")})
    @RequestMapping(value = BigDataPath.CAPTURECOUNT_ATTRIBUTE, method = RequestMethod.POST)
    public ResponseResult<List<AttributeCount>> captureAttributeQuery(
            @RequestBody @ApiParam(value = "抓拍统计入参") CapturePictureSearchVO capturePictureSearchVO) {
        String startTime;
        String endTime;
        List<String> ipcIdList;
        SearchType type;
        if (capturePictureSearchVO != null) {
            startTime = capturePictureSearchVO.getStartTime();
            endTime = capturePictureSearchVO.getEndTime();
            ipcIdList = capturePictureSearchVO.getIpcIdList();
            type = capturePictureSearchVO.getType();
        } else {
            return null;
        }
        List<AttributeCount> attributeCountList = captureCountService.captureAttributeQuery(startTime, endTime, ipcIdList, type);
        return ResponseResult.init(attributeCountList);
    }
}
