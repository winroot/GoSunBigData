package com.hzgc.service.dynrepo.controller;

import com.hzgc.common.service.BigDataPath;
import com.hzgc.common.service.ResponseResult;
import com.hzgc.service.dynrepo.service.CaptureNumberImpl;
import com.hzgc.service.dynrepo.vo.CaptureNumberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@FeignClient(name = "dynRepo")
@RequestMapping(value = BigDataPath.DYNREPO_CAPTURENUM)
public class CaptureNumberController {

    @Autowired
    private CaptureNumberImpl captureNumberService;

    @RequestMapping(value = BigDataPath.DYNREPO_CAPTURENUM_SEARCHDYNREPO)
    public ResponseResult dynaicNumberService(CaptureNumberVO captureNumberVO) {
        List<String> ipcIdList;
        if (captureNumberVO != null){
            ipcIdList = captureNumberVO.getIpcIdList();
        }else {
            return null;
        }
        Map<String,Integer> count = captureNumberService.dynaicNumberService(ipcIdList);
        return ResponseResult.init(count);
    }

    @RequestMapping(value = BigDataPath.DYNREPO_CAPTURENUM_SEARCHSTAREPO)
    public ResponseResult staticNumberService(CaptureNumberVO captureNumberVO) {
        String platformId;
        if (captureNumberVO != null){
            platformId = captureNumberVO.getPlatformId();
        }else {
            return null;
        }
        Map<String,Integer> count = captureNumberService.staticNumberService(platformId);
        return ResponseResult.init(count);
    }

    @RequestMapping(value = BigDataPath.DYNREPO_CAPTURENUM_FILTER)
    public ResponseResult timeSoltNumber(CaptureNumberVO captureNumberVO) {
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
