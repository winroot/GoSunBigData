package com.hzgc.service.visual.controller;

import com.hzgc.service.util.response.ResponseResult;
import com.hzgc.service.util.rest.BigDataPath;
import com.hzgc.service.visual.service.PeopleManagerService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "人口管理服务")
public class PeopleManagerController {
    @Autowired
    private PeopleManagerService peopleManagerService;

    @RequestMapping(value = BigDataPath.STAREPO_COUNT_MIGRATION, method = RequestMethod.GET)
    public ResponseResult
}
