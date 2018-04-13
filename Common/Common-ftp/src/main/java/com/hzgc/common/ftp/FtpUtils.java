package com.hzgc.common.ftp;

import com.hzgc.common.ftp.message.FtpPathMessage;
import com.hzgc.common.ftp.message.FtpUrlMessage;
import com.hzgc.common.ftp.properties.FTPAddressProperties;
import com.hzgc.common.util.empty.IsEmpty;
import org.apache.log4j.Logger;

import java.io.*;

public final class FtpUtils implements Serializable {

    private static Logger LOG = Logger.getLogger(FtpUtils.class);

    public static boolean checkPort(int checkPort) throws Exception {
        return checkPort > 1024;
    }

    public static ByteArrayOutputStream inputStreamCacher(InputStream is) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int len;
        try {
            while ((len = is.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return baos;
    }

    /**
     * 根据文件上传至ftp的绝对路径获取到ipcID、TimeStamp、Date、TimeSlot
     *
     * @param fileName 文件上传至ftp的绝对路径，例如：/3B0383FPAG51511/2017/05/23/16/00/2017_05_23_16_00_15_5704_0.jpg
     * @return 设备、时间等信息 例如：{date=2017-05-23, sj=1600, ipcID=3B0383FPAG51511, time=2017-05-23 16:00:15}
     */
    public static FtpPathMessage getFtpPathMessage(String fileName) {
        FtpPathMessage message = new FtpPathMessage();
        String ipcID = fileName.substring(1, fileName.indexOf("/", 1));
        String timeStr = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.lastIndexOf("_")).replace("_", "");

        String year = timeStr.substring(0, 4);
        String month = timeStr.substring(4, 6);
        String day = timeStr.substring(6, 8);
        String hour = timeStr.substring(8, 10);
        String minute = timeStr.substring(10, 12);
        String second = timeStr.substring(12, 14);

        StringBuilder time = new StringBuilder();
        time = time.append(year).append("-").append(month).append("-").append(day).
                append(" ").append(hour).append(":").append(minute).append(":").append(second);
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        StringBuilder date = new StringBuilder();
        date = date.append(year).append("-").append(month).append("-").append(day);

        StringBuilder sj = new StringBuilder();
        sj = sj.append(hour).append(minute);

        try {
            /*Date date = sdf.parse(time.toString());
            long timeStamp = date.getTime();*/
            message.setIpcid(ipcID);
            message.setTimeStamp(time.toString());
            message.setDate(date.toString());
            message.setTimeslot(sj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    /**
     * 根据ftpUrl获取到IP、HostName、ipcID、TimeStamp、Date、TimeSlot
     *
     * @param ftpUrl ftp地址 例如：ftp://172.18.18.109:2121/ABCVS20160823CCCH/2017_11_09_10_53_35_2_0.jpg
     * @return 设备、时间等信息 例如：{date=2017-11-09, filepath=/ABCVS20160823CCCH/2017_11_09_10_53_35_2_0.jpg, port=2121, ip=172.18.18.109
     * , timeslot=1053, ipcid=ABCVS20160823CCCH, timestamp=2017-11-09 10:53:35}
     */
    public static FtpUrlMessage getFtpUrlMessage(String ftpUrl) {
        FtpUrlMessage message = new FtpUrlMessage();
        String ip = ftpUrl.substring(ftpUrl.indexOf(":") + 3, ftpUrl.lastIndexOf(":"));
        String portStr = ftpUrl.substring(ftpUrl.lastIndexOf(":") + 1);
        String port = portStr.substring(0, portStr.indexOf("/"));
        String filePath = portStr.substring(portStr.indexOf("/"));
        FtpPathMessage pathMessage = getFtpPathMessage(filePath);
        if (pathMessage != null) {
            message.setIpcid(pathMessage.getIpcid());
            message.setTimeStamp(pathMessage.getTimeStamp());
            message.setDate(pathMessage.getDate());
            message.setTimeslot(pathMessage.getTimeslot());
            message.setIp(ip);
            message.setPort(port);
            message.setFilePath(filePath);
        }
        return message;
    }

    /**
     * 小图ftpUrl转大图ftpUrl
     *
     * @param surl 小图ftpUrl
     * @return 大图ftpUrl
     */
    public static String surlToBurl(String surl) {
        StringBuilder burl = new StringBuilder();
        String s1 = surl.substring(0, surl.lastIndexOf("_") + 1);
        String s2 = surl.substring(surl.lastIndexOf("."));
        burl.append(s1).append(0).append(s2);
        return burl.toString();
    }

    /**
     * ftpUrl中的HostName转为IP
     *
     * @param ftpUrl 带HostName的ftpUrl
     * @return 带IP的ftpUrl
     */
    public static String getFtpUrl(String ftpUrl) {
        String hostName = ftpUrl.substring(ftpUrl.indexOf("/") + 2, ftpUrl.lastIndexOf(":"));
        String ftpServerIP = FTPAddressProperties.getIpByHostName(hostName);
        if (IsEmpty.strIsRight(ftpServerIP)) {
            return ftpUrl.replace(hostName, ftpServerIP);
        }
        return ftpUrl;
    }
}
