package com.hzgc.common.service.mq;

import com.hzgc.common.service.utils.ServerIdUtil;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * mq生产者工具类
 *
 * @author liuzhikun
 */
@Component
public class MQProducerUtil {
    private static MQDefaultConfig defaultConfig;

    @Autowired(required = true)
    public void setDefaultConfig(MQDefaultConfig defaultConfig) {
        MQProducerUtil.defaultConfig = defaultConfig;
    }

    public static MQProducer newProducer(String producerGroup) {
        DefaultMQProducer producer = new DefaultMQProducer(generateProducerGroup(producerGroup));
        producer.setNamesrvAddr(defaultConfig.getNamesrvAddr());
        return producer;
    }

    public static MQDefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    private static String generateProducerGroup(String producerGroup) {
        return producerGroup + "_" + ServerIdUtil.getId();
    }
}
