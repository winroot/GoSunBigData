package com.hzgc.service.address.controller;

import com.hzgc.common.service.error.RestErrorCode;
import com.hzgc.common.service.response.ResponseResult;
import com.hzgc.common.service.rest.BigDataPath;
import com.hzgc.service.address.service.FtpSubscriptionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@Api(tags = "抓拍订阅服务")
@Slf4j
public class FtpSubscriptionController {

    @Autowired
    private FtpSubscriptionService ftpSubscriptionService;

    @ApiOperation(value = "打开抓拍订阅")
    @RequestMapping(value = BigDataPath.FTP_SUBSCRIPTION_OPEN, method = RequestMethod.POST)
    public ResponseResult<Boolean> openFtpSubscription(String userId, @RequestBody List<String> ipcIdList) throws IOException {
        if (StringUtils.isBlank(userId) || ipcIdList.isEmpty()) {
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
        }
        boolean bb = ftpSubscriptionService.openFtpSubscription(userId, ipcIdList);
        return ResponseResult.init(bb);
    }

    @ApiOperation(value = "关闭抓拍订阅")
    @RequestMapping(value = BigDataPath.FTP_SUBSCRIPTION_CLOSE, method = RequestMethod.POST)
    public ResponseResult<Boolean> closeFtpSubscription(String userId) {
        if (StringUtils.isBlank(userId)) {
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
        }
        boolean bb = ftpSubscriptionService.closeFtpSubscription(userId);
        return ResponseResult.init(bb);
    }
}
