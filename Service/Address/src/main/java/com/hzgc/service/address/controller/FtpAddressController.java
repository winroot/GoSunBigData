package com.hzgc.service.address.controller;

import com.hzgc.common.service.BigDataPath;
import com.hzgc.common.service.ResponseResult;
import com.hzgc.service.address.service.FtpAddressService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Properties;

@RestController
@FeignClient(name = "ftp")
@RequestMapping(value = BigDataPath.FTP, consumes = "application/json", produces = "application/json")
@Api(value = "/ftp", tags = "ftp地址服务")
public class FtpAddressController {

    @Autowired
    private FtpAddressService ftpAddressService;

    /**
     * 获取Ftp相关配置参数
     *
     * @return ftp相关配置参数
     */
    @ApiOperation(value = "ftp配置信息查询", response = Properties.class, responseContainer = "List")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "successful response")})
    @RequestMapping(value = BigDataPath.FTP_GET_PROPERTIES, method = RequestMethod.GET)
    public ResponseResult<Properties> getFtpAddress() {
        Properties properties = ftpAddressService.getProperties();
        return ResponseResult.init(properties);
    }

    /**
     * 通过主机名获取FTP的IP地址
     *
     * @param hostname 主机名
     * @return IP地址
     */
    @ApiOperation(value = "ftp配置信息查询", response = String.class, responseContainer = "List")
    @ApiImplicitParam(name = "hostname", value = "主机名", required = true, dataType = "text", paramType = "form")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "successful response")})
    @RequestMapping(value = BigDataPath.FTP_GET_IP, method = RequestMethod.GET)
    public ResponseResult<String> getIPAddress(@ApiParam(value = "聚类信息查询入参") String hostname) {
        String ip = ftpAddressService.getIPAddress(hostname);
        return ResponseResult.init(ip);
    }
}
