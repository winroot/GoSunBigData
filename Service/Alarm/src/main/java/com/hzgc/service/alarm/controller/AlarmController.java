package com.hzgc.service.alarm.controller;

import com.hzgc.common.util.json.JSONUtil;
import com.hzgc.service.alarm.bean.AlarmBean;
import com.hzgc.service.alarm.bean.UserAlarmMessage;
import com.hzgc.service.alarm.service.AlarmService;
import com.hzgc.service.util.error.RestErrorCode;
import com.hzgc.service.util.response.ResponseResult;
import com.hzgc.service.util.rest.BigDataPath;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@Api(tags = "告警信息查询")
public class AlarmController {

    @Autowired
    private AlarmService alarmService;

    @ApiOperation(value = "告警查询",response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.ALARM__QUERY, method = RequestMethod.POST)
    public ResponseResult<List<UserAlarmMessage>> alarmSearch(@RequestBody @ApiParam(value = "告警参数") AlarmBean alarmBean) throws Exception {
        log.info("Start param is " + JSONUtil.toJson(alarmBean));
        if (null == alarmBean){
            log.info("Alarm param is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
        }
        log.info("Alarm query is successful");
        return alarmService.alarmSearch(alarmBean);
    }
}
