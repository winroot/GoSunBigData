package com.hzgc.compare.worker.comsumer;



import com.hzgc.compare.FaceObject;
import com.hzgc.compare.worker.conf.Config;
import com.hzgc.compare.worker.memory.cache.MemoryCacheImpl;
import com.hzgc.compare.worker.util.FaceObjectUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Comsumer extends Thread{
    private static final Logger logger = LoggerFactory.getLogger(Comsumer.class);
    private MemoryCacheImpl memoryCache;
    private Config conf;
    private KafkaConsumer<String, String> comsumer;

    public Comsumer(){
        init();
    }

    /**
     * 初始化
     */
    private void init(){
        conf = Config.getConf();
        Properties prop = new Properties();
        prop.put("bootstrap.servers", conf.getValue(Config.KAFKA_BOOTSTRAP_SERVERS));
        prop.put("group.id", conf.getValue(Config.KAFKA_GROUP_ID));
        prop.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        prop.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        comsumer = new KafkaConsumer<>(prop);
        logger.info("Kafka comsumer is init.");
        memoryCache = MemoryCacheImpl.getInstance();
    }
    /**
     * 接收从kafka传来的数据
     */
    private void receiveAndSave(){
        comsumer.subscribe(Collections.singletonList(conf.getValue(Config.KAFKA_TOPIC)));
        logger.info("Comsumer is started to accept kafka info.");
        while(true){
            ConsumerRecords<String, String> records =
                    comsumer.poll(Long.parseLong(conf.getValue(Config.KAFKA_MAXIMUM_TIME)));
            List<FaceObject> objList = new ArrayList<FaceObject>();
            for(ConsumerRecord<String, String> record : records){
                FaceObject obj = FaceObjectUtil.jsonToObject(record.value());
                objList.add(obj);
                logger.debug(record.value());
            }
            memoryCache.addFaceObjects(objList);
//            logger.info("Push records from kafka to memory , the size is : " + objList.size());
        }
    }

    public void run() {
        receiveAndSave();
    }
}
