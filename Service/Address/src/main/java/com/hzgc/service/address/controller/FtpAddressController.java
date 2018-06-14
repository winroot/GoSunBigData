package com.hzgc.service.address.controller;

import com.hzgc.service.address.service.FtpAddressService;
import com.hzgc.service.util.error.RestErrorCode;
import com.hzgc.service.util.response.ResponseResult;
import com.hzgc.service.util.rest.BigDataPath;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Api(value = "/ftp", tags = "大数据地址服务")
public class FtpAddressController {

    @Autowired
    private FtpAddressService ftpAddressService;

    /**
     * 获取Ftp相关配置参数
     *
     * @return ftp相关配置参数
     */
    @ApiOperation(value = "获取可绑定ftp地址信息")
    @RequestMapping(value = BigDataPath.FTP_GET_PROPERTIES, method = RequestMethod.GET)
    public ResponseResult<Map<String, String>> getFtpAddress() {
        Map<String, String> map = ftpAddressService.getProperties();
        return ResponseResult.init(map);
    }

    /**
     * 通过主机名获取FTP的IP地址
     *
     * @param hostname 主机名
     * @return IP地址
     */
    @ApiOperation(value = "ftp服务器主机名转IP", response = String.class, responseContainer = "List")
    @ApiImplicitParam(name = "hostname", value = "主机名", required = true, dataType = "String",paramType = "query")
    @RequestMapping(value = BigDataPath.FTP_GET_IP, method = RequestMethod.GET)
    public ResponseResult<String> getIPAddress(@ApiParam(value = "主机名") String hostname) {
        if (null != hostname) {
            String ip = ftpAddressService.getIPAddress(hostname);
            return ResponseResult.init(ip);
        }
        return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
    }
}
