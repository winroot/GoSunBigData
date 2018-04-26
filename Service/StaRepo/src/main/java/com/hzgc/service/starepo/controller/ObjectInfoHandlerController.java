package com.hzgc.service.starepo.controller;

import com.hzgc.common.service.BigDataPath;
import com.hzgc.common.service.ResponseResult;
import com.hzgc.jni.FaceAttribute;
import com.hzgc.service.starepo.object.ObjectSearchResult;
import com.hzgc.service.starepo.object.PSearchArgsModel;
import com.hzgc.service.starepo.object.SearchRecordOpts;
import com.hzgc.service.starepo.service.ObjectInfoHandlerImpl;
import com.hzgc.service.starepo.vo.ObjectInfoHandlerVO;
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
@Api(value = "staRepo", tags = "静态库")
public class ObjectInfoHandlerController {

    @Autowired
    private ObjectInfoHandlerImpl objectInfoHandlerService;

    @ApiOperation(value = "添加对象信息", response = Byte.class)
    @ApiResponses(
            {@ApiResponse(code = 200, message = "successful response")})
    @RequestMapping(value = BigDataPath.STAREPO_ADD, method = RequestMethod.POST)
    public ResponseResult <Byte> addObjectInfo(@RequestBody @ApiParam(value = "对象信息封装类") ObjectInfoHandlerVO objectInfoHandlerVO) {
        String platformId;
        Map <String, Object> personObject;
        if (objectInfoHandlerVO != null) {
            platformId = objectInfoHandlerVO.getPlatformId();
            personObject = objectInfoHandlerVO.getPersonObject();
        } else {
            return null;
        }
        Byte succeed = objectInfoHandlerService.addObjectInfo(platformId, personObject);
        return ResponseResult.init(succeed);
    }

    @ApiOperation(value = "删除对象信息", response = Integer.class)
    @RequestMapping(value = BigDataPath.STAREPO_DELETE, method = RequestMethod.DELETE)
    public ResponseResult <Integer> deleteObjectInfo(@RequestBody @ApiParam(value = "对象信息封装类") ObjectInfoHandlerVO objectInfoHandlerVO) {
        List <String> rowkeys;
        if (objectInfoHandlerVO != null) {
            rowkeys = objectInfoHandlerVO.getRowkeys();
        } else {
            return null;
        }
        Integer succeed = objectInfoHandlerService.deleteObjectInfo(rowkeys);
        return ResponseResult.init(succeed);
    }

    @ApiOperation(value = "修改对象信息", response = Integer.class)
    @RequestMapping(value = BigDataPath.STAREPO_UPDATE, method = RequestMethod.POST)
    public ResponseResult <Integer> updateObjectInfo(@RequestBody @ApiParam(value = "对象信息封装类") ObjectInfoHandlerVO objectInfoHandlerVO) {
        Map <String, Object> personObject;
        if (objectInfoHandlerVO != null) {
            personObject = objectInfoHandlerVO.getPersonObject();
        } else {
            return null;
        }
        Integer succeed = objectInfoHandlerService.updateObjectInfo(personObject);
        return ResponseResult.init(succeed);
    }

    @ApiOperation(value = "通过rowKey查找", response = ObjectSearchResult.class)
    @RequestMapping(value = BigDataPath.STAREPO_SEARCH_BYROWKEY, method = RequestMethod.POST)
    public ResponseResult <ObjectSearchResult> searchByRowkey(@RequestBody @ApiParam(value = "对象信息封装类") ObjectInfoHandlerVO objectInfoHandlerVO) {
        String rowkey;
        if (objectInfoHandlerVO != null) {
            rowkey = objectInfoHandlerVO.getRowkey();
        } else {
            return null;
        }
        ObjectSearchResult result = objectInfoHandlerService.searchByRowkey(rowkey);
        return ResponseResult.init(result);
    }

    @ApiOperation(value = "获得对象信息", response = ObjectSearchResult.class)
    @RequestMapping(value = BigDataPath.STAREPO_GET_OBJECTINFO, method = RequestMethod.POST)
    public ResponseResult <ObjectSearchResult> getObjectInfo(@RequestBody @ApiParam(value = "对象信息封装类") ObjectInfoHandlerVO objectInfoHandlerVO) {
        PSearchArgsModel pSearchArgsModel;
        if (objectInfoHandlerVO != null) {
            pSearchArgsModel = objectInfoHandlerVO.getpSearchArgsModel();
        } else {
            return null;
        }
        ObjectSearchResult result = objectInfoHandlerService.getObjectInfo(pSearchArgsModel);
        return ResponseResult.init(result);
    }

    @ApiOperation(value = "获得对象信息", response = byte[].class)
    @RequestMapping(value = BigDataPath.STAREPO_GET_PHOTOBYKEY, method = RequestMethod.POST)
    public ResponseResult <byte[]> getPhotoByKey(@RequestBody @ApiParam(value = "对象信息封装类") ObjectInfoHandlerVO objectInfoHandlerVO) {
        String rowkey;
        if (objectInfoHandlerVO != null) {
            rowkey = objectInfoHandlerVO.getRowkey();
        } else {
            return null;
        }
        byte[] photo = objectInfoHandlerService.getPhotoByKey(rowkey);
        return ResponseResult.init(photo);
    }

    @ApiOperation(value = "获得记录的对象信息", response = ObjectSearchResult.class)
    @RequestMapping(value = BigDataPath.STAREPO_GETSEARCHRESULT, method = RequestMethod.POST)
    public ResponseResult <ObjectSearchResult> getRocordOfObjectInfo(@RequestBody @ApiParam(value = "对象信息封装类") ObjectInfoHandlerVO objectInfoHandlerVO) {
        SearchRecordOpts searchRecordOpts;
        if (objectInfoHandlerVO != null) {
            searchRecordOpts = objectInfoHandlerVO.getSearchRecordOpts();
        } else {
            return null;
        }
        ObjectSearchResult result = objectInfoHandlerService.getRocordOfObjectInfo(searchRecordOpts);
        return ResponseResult.init(result);
    }

    @ApiOperation(value = "查询图片", response = byte[].class)
    @RequestMapping(value = BigDataPath.STAREPO_GETSEARCHPHOTO, method = RequestMethod.POST)
    public ResponseResult <byte[]> getSearchPhoto(@RequestBody @ApiParam(value = "对象信息封装类") ObjectInfoHandlerVO objectInfoHandlerVO) {
        String rowkey;
        if (objectInfoHandlerVO != null) {
            rowkey = objectInfoHandlerVO.getRowkey();
        } else {
            return null;
        }
        byte[] bytes = objectInfoHandlerService.getSearchPhoto(rowkey);
        return ResponseResult.init(bytes);
    }
}
