package com.hzgc.common.service.mq;

import com.hzgc.common.service.utils.ServerIdUtil;
import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MQPullConsumer;
import org.apache.rocketmq.client.consumer.MQPushConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * mq消费者工具类
 *
 * @author liuzhikun
 */
@Component
public class MQConsumerUtil {
    private static MQDefaultConfig defaultConfig;

    @Autowired(required = true)
    public void setDefaultConfig(MQDefaultConfig defaultConfig) {
        MQConsumerUtil.defaultConfig = defaultConfig;
    }

    public static MQPushConsumer newPushConsumerInstance(String consumerGroup) {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(generateConsumerGroup(consumerGroup));
        consumer.setNamesrvAddr(defaultConfig.getNamesrvAddr());
        consumer.setConsumeFromWhere(defaultConfig.getConsumeFromWhere());
        consumer.setMessageModel(defaultConfig.getMessageModel());
        return consumer;
    }

    public static MQPullConsumer newPullConsumerInstance(String consumerGroup) {
        DefaultMQPullConsumer consumer = new DefaultMQPullConsumer(generateConsumerGroup(consumerGroup));
        consumer.setNamesrvAddr(defaultConfig.getNamesrvAddr());
        consumer.setMessageModel(defaultConfig.getMessageModel());
        return consumer;
    }

    public static MQDefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    private static String generateConsumerGroup(String consumerGroup) {
        return consumerGroup + "_" + ServerIdUtil.getId();
    }
}
