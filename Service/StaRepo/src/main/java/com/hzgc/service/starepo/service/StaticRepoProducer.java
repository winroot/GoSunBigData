package com.hzgc.service.starepo.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
@Slf4j
public class StaticRepoProducer {
    private KafkaProducer<String, Object> kafkaProducer;

    public StaticRepoProducer(@Value("${bootstrap.servers}") String kafkaBootStrap) {
        Properties properties = new Properties();
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("retries", "0");
        properties.put("request.required.acks", "-1");
        properties.put("bootstrap.servers", kafkaBootStrap);
        kafkaProducer = new KafkaProducer<>(properties);
        log.info("Create ProducerKafka successfully!");
    }

    public void sendKafkaMessage(final String topic, final String key, final String value) {
        log.info("Send kafka message [key:" + key + ", value:" + value + "]");
        kafkaProducer.send(new ProducerRecord<>(topic, key, value));
    }

    public void closeProducer() {
        if (null != kafkaProducer) {
            kafkaProducer.close();
        }
    }
}
