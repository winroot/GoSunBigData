package com.hzgc.service.visual.controller;

import com.hzgc.common.service.error.RestErrorCode;
import com.hzgc.common.service.response.ResponseResult;
import com.hzgc.common.service.rest.BigDataPath;
import com.hzgc.common.service.rest.BigDataPermission;
import com.hzgc.common.util.json.JSONUtil;
import com.hzgc.service.visual.bean.ObjectInfo;
import com.hzgc.service.visual.bean.PeopleManager;
import com.hzgc.service.visual.bean.PeopleParam;
import com.hzgc.service.visual.service.PeopleManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
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
        log.info("Param are start_time: " + start_time + "  , end_time: " + end_time);
        if (StringUtils.isBlank(start_time) || StringUtils.isBlank(end_time)) {
            log.error("Start people count, but start_time or end_time is error");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
        }
        return ResponseResult.init(peopleManagerService.peopleCount(start_time, end_time));
    }

    /**
     * 关爱人员离线查询
     *
     * @param param
     * @return List<ObjectInfo>
     * @parm offTime  单位：小时
     */
    @ApiOperation(value = "关爱人口离线查询", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.GET_CARE_PEOPLE, method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('" + BigDataPermission.OBJECT_VIEW + "')")
    public ResponseResult<List<ObjectInfo>> getCarePeople(
            @RequestBody @ApiParam(value = "关爱人口离线查询入参")PeopleParam param) {
        if (param == null) {
            log.error("Start search care people, but param is nul");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询入参为空，请检查！");
        }
        List<String> objectTypeKeyList = param.getObjectTypeKeyList();
        String offTime = param.getOffTime();
        if (objectTypeKeyList == null || objectTypeKeyList.size() == 0 || StringUtils.isBlank(offTime)) {
            log.error("Start search care people, but object type key or offerTime is nul");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询参数为空，请检查！");
        }
        log.info("Start search care people, param is " + JSONUtil.toJson(param));
        List<ObjectInfo> result = peopleManagerService.getCarePeople(param);
        if (result != null) {
            return ResponseResult.init(result);
        }
        return ResponseResult.error(RestErrorCode.RECORD_NOT_EXIST);
    }

    /**
     * 常住人口查询
     *log/
     * @param param
     * @return List<ObjectInfo>
     */
    @ApiOperation(value = "常住人口查询", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.GET_STATUS_PEOPLE, method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @PreAuthorize("hasAuthority('" + BigDataPermission.OBJECT_VIEW + "')")
    public ResponseResult<List<ObjectInfo>> getStatusPeople(
            @RequestBody @ApiParam(value = "常住人口查询入参")PeopleParam param) {
        if (param == null){
            log.error("Start search care people, but param is nul");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询入参为空，请检查！");
        }
        List<String> objectTypeKeyList = param.getObjectTypeKeyList();
        if (objectTypeKeyList == null || objectTypeKeyList.size() == 0) {
            log.error("Start search status people, but object type key list is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询参数为空，请检查！");
        }
        log.info("Start search status people, param object type key list = " + JSONUtil.toJson(param));
        List<ObjectInfo> result = peopleManagerService.getStatusPeople(param);
        if (result != null) {
            return ResponseResult.init(result);
        }
        return ResponseResult.error(RestErrorCode.RECORD_NOT_EXIST);
    }

    /**
     * 重点人口查询
     *
     * @param param
     * @return List<ObjectInfo>
     */
    @ApiOperation(value = "重点人口查询", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.GET_IMPORTANT_PEOPLE, method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('" + BigDataPermission.OBJECT_VIEW + "')")
    public ResponseResult<List<ObjectInfo>> getImportantPeople(
            @RequestBody @ApiParam(value = "重点人口查询入参")PeopleParam param) {
        if (param == null){
            log.error("Start search care people, but param is nul");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询入参为空，请检查！");
        }
        List<String> objectTypeKeyList = param.getObjectTypeKeyList();
        if (objectTypeKeyList == null || objectTypeKeyList.size() == 0) {
            log.error("Start search important people, but object type key list is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "查询参数为空，请检查！");
        }
        log.info("Start search status people, param object type key list = " + JSONUtil.toJson(param));
        List<ObjectInfo> result = peopleManagerService.getImportantPeople(param);
        if (result != null) {
            return ResponseResult.init(result);
        }
        return ResponseResult.error(RestErrorCode.RECORD_NOT_EXIST);
    }
}
