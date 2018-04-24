package com.hzgc.collect.expand.processer;


import com.hzgc.collect.expand.conf.CommonConf;
import com.hzgc.collect.expand.log.DataProcessLogWriter;
import com.hzgc.collect.expand.log.LogEvent;
import com.hzgc.collect.expand.util.KafkaProperties;
import com.hzgc.collect.expand.util.ProducerKafka;
import com.hzgc.common.ftp.FtpUtils;
import com.hzgc.common.ftp.Sharpness;
import com.hzgc.common.ftp.faceobj.FaceObject;
import com.hzgc.common.ftp.message.FtpPathMessage;
import com.hzgc.common.ftp.properties.CollectProperties;
import com.hzgc.common.util.searchtype.SearchType;
import com.hzgc.jni.FaceAttribute;
import com.hzgc.jni.FaceFunction;

import java.util.concurrent.BlockingQueue;

public class ProcessThread implements Runnable {

    private BlockingQueue<LogEvent> queue;
    private DataProcessLogWriter writer;

    public ProcessThread(CommonConf conf, BlockingQueue<LogEvent> queue, String queueID) {
        this.queue = queue;
        writer = new DataProcessLogWriter(conf, queueID);
    }

    @Override
    public void run() {
        LogEvent event;
        try {
            while ((event = queue.take()) != null) {
                Sharpness sharpness = CollectProperties.getSharpness();
                System.out.println(sharpness.getWeight());
                System.out.println(sharpness.getHeight());
                FaceAttribute attribute =
                        FaceFunction.featureExtract(event.getAbsolutePath(), sharpness.getWeight(), sharpness.getHeight());
                FtpPathMessage message = FtpUtils.getFtpPathMessage(event.getRelativePath());
                if (attribute.getFeature() != null) {
                    FaceObject faceObject = new FaceObject(message.getIpcid()
                            , message.getTimeStamp()
                            , SearchType.PERSON
                            , message.getDate()
                            , message.getTimeslot()
                            , attribute
                            , event.getTimeStamp() + "");
                    ProcessCallBack callBack = new ProcessCallBack(event.getFtpPath(),
                            System.currentTimeMillis());
                    ProducerKafka.getInstance().sendKafkaMessage(
                            KafkaProperties.getTopicFeature(),
                            event.getFtpPath(),
                            faceObject,
                            callBack);
                    writer.countCheckAndWrite(event);
                } else {
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
