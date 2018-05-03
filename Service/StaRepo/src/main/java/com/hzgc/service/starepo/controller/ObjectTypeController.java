package com.hzgc.service.starepo.controller;

import com.hzgc.common.service.BigDataPath;
import com.hzgc.common.service.ResponseResult;
import com.hzgc.service.starepo.service.ObjectTypeServiceImpl;
import com.hzgc.service.starepo.vo.ObjectTypeVO;
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
@FeignClient(name = "type")
@RequestMapping(value = BigDataPath.TYPE, consumes = "application/json", produces = "application/json")
@Api(value = "type", tags = "对象类型")
public class ObjectTypeController {

    @Autowired
    private ObjectTypeServiceImpl objectTypeServiceImpl;

    @ApiOperation(value = "添加对象类型", response = Byte.class)
    @ApiResponses(
            {@ApiResponse(code = 200, message = "successful response")})
    @RequestMapping(value = BigDataPath.TYPE_ADD, method = RequestMethod.POST)
    public ResponseResult<Boolean> addObjectType(@RequestBody @ApiParam(value = "对象类型封装类") ObjectTypeVO vo) {
        if(vo == null || vo.getName() == null || "".equals(vo.getName())){
            return null;
        }

        boolean success = objectTypeServiceImpl.addObjectType(vo.getName(), vo.getCreator(), vo.getRemark());
        return ResponseResult.init(success);
    }

    @ApiOperation(value = "删除对象类型", response = Byte.class)
    @ApiResponses(
            {@ApiResponse(code = 200, message = "successful response")})
    @RequestMapping(value = BigDataPath.TYPE_DELETE, method = RequestMethod.POST)
    public ResponseResult<Boolean> deleteObjectType(@RequestBody @ApiParam(value = "对象类型封装类") ObjectTypeVO vo) {
        if(vo == null || vo.getId() == null || "".equals(vo.getId())){
            return null;
        }

        boolean success = objectTypeServiceImpl.deleteObjectType(vo.getId());
        return ResponseResult.init(success);
    }

    @ApiOperation(value = "修改对象类型", response = Byte.class)
    @ApiResponses(
            {@ApiResponse(code = 200, message = "successful response")})
    @RequestMapping(value = BigDataPath.TYPE_UPDATE, method = RequestMethod.POST)
    public ResponseResult<Boolean> updateObjectType(@RequestBody @ApiParam(value = "对象类型封装类") ObjectTypeVO vo) {
        if(vo == null || vo.getId() == null || vo.getName() == null || "".equals(vo.getId()) || "".equals(vo.getName())){
            return null;
        }

        boolean success = objectTypeServiceImpl.updateObjectType(vo.getId(), vo.getName(), vo.getCreator(), vo.getRemark());
        return ResponseResult.init(success);
    }

    @ApiOperation(value = "查询对象类型", response = Byte.class)
    @ApiResponses(
            {@ApiResponse(code = 200, message = "successful response")})
    @RequestMapping(value = BigDataPath.TYPE_SEARCH, method = RequestMethod.POST)
    public ResponseResult<List> searchObjectType(@RequestBody @ApiParam(value = "对象类型封装类") ObjectTypeVO vo) {
        if(vo == null || vo.getPageIndex() == 0 || vo.getPageSize() == 0){
            return null;
        }

        List<Map<String, String>> success = objectTypeServiceImpl.searchObjectType(vo.getName(), vo.getPageIndex(), vo.getPageSize());
        return ResponseResult.init(success);
    }
}
