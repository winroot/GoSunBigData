package com.hzgc.collect.expand.processer;

import com.hzgc.collect.expand.receiver.Event;
import com.hzgc.collect.expand.util.CollectProperties;
import com.hzgc.collect.expand.util.SendMqMessage;
import com.hzgc.common.collect.bean.FaceObject;
import com.hzgc.common.collect.facesub.FtpSubscribeClient;
import com.hzgc.common.rocketmq.RocketMQProducer;
import com.hzgc.common.util.file.FileUtil;
import com.hzgc.common.util.json.JSONUtil;
import com.hzgc.jni.FaceAttribute;
import com.hzgc.jni.FaceFunction;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class ProcessThread implements Runnable {
    private Logger LOG = Logger.getLogger(ProcessThread.class);
    private BlockingQueue<Event> queue;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ProcessThread(BlockingQueue<Event> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        Event event;
        try {
            while ((event = queue.take()) != null) {
                byte[] bytes = FileUtil.fileToByteArray(event.getAbsolutePath());
                BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));
                if (image.getWidth() * image.getHeight() >= 1920 * 1080) {
                    LOG.error("Camera error, This is a big picture, fileName: " + event.getAbsolutePath());
                    continue;
                }
                if (CollectProperties.isFtpSubscribeSwitch()) {
                    // ftpSubscribeMap: key is ipcId, value is sessionIds
                    Map<String, List<String>> ftpSubscribeMap =FtpSubscribeClient.getSessionMap();
                    if (!ftpSubscribeMap.isEmpty()) {
                        if (ftpSubscribeMap.containsKey(event.getIpcId())) {
                            List<String> sessionIds = ftpSubscribeMap.get(event.getIpcId());
                            sendMQ(event, sessionIds);
                        }
                    }
                } else {
                    sendMQ(event);
                }
                FaceAttribute attribute = FaceFunction.featureExtract(bytes);
                if (attribute.getFeature() != null) {
                    FaceObject faceObject = FaceObject.builder()
                            .setIpcId(event.getIpcId())
                            .setTimeStamp(event.getTimeStamp())
                            .setDate(event.getDate())
                            .setTimeSlot(event.getTimeSlot())
                            .setAttribute(attribute)
                            .setStartTime(dateFormat.format(System.currentTimeMillis()))
                            .setSurl(event.getFtpHostNameUrlPath())
                            .setBurl(event.getBigPicurl())
                            .setHostname(CollectProperties.getHostname())
                            .setRelativePath(event.getRelativePath());
                    ProcessCallBack callBack = new ProcessCallBack(event.getFtpIpUrlPath(),
                            dateFormat.format(System.currentTimeMillis()));
                    String jsonObject = JSONUtil.toJson(faceObject);
                    ProducerKafka.getInstance().sendKafkaMessage(
                            CollectProperties.getKafkaFaceObjectTopic(),
                            event.getFtpHostNameUrlPath(),
                            jsonObject,
                            callBack);
                }else {
                    LOG.info("FaceAttribute values are not extracted, fileName: " + event.getAbsolutePath());
                }
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMQ(Event event) {
        RocketMQProducer.getInstance(CollectProperties.getRocketmqAddress(),
                CollectProperties.getRocketmqCaptureTopic(),
                CollectProperties.getRokcetmqCaptureGroup())
                .send(event.getIpcId(), event.getTimeStamp(), event.getFtpIpUrlPath().getBytes());
    }

    private void sendMQ(Event event, List<String> sessionIds) {
        SendMqMessage mqMessage = new SendMqMessage();
        mqMessage.setSessionIds(sessionIds);
        mqMessage.setFtpUrl(event.getFtpIpUrlPath());
        RocketMQProducer.getInstance(
                CollectProperties.getRocketmqAddress(),
                CollectProperties.getRocketmqCaptureTopic(),
                CollectProperties.getRokcetmqCaptureGroup())
                .send(event.getIpcId(), event.getTimeStamp(), JSONUtil.toJson(mqMessage).getBytes());
    }
}
