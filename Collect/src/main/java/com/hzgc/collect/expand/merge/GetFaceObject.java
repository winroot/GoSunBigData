package com.hzgc.collect.expand.merge;

import com.hzgc.collect.expand.log.LogEvent;
import com.hzgc.collect.expand.processer.FaceObject;
import com.hzgc.common.ftp.FtpPathMessage;
import com.hzgc.common.ftp.FtpUtils;
import com.hzgc.common.json.JSONUtil;
import com.hzgc.dubbo.dynamicrepo.SearchType;
import com.hzgc.jni.FaceAttribute;
import com.hzgc.jni.FaceFunction;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

class GetFaceObject implements Serializable {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static FaceObject getFaceObject(String row) {
        FaceObject faceObject = null;
        if (row != null && row.length() != 0) {
            LogEvent event = JSONUtil.toObject(row, LogEvent.class);
            // 路径中不包含/opt/ftpdata
            String portPath = event.getFtpPath();
            String path = portPath.split("://")[1].substring(portPath.split("://")[1].indexOf("/"));
            // 路径中包含/opt/ftpdata/
            String absolutePath = event.getAbsolutePath();

            byte[] photo = FaceFunction.getPictureBytes(absolutePath);
            if (photo != null) {
                FaceAttribute faceAttribute = FaceFunction.featureExtract(photo);
                FtpPathMessage ftpPathMessage = FtpUtils.getFtpPathMessage(path);
                String ipcId = ftpPathMessage.getIpcid();
                String timeStamp = ftpPathMessage.getTimeStamp();
                String timeSlot = ftpPathMessage.getTimeslot();
                String date = ftpPathMessage.getDate();
                SearchType type = SearchType.PERSON;
                String startTime = sdf.format(new Date());
                faceObject = new FaceObject(ipcId, timeStamp, type, date, timeSlot, faceAttribute, startTime);
            }
        }
        return faceObject;
    }
}
