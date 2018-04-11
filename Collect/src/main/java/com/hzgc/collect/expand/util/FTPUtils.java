package com.hzgc.collect.expand.util;

import com.hzgc.collect.FTP;
import com.hzgc.common.util.ip.IPAddressUtils;

import java.lang.management.ManagementFactory;
import java.util.Map;

public class FTPUtils {

    /**
     * @param pictureName determine the picture type based on the file name
     * @return equals 0, it is a picture
     * lager than 0, it is a face picture
     */
    public static int pickPicture(String pictureName) {
        int picType = 0;
        if (null != pictureName) {
            String tmpStr = pictureName.substring(pictureName.lastIndexOf("_") + 1, pictureName.lastIndexOf("."));
            try {
                picType = Integer.parseInt(tmpStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return picType;
    }

    /**
     * 通过上传文件路径解析到文件的ftp地址（ftp发送至kafka的key）
     *
     * @param filePath ftp接收数据路径
     * @return 文件的ftp地址
     */
    public static String filePath2FtpUrl(String filePath) {
        StringBuilder url = new StringBuilder();
        String hostName = IPAddressUtils.getHostName();
        Map<Integer, Integer> ftpPIDMap = FTP.getPidMap();
        if (!ftpPIDMap.isEmpty()) {
            Integer ftpPID = Integer.valueOf(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
            //LOG.info("ftp PID = " + ftpPID);
            int ftpPort = ftpPIDMap.get(ftpPID);
            url = url.append("ftp://").append(hostName).append(":").append(ftpPort).append(filePath);
            return url.toString();
        }
        url = url.append("ftp://").append(hostName).append(":").append("none").append(filePath);
        return url.toString();
    }
}
