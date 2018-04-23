package com.hzgc.service.starepo.controller;

import com.hzgc.common.service.BigDataPath;
import com.hzgc.common.service.ResponseResult;
import com.hzgc.service.starepo.object.ObjectSearchResult;
import com.hzgc.service.starepo.object.PSearchArgsModel;
import com.hzgc.service.starepo.object.SearchRecordOpts;
import com.hzgc.service.starepo.service.ObjectInfoHandlerImpl;
import com.hzgc.service.starepo.vo.ObjectInfoHandlerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@FeignClient(name = "staRepo")
@RequestMapping(value = BigDataPath.STAREPO)
public class ObjectInfoHandlerController {

    @Autowired
    private ObjectInfoHandlerImpl objectInfoHandlerService;

    @RequestMapping(value = BigDataPath.STAREPO_ADD)
    public ResponseResult addObjectInfo(ObjectInfoHandlerVO objectInfoHandlerVO) {
        String platformId;
        Map<String, Object> personObject;
        if (objectInfoHandlerVO != null) {
            platformId = objectInfoHandlerVO.getPlatformId();
            personObject = objectInfoHandlerVO.getPersonObject();
        } else {
            return null;
        }
        byte succeed = objectInfoHandlerService.addObjectInfo(platformId, personObject);
        return ResponseResult.init(succeed);
    }

    @RequestMapping(value = BigDataPath.STAREPO_DELETE)
    public ResponseResult deleteObjectInfo(ObjectInfoHandlerVO objectInfoHandlerVO) {
        List<String> rowkeys;
        if (objectInfoHandlerVO != null) {
            rowkeys = objectInfoHandlerVO.getRowkeys();
        } else {
            return null;
        }
        int succeed = objectInfoHandlerService.deleteObjectInfo(rowkeys);
        return ResponseResult.init(succeed);
    }

    @RequestMapping(value = BigDataPath.STAREPO_UPDATE)
    public ResponseResult updateObjectInfo(ObjectInfoHandlerVO objectInfoHandlerVO) {
        Map<String, Object> personObject;
        if (objectInfoHandlerVO != null) {
            personObject = objectInfoHandlerVO.getPersonObject();
        } else {
            return null;
        }
        int succeed = objectInfoHandlerService.updateObjectInfo(personObject);
        return ResponseResult.init(succeed);
    }

    @RequestMapping(value = BigDataPath.STAREPO_SEARCH_BYROWKEY)
    public ResponseResult searchByRowkey(ObjectInfoHandlerVO objectInfoHandlerVO) {
        String rowkey;
        if (objectInfoHandlerVO != null) {
            rowkey = objectInfoHandlerVO.getRowkey();
        } else {
            return null;
        }
        ObjectSearchResult result = objectInfoHandlerService.searchByRowkey(rowkey);
        return ResponseResult.init(result);
    }

    @RequestMapping(value = BigDataPath.STAREPO_GET_OBJECTINFO)
    public ResponseResult getObjectInfo(ObjectInfoHandlerVO objectInfoHandlerVO) {
        PSearchArgsModel pSearchArgsModel;
        if (objectInfoHandlerVO != null) {
            pSearchArgsModel = objectInfoHandlerVO.getpSearchArgsModel();
        } else {
            return null;
        }
        ObjectSearchResult result = objectInfoHandlerService.getObjectInfo(pSearchArgsModel);
        return ResponseResult.init(result);
    }

    @RequestMapping(value = BigDataPath.STAREPO_GET_PHOTOBYKEY)
    public ResponseResult getPhotoByKey(ObjectInfoHandlerVO objectInfoHandlerVO) {
        String rowkey;
        if (objectInfoHandlerVO != null) {
            rowkey = objectInfoHandlerVO.getRowkey();
        } else {
            return null;
        }
        byte[] photo = objectInfoHandlerService.getPhotoByKey(rowkey);
        return ResponseResult.init(photo);
    }

    @RequestMapping(value = BigDataPath.STAREPO_GETSEARCHRESULT)
    public ResponseResult getRocordOfObjectInfo(ObjectInfoHandlerVO objectInfoHandlerVO) {
        SearchRecordOpts searchRecordOpts;
        if (objectInfoHandlerVO != null) {
            searchRecordOpts = objectInfoHandlerVO.getSearchRecordOpts();
        } else {
            return null;
        }
        ObjectSearchResult result = objectInfoHandlerService.getRocordOfObjectInfo(searchRecordOpts);
        return ResponseResult.init(result);
    }

    @RequestMapping(value = BigDataPath.STAREPO_GETSEARCHPHOTO)
    public ResponseResult getSearchPhoto(ObjectInfoHandlerVO objectInfoHandlerVO) {
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
