package com.hzgc.collect.expand.processer;

import com.hzgc.collect.expand.util.CollectProperties;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.Properties;

public class ProducerKafka implements Serializable {

    private static Logger LOG = Logger.getLogger(ProducerKafka.class);

    public static ProducerKafka instance;
    private static KafkaProducer<String, String> kafkaProducer;

    private ProducerKafka() {
        Properties kafkProper = new Properties();
        CollectProperties.getProps().forEach((key, value) -> {
            if (((String)key).contains("kafka.") && !((String)key).contains("topic")) {
                kafkProper.setProperty(((String) key).replace("kafka.", ""),
                        (String) value);
            }
        });
        kafkaProducer = new KafkaProducer<>(kafkProper);
        LOG.info("Create ProducerKafka successfully!");
    }

    void sendKafkaMessage(final String topic,
                          final String key,
                          final String value,
                          final Callback callBack) {
        kafkaProducer.send(new ProducerRecord<>(topic, key, value), callBack);
    }

    public static ProducerKafka getInstance() {
        if (instance == null) {
            synchronized (ProducerKafka.class) {
                instance = new ProducerKafka();
            }
        }
        return instance;
    }

    public void closeProducer() {
        if (null != kafkaProducer) {
            kafkaProducer.close();
        }
    }
}

