package com.hzgc.service.visual.controller;

import com.hzgc.service.util.error.RestErrorCode;
import com.hzgc.service.util.response.ResponseResult;
import com.hzgc.service.util.rest.BigDataPath;
import com.hzgc.service.visual.bean.PeopleManager;
import com.hzgc.service.visual.service.PeopleManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "人口管理统计服务")
@Slf4j
public class PeopleManagerController {
    @Autowired
    private PeopleManagerService peopleManagerService;

    @ApiOperation(value = "获取迁入迁出人口统计", response = PeopleManager.class, responseContainer = "List")
    @RequestMapping(value = BigDataPath.PEOPLE_COUNT, method = RequestMethod.GET)
    public ResponseResult<List<PeopleManager>> peopleCount(String start_time, String end_time) {
        if (StringUtils.isBlank(start_time) || StringUtils.isBlank(end_time)) {
            log.error("Start people count, but start_time or end_time is error");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
        }
        return ResponseResult.init(peopleManagerService.peopleCount(start_time, end_time));
    }
}
