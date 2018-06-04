package com.hzgc.service.starepo.controller;

import com.hzgc.common.util.empty.IsEmpty;
import com.hzgc.common.util.json.JSONUtil;
import com.hzgc.service.starepo.bean.param.ObjectTypeParam;
import com.hzgc.service.starepo.service.ObjectTypeService;
import com.hzgc.service.util.error.RestErrorCode;
import com.hzgc.service.util.response.ResponseResult;
import com.hzgc.service.util.rest.BigDataPath;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@Api(value = "/objectType", tags = "对象类型")
public class ObjectTypeController {

    @Autowired
    private ObjectTypeService objectTypeService;

    /**
     * 添加objectType
     *
     * @param objectTypeParam add objectType对象
     * @return boolean
     */
    @ApiOperation(value = "添加对象类型", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.TYPE_ADD, method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseResult<Boolean> addObjectType(@RequestBody @ApiParam(value = "对象类型") ObjectTypeParam objectTypeParam) {
        if (!ObjectTypeParam.ValidateParam_add(objectTypeParam)) {
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
        }
        boolean success = objectTypeService.addObjectType(objectTypeParam);
        return ResponseResult.init(success);
    }

    /**
     * 删除objectType
     *
     * @param objectTypeKeyList 类型Key列表
     * @return boolean
     */
    @ApiOperation(value = "删除对象类型", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.TYPE_DELETE, method = RequestMethod.DELETE)
    public ResponseResult<Boolean> deleteObjectType(@RequestBody @ApiParam(value = "对象类型key列表") List<String> objectTypeKeyList) {
        if (!IsEmpty.listIsRight(objectTypeKeyList)) {
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
        }
        boolean success = objectTypeService.deleteObjectType(objectTypeKeyList);
        return ResponseResult.init(success);
    }

    /**
     * 修改ObjectType
     *
     * @param objectTypeParam update objectType对象
     * @return boolean
     */
    @ApiOperation(value = "修改对象类型", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.TYPE_UPDATE, method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    public ResponseResult<Boolean> updateObjectType(@RequestBody @ApiParam(value = "对象类型") ObjectTypeParam objectTypeParam) {
        if (!ObjectTypeParam.ValidateParam_update(objectTypeParam)) {
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
        }
        boolean success = objectTypeService.updateObjectType(objectTypeParam);
        return ResponseResult.init(success);
    }

    /**
     * 查询objectType
     *
     * @param start 起始页码
     * @param limit 每页行数
     * @return List<ObjectTypeParam>
     */
    @ApiOperation(value = "查询对象类型", response = ResponseResult.class)
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "start", value = "起始行数", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "分页行数", dataType = "Integer", paramType = "query")
    })
    @RequestMapping(value = BigDataPath.TYPE_SEARCH, method = RequestMethod.GET)
    public ResponseResult<List<ObjectTypeParam>> searchObjectType(Integer start, Integer limit) {
        if (start == null || start == 0) {
            start = 1;
        }
        if (limit == null || limit == 0) {
            limit = 5;
        }
        List<ObjectTypeParam> objectTypeParamList = objectTypeService.searchObjectType(start, limit);
        return ResponseResult.init(objectTypeParamList);
    }

    /**
     * 查询objectTypeName
     *
     * @param objectTypeKeys 对象类型key数组
     * @return Map
     */
    @ApiOperation(value = "查询对象类型名称", response = ResponseResult.class)
    @ApiImplicitParam(name = "objectTypeKeys", value = "对象类型key数组", dataType = "List", paramType = "query")
    @RequestMapping(value = BigDataPath.TYPE_SEARCH_NAMES, method = RequestMethod.POST)
    public ResponseResult<Map> searchObjectTypeNames(@RequestBody List<String> objectTypeKeys) {
        if (objectTypeKeys == null || objectTypeKeys.size() <= 0) {
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
        }
        Map map = objectTypeService.searchObjectTypeNames(objectTypeKeys);
        return ResponseResult.init(map);
    }

    /**
     * 统计对象类型的数量
     *
     * @return int 对象类型的数量
     */
    @ApiOperation(value = "统计常住人口", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.TYPE_COUNT, method = RequestMethod.GET)
    public ResponseResult<Integer>objectTypeCount() {
        int count = objectTypeService.objectTypeCount();
        return ResponseResult.init(count);
    }
}
