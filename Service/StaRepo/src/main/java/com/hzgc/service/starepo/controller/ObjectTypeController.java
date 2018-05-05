package com.hzgc.service.starepo.controller;

import com.hzgc.common.service.BigDataPath;
import com.hzgc.common.service.ResponseResult;
import com.hzgc.service.starepo.service.ObjectTypeService;
import com.hzgc.service.starepo.bean.ObjectType;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@FeignClient(name = "staRepo")
@RequestMapping(value = BigDataPath.TYPE, consumes = "application/json", produces = "application/json")
@Api(value = "objectType", tags = "对象类型服务")
public class ObjectTypeController {

    @Autowired
    private ObjectTypeService objectTypeService;

    @ApiOperation(value = "添加对象类型", response = Boolean.class)
    @ApiResponses(
            {@ApiResponse(code = 200, message = "successful response")})
    @RequestMapping(value = BigDataPath.TYPE_ADD, method = RequestMethod.POST)
    public ResponseResult<Boolean> addObjectType(@RequestBody @ApiParam(value = "对象类型") ObjectType objectType) {
        String name;
        String creator;
        String remark;
        if (objectType != null) {
            name = objectType.getName();
            creator = objectType.getCreator();
            remark = objectType.getRemark();
        } else {
            return null;
        }
        boolean success = objectTypeService.addObjectType(name, creator, remark);
        return ResponseResult.init(success);
    }

    @ApiOperation(value = "删除对象类型", response = Boolean.class)
    @ApiResponses(
            {@ApiResponse(code = 200, message = "successful response")})
    @RequestMapping(value = BigDataPath.TYPE_DELETE, method = RequestMethod.POST)
    public ResponseResult<Boolean> deleteObjectType(@RequestBody @ApiParam(value = "对象类型") ObjectType objectType) {
        String id;
        if (objectType != null) {
            id = objectType.getId();
        } else {
            return null;
        }

        boolean success = objectTypeService.deleteObjectType(id);
        return ResponseResult.init(success);
    }

    @ApiOperation(value = "修改对象类型", response = Boolean.class)
    @ApiResponses(
            {@ApiResponse(code = 200, message = "successful response")})
    @RequestMapping(value = BigDataPath.TYPE_UPDATE, method = RequestMethod.POST)
    public ResponseResult<Boolean> updateObjectType(@RequestBody @ApiParam(value = "对象类型") ObjectType objectType) {
        String id;
        String name;
        String creator;
        String remark;
        if (objectType != null) {
            id = objectType.getId();
            name = objectType.getName();
            creator = objectType.getCreator();
            remark = objectType.getRemark();
        } else {
            return null;
        }
        boolean success = objectTypeService.updateObjectType(id, name, creator, remark);
        return ResponseResult.init(success);
    }

    @ApiOperation(value = "查询对象类型", response = List.class, responseContainer = "List")
    @ApiResponses(
            {@ApiResponse(code = 200, message = "successful response")})
    @RequestMapping(value = BigDataPath.TYPE_SEARCH, method = RequestMethod.POST)
    public ResponseResult<List<Map<String, String>>> searchObjectType(@RequestBody @ApiParam(value = "对象类型") ObjectType objectType) {
        String name;
        int pageIndex;
        int pageSize;
        if (objectType != null) {
            name = objectType.getName();
            pageIndex = objectType.getPageIndex();
            pageSize = objectType.getPageSize();
        } else {
            return null;
        }
        List<Map<String, String>> mapList = objectTypeService.searchObjectType(name, pageIndex, pageSize);
        return ResponseResult.init(mapList);
    }
}
