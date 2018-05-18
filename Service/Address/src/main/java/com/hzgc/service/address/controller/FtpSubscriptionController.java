package com.hzgc.service.address.controller;

import com.hzgc.service.address.service.FtpSubscriptionService;
import com.hzgc.service.util.rest.BigDataPath;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = BigDataPath.FTP_SUBSCRIPTION, consumes = "application/json", produces = "application/json")
@Api(value = "/ftpSubscription", tags = "人脸抓拍订阅功能")
public class FtpSubscriptionController {

    @Autowired
    private FtpSubscriptionService ftpSubscriptionService;

    /**
     * 打开人脸抓拍订阅功能
     *
     * @param userId    用户ID
     * @param ipcIdList 设备ID列表
     */
    @ApiOperation(value = "打开人脸抓拍订阅功能")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "successful response")})
    @RequestMapping(value = BigDataPath.FTP_SUBSCRIPTION_OPEN, method = RequestMethod.POST)
    public void openFtpReception(String userId, List<String> ipcIdList) {
        ftpSubscriptionService.openFtpReception(userId, ipcIdList);
    }

    /**
     * 关闭人脸抓拍订阅功能
     *
     * @param userId 用户ID
     */
    @ApiOperation(value = "关闭人脸抓拍订阅功能")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "successful response")})
    @RequestMapping(value = BigDataPath.FTP_SUBSCRIPTION_CLOSE, method = RequestMethod.POST)
    public void closeFtpReception(String userId) {
        ftpSubscriptionService.closeFtpReception(userId);
    }
}
