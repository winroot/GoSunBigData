package com.hzgc.service.visual.controller;

import com.hzgc.common.service.error.RestErrorCode;
import com.hzgc.common.service.response.ResponseResult;
import com.hzgc.common.service.rest.BigDataPath;
import com.hzgc.service.visual.bean.*;
import com.hzgc.service.visual.service.CaptureCountService;
import com.hzgc.service.visual.util.DateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Api(tags = "大数据可视化服务")
@Slf4j
public class CaptureCountController {

    @Autowired
    private CaptureCountService captureCountService;

    /**
     * 抓拍统计与今日抓拍统计
     * 查询es的动态库，返回总抓拍数量和今日抓拍数量
     *
     * @param areaId 区域ID
     * @param level 区域等级
     * @return 总抓拍数量和今日抓拍数量
     */
    @ApiOperation(value = "抓拍统计与今日抓拍统计", response = TotalAndTodayCount.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "areaId", value = "区域ID", paramType = "query"),
            @ApiImplicitParam(name = "level", value = "区域等级", paramType = "query")
    })
    @RequestMapping(value = BigDataPath.CAPTURECOUNT_DYNREPO, method = RequestMethod.GET)
    public ResponseResult<CaptureCountBean> dynaicNumberService(Long areaId, String level) {
        if (areaId == null || StringUtils.isBlank(level)){
            log.error("Start count capture total and today capture count, but param error");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT,
                    "Param: areaId or level error");
        }
        log.info("Start count capture total and today capture count, param is: areaId = "
                + areaId + ", level = " + level);
        CaptureCountBean count = captureCountService.dynamicNumberService(areaId, level);
        return ResponseResult.init(count);
    }

    /**
     * 多设备每小时抓拍统计
     * 返回某段时间内，这些ipcid的抓拍的总数量
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 每小时抓拍数
     */
    @ApiOperation(value = "多设备抓拍统计（每小时）", response = TimeSlotNumber.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", paramType = "query")
    })
    @RequestMapping(value = BigDataPath.CAPTURECOUNT_IPCIDS_TIME, method = RequestMethod.GET)
    public ResponseResult<TimeSlotNumber> timeSoltNumber(String startTime, String endTime) {
        //时间格式检测
        if(!DateUtils.checkForm(startTime) || !DateUtils.checkForm(endTime)){
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT,
                    "Time does not conform to the format: yyyy-MM-dd");
        }
        TimeSlotNumber count = captureCountService.timeSoltNumber(new ArrayList<>(), startTime, endTime);
        return ResponseResult.init(count);

    }

    /**
     * 某地区某段日期每天固定时间段抓拍统计
     *
     * @param startData 开始日期
     * @param endData 结束日期
     * @return List<CaptureCountSixHour> 每六小时抓拍统计
     */
    @ApiOperation(value = "多设备抓拍统计（每六小时）", response = TimeSlotNumber.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startData", value = "开始日期", paramType = "query"),
            @ApiImplicitParam(name = "endData", value = "结束日期", paramType = "query"),
            @ApiImplicitParam(name = "areaId", value = "区域ID", paramType = "query"),
            @ApiImplicitParam(name = "level", value = "区域等级", paramType = "query")
    })
    @RequestMapping(value = BigDataPath.CAPTURECOUNT_SIX_HOUR, method = RequestMethod.GET)
    public ResponseResult<List<CaptureCountSixHour>> captureCountSixHour(String startData, String endData, Long areaId, String level) {
        //时间格式检测
        if(!DateUtils.checkForm(startData) || !DateUtils.checkForm(endData)){
            log.error("Start count capture every six hours, but time does not conform to the format: yyyy-MM-dd");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT,
                    "Time does not conform to the format: yyyy-MM-dd");
        }
        if (areaId == null || StringUtils.isBlank(level)){
            log.error("Start count capture every six hours, but param: areaId or level error");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT,
                    "Param: areaId or level error");
        }
        log.info("Start count capture every six hours, param is: areaId = " + areaId + ", level = " + level);
        List<CaptureCountSixHour> list = captureCountService.captureCountSixHour(startData, endData, areaId, level);
        return ResponseResult.init(list);

    }

    /**
     * 多设备抓拍，每天统计
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 每天抓拍数
     */
    @ApiOperation(value = "多设备抓拍统计（每天）", response = Long.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", paramType = "query")
    })
    @RequestMapping(value = BigDataPath.CAPTURECOUNT_IPCIDS, method = RequestMethod.GET)
    public ResponseResult<List<StatisticsBean>> getStatisticsFace(String startTime, String endTime){
        //时间格式检测
        if(!DateUtils.checkForm(startTime) || !DateUtils.checkForm(endTime)){
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT,
                    "Time does not conform to the format: yyyy-MM-dd");
        }
        List<StatisticsBean> count = captureCountService.getStatisticsFace(startTime, endTime, new ArrayList<>());
        return ResponseResult.init(count);
    }

    /**
     * 根据URL获取图片
     * @param ftpurl 图片地址
     * @return 图片内容
     */
    @ApiOperation(value = "获取图片", response = Long.class)
    @ApiImplicitParam(name = "ftpurl", value = "图片地址", paramType = "query")
    @RequestMapping(value = BigDataPath.GET_PICTURE, method = RequestMethod.GET)
    public ResponseResult<String> getImageBase64(String ftpurl) {
        if (ftpurl != null && !"".equals(ftpurl)) {
            String photo = captureCountService.getImageBase64(ftpurl);
            return ResponseResult.init(photo);
        }
        return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
    }
}
