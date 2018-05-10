package com.hzgc.service.starepo.controller;

import com.hzgc.service.starepo.bean.ObjectSearchResult;
import com.hzgc.service.starepo.bean.PSearchArgsModel;
import com.hzgc.service.starepo.bean.SearchRecordOpts;
import com.hzgc.service.starepo.service.ObjectInfoHandlerService;
import com.hzgc.service.starepo.bean.ObjectInfoHandler;
import com.hzgc.service.util.response.ResponseResult;
import com.hzgc.service.util.rest.BigDataPath;
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
@RequestMapping(value = BigDataPath.STAREPO, consumes = "application/json", produces = "application/json")
@Api(value = "objectInfo", tags = "对象信息服务")
public class ObjectInfoHandlerController {

    @Autowired
    private ObjectInfoHandlerService objectInfoHandlerService;

    @ApiOperation(value = "添加对象信息", response = Integer.class)
    @ApiResponses(
            {@ApiResponse(code = 200, message = "successful response")})
    @RequestMapping(value = BigDataPath.STAREPO_ADD, method = RequestMethod.POST)
    public ResponseResult<Integer> addObjectInfo(@RequestBody @ApiParam(value = "对象信息") ObjectInfoHandler objectInfoHandler) {
        String platformId;
        Map <String, Object> personObject;
        if (objectInfoHandler != null) {
            platformId = objectInfoHandler.getPlatformId();
            personObject = objectInfoHandler.getPersonObject();
        } else {
            return null;
        }
        Integer succeed = objectInfoHandlerService.addObjectInfo(platformId, personObject);
        return ResponseResult.init(succeed);
    }

    @ApiOperation(value = "删除对象信息", response = Integer.class)
    @RequestMapping(value = BigDataPath.STAREPO_DELETE, method = RequestMethod.DELETE)
    public ResponseResult <Integer> deleteObjectInfo(@RequestBody @ApiParam(value = "对象信息") ObjectInfoHandler objectInfoHandler) {
        List <String> rowkeys;
        if (objectInfoHandler != null) {
            rowkeys = objectInfoHandler.getRowkeys();
        } else {
            return null;
        }
        Integer succeed = objectInfoHandlerService.deleteObjectInfo(rowkeys);
        return ResponseResult.init(succeed);
    }

    @ApiOperation(value = "修改对象信息", response = Integer.class)
    @RequestMapping(value = BigDataPath.STAREPO_UPDATE, method = RequestMethod.POST)
    public ResponseResult <Integer> updateObjectInfo(@RequestBody @ApiParam(value = "对象信息") ObjectInfoHandler objectInfoHandler) {
        Map <String, Object> personObject;
        if (objectInfoHandler != null) {
            personObject = objectInfoHandler.getPersonObject();
        } else {
            return null;
        }
        Integer succeed = objectInfoHandlerService.updateObjectInfo(personObject);
        return ResponseResult.init(succeed);
    }

    @ApiOperation(value = "通过rowKey查找", response = ObjectSearchResult.class)
    @RequestMapping(value = BigDataPath.STAREPO_SEARCH_BYROWKEY, method = RequestMethod.POST)
    public ResponseResult <ObjectSearchResult> searchByRowkey(@RequestBody @ApiParam(value = "对象信息") ObjectInfoHandler objectInfoHandler) {
        String rowkey;
        if (objectInfoHandler != null) {
            rowkey = objectInfoHandler.getRowkey();
        } else {
            return null;
        }
        ObjectSearchResult result = objectInfoHandlerService.searchByRowkey(rowkey);
        return ResponseResult.init(result);
    }

    @ApiOperation(value = "获得对象信息", response = ObjectSearchResult.class)
    @RequestMapping(value = BigDataPath.STAREPO_GET_OBJECTINFO, method = RequestMethod.POST)
    public ResponseResult <ObjectSearchResult> getObjectInfo(@RequestBody @ApiParam(value = "对象信息") ObjectInfoHandler objectInfoHandler) {
        PSearchArgsModel pSearchArgsModel;
        if (objectInfoHandler != null) {
            pSearchArgsModel = objectInfoHandler.getPSearchArgsModel();
        } else {
            return null;
        }
        ObjectSearchResult result = objectInfoHandlerService.getObjectInfo(pSearchArgsModel);
        return ResponseResult.init(result);
    }

    @ApiOperation(value = "获得对象信息", response = Byte.class)
    @RequestMapping(value = BigDataPath.STAREPO_GET_PHOTOBYKEY, method = RequestMethod.POST)
    public ResponseResult <Byte> getPhotoByKey(@RequestBody @ApiParam(value = "对象信息") ObjectInfoHandler objectInfoHandler) {
        String rowkey;
        if (objectInfoHandler != null) {
            rowkey = objectInfoHandler.getRowkey();
        } else {
            return null;
        }
        Byte photo = objectInfoHandlerService.getPhotoByKey(rowkey);
        return ResponseResult.init(photo);
    }

    @ApiOperation(value = "获得记录的对象信息", response = ObjectSearchResult.class)
    @RequestMapping(value = BigDataPath.STAREPO_GETSEARCHRESULT, method = RequestMethod.POST)
    public ResponseResult <ObjectSearchResult> getRocordOfObjectInfo(@RequestBody @ApiParam(value = "对象信息") ObjectInfoHandler objectInfoHandler) {
        SearchRecordOpts searchRecordOpts;
        if (objectInfoHandler != null) {
            searchRecordOpts = objectInfoHandler.getSearchRecordOpts();
        } else {
            return null;
        }
        ObjectSearchResult result = objectInfoHandlerService.getRocordOfObjectInfo(searchRecordOpts);
        return ResponseResult.init(result);
    }

    @ApiOperation(value = "查询图片", response = byte[].class)
    @RequestMapping(value = BigDataPath.STAREPO_GETSEARCHPHOTO, method = RequestMethod.POST)
    public ResponseResult <byte[]> getSearchPhoto(@RequestBody @ApiParam(value = "对象信息") ObjectInfoHandler objectInfoHandler) {
        String rowkey;
        if (objectInfoHandler != null) {
            rowkey = objectInfoHandler.getRowkey();
        } else {
            return null;
        }
        byte[] bytes = objectInfoHandlerService.getSearchPhoto(rowkey);
        return ResponseResult.init(bytes);
    }
}
