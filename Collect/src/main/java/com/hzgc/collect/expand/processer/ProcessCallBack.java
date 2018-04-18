package com.hzgc.collect.expand.processer;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.log4j.Logger;

public class ProcessCallBack implements Callback {
    private static Logger LOG = Logger.getLogger(ProcessCallBack.class);
    private long elapsedTime;
    private String key;

    ProcessCallBack(String key, long time) {
        this.key = key;
        this.elapsedTime = time;
    }
    @Override
    public void onCompletion(RecordMetadata metadata, Exception e) {
        if (metadata != null) {
            LOG.info("Send Kafka successfully! message:[topic:" + metadata.topic() + ", key:" + key +
                    "], send to partition(" + metadata.partition() + "), offset(" + metadata.offset() + ") in " + elapsedTime + "ms");
        } else {
            LOG.error("Send Kafka failed！ message:[" + key + "], write to error log！");
            LOG.error(e.getMessage());
        }
    }
}
