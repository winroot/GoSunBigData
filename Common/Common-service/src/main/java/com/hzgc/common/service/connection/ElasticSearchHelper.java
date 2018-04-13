package com.hzgc.common.service.connection;

import org.apache.log4j.Logger;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ElasticSearchHelper implements Serializable{
    private static Logger LOG = Logger.getLogger(ElasticSearchHelper.class);
    private static TransportClient client = null;

    /**
     * 初始化Es 集群信息
     */
    private static void initElasticSearchClient() {
        String es_cluster = ElasticSearchProperties.getEs_cluster_name();
        String es_hosts = ElasticSearchProperties.getEs_hosts().trim();
        Integer es_port = Integer.parseInt(ElasticSearchProperties.getEs_cluster_port());
        // 初始化配置文件
        Settings settings = Settings.builder().put("cluster.name", es_cluster)
                .put("client.transport.sniff", true).build();
        //初始化client
        client = new PreBuiltTransportClient(settings);
        for (String host: es_hosts.split(",")){
            try {
                client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), es_port));
                LOG.info("Address addition successed!");
            } catch (UnknownHostException e) {
                LOG.error("Host can not be identify!");
                e.printStackTrace();
            }
        }
    }

    /**
     * 返回client 对象信息
     */
    public static TransportClient getEsClient(){
        if (null == client){
            initElasticSearchClient();
        }
        return ElasticSearchHelper.client;
    }
}
