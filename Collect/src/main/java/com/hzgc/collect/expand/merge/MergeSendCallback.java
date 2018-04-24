package com.hzgc.collect.expand.merge;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.log4j.Logger;

public class MergeSendCallback implements Callback {

    private Logger LOG = Logger.getLogger(MergeSendCallback.class);

    private final long startTime = System.currentTimeMillis();

    private String key;

    MergeSendCallback(String key) {
        this.key = key;
    }

    @Override
    public void onCompletion(RecordMetadata metadata, Exception e) {

        long elapsedTime = System.currentTimeMillis() - startTime;
        if (metadata != null) {
            LOG.info("Send Kafka successfully! message:[topic:" + metadata.topic() + ", key:" + key +
                    "], send to partition(" + metadata.partition() + "), offset(" + metadata.offset() + ") in " + elapsedTime + "ms");
        } else {
            LOG.error("message:[" + key + "Send to Kafka failed! ");
            e.printStackTrace();
        }
    }

}