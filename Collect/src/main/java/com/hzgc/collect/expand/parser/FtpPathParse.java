package com.hzgc.collect.expand.parser;

import com.hzgc.collect.expand.util.CollectProperties;
import org.apache.commons.lang3.StringUtils;

public class FtpPathParse {
    private static Parser boxParser = new BoxParser();
    private static Parser daHuaParser_zhuaPaiJi = new DaHuaParser_ZhuaPaiJi();
    private static Parser daHuaParser_gongChengJi = new DaHuaParser_GongChengJi();

    public static boolean isParse(String fileName){
        if (fileName.contains(DeviceUtil.DaHua_ZhuaPaiJi)){
            return daHuaParser_zhuaPaiJi.canParse(fileName);
        } else if (fileName.contains(DeviceUtil.DaHua_GongChengJi)){
            return daHuaParser_gongChengJi.canParse(fileName);
        } else {
            return boxParser.canParse(fileName);
        }
    }

    public static FtpPathMetaData parse(String fileName) {
        if (fileName.contains(DeviceUtil.DaHua_ZhuaPaiJi)){
            return daHuaParser_zhuaPaiJi.parse(fileName);
        } else if (fileName.contains(DeviceUtil.DaHua_GongChengJi)){
            return daHuaParser_gongChengJi.parse(fileName);
        } else {
            return boxParser.parse(fileName);
        }
    }

    /**
     * ftpUrl中的HostName转为IP
     *
     * @param ftpUrl 带HostName的ftpUrl
     * @return 带IP的ftpUrl
     */
    public static String hostNameUrl2IpUrl(String ftpUrl) {
        String hostName = ftpUrl.substring(ftpUrl.indexOf("/") + 2, ftpUrl.lastIndexOf(":"));
        String ftpServerIP = CollectProperties.getFtpIp();
        if (!StringUtils.isBlank(ftpUrl)) {
            return ftpUrl.replace(hostName, ftpServerIP);
        }
        return ftpUrl;
    }

    /**
     * 通过上传文件路径解析到文件的ftp地址（ftp发送至kafka的key）
     * /xx/xx/xx => ftp://hostname/xx/xx/xx
     *
     * @param filePath ftp接收数据路径
     * @return 文件的ftp地址
     */
    public static String ftpPath2HostNameUrl(String filePath) {
        StringBuilder url = new StringBuilder();
        String hostName = CollectProperties.getHostname();
        url = url.append("ftp://").append(hostName).append(":").append(CollectProperties.getFtpPort()).append(filePath);
        return url.toString();
    }

    /**
     * 小图ftpUrl转大图ftpUrl
     *
     * @param surl 小图ftpUrl
     * @return 大图ftpUrl
     */
    public static String surlToBurl(String surl) {
        if (surl.contains(DeviceUtil.DaHua_ZhuaPaiJi)){
            return daHuaParser_zhuaPaiJi.surlToBurl(surl);
        } else if (surl.contains(DeviceUtil.DaHua_GongChengJi)){
            return daHuaParser_gongChengJi.surlToBurl(surl);
        } else {
            return boxParser.surlToBurl(surl);
        }
    }
}
