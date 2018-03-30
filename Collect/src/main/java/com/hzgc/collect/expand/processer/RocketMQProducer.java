package com.hzgc.collect.expand.processer;

import com.hzgc.collect.expand.util.RocketMQProperHelper;
import org.apache.log4j.Logger;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;

import java.io.Serializable;
import java.util.List;

public class RocketMQProducer implements Serializable {
    private static Logger LOG = Logger.getLogger(RocketMQProducer.class);
    private static String topic;
    private static RocketMQProducer instance = null;
    private DefaultMQProducer producer;

    private RocketMQProducer() {
        try {
            String namesrvAddr = RocketMQProperHelper.getAddress();
            topic = RocketMQProperHelper.getTopic();
            String producerGroup = RocketMQProperHelper.getGroup();
            if (null != namesrvAddr && namesrvAddr.length() > 0 &&
                    null != topic && topic.length() > 0 &&
                    null != producerGroup && producerGroup.length() > 0) {
                producer = new DefaultMQProducer(producerGroup);
                producer.setRetryTimesWhenSendFailed(4);
                producer.setRetryAnotherBrokerWhenNotStoreOK(true);
                producer.setNamesrvAddr(namesrvAddr);
                producer.start();
                LOG.info("producer started...");
            } else {
                LOG.error("parameter init error");
                throw new Exception("parameter init error");
            }
        } catch (Exception e) {
            LOG.error("producer init error...");
            throw new RuntimeException(e);
        }
    }

    public static RocketMQProducer getInstance() {
        if (instance == null) {
            synchronized (RocketMQProducer.class) {
                if (instance == null) {
                    instance = new RocketMQProducer();
                }
            }
        }
        return instance;
    }

    public SendResult send(byte[] data) {
        return send(topic, null, null, data, null);
    }

    public SendResult send(String tag, byte[] data) {
        return send(topic, tag, null, data, null);
    }

    public SendResult send(String tag, String key, byte[] data) {
        return send(topic, tag, key, data, null);
    }

    public SendResult send(String topic, String tag, String key, byte[] data, final MessageQueueSelector selector) {
        SendResult sendResult = null;
        try {
            Message msg;
            if (tag == null || tag.length() == 0) {
                msg = new Message(topic, data);
            } else if (key == null || key.length() == 0) {
                msg = new Message(topic, tag, data);
            } else {
                msg = new Message(topic, tag, key, data);
            }
            if (selector != null) {
                sendResult = producer.send(msg, new MessageQueueSelector() {
                    public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                        return selector.select(mqs, msg, arg);
                    }
                }, key);
            } else {
                sendResult = producer.send(msg);
            }
            LOG.info("Send RocketMQ successfully! message:[topic:" + msg.getTopic() + ", tag:" + msg.getTags() +
                    ", key:" + msg.getKeys() + ", data:" + new String(data) + "], " + sendResult);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("Send message error...");
        }
        return sendResult;
    }
}
