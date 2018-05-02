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
     * 通过配置文件来初始化ES客户端
     *
     * @return ES客户端
     */
    public static TransportClient getEsClient(){
        if (null == client){
            String es_cluster = ElasticSearchProperties.getEs_cluster_name();
            String es_hosts = ElasticSearchProperties.getEs_hosts().trim();
            Integer es_port = Integer.parseInt(ElasticSearchProperties.getEs_cluster_port());
            initElasticSearchClient(es_cluster, es_hosts, es_port);
        }
        return client;
    }

    /**
     * 通过指定参数获取ES客户端
     *
     * @param clusterName 集群名称
     * @param esHost 集群IP
     * @param esPort 集群端口号
     * @return ES客户端
     */
    public static TransportClient getEsClient(String clusterName, String esHost, int esPort) {
        if (null == client) {
            initElasticSearchClient(clusterName, esHost, esPort);
        }
        return client;
    }

    /**
     * 初始化ES客户端
     *
     * @param clusterName 集群名称
     * @param esHost 集群IP
     * @param esPort 集群端口号
     */
    private static void initElasticSearchClient(String clusterName, String esHost, int esPort) {
        // 初始化配置文件
        Settings settings = Settings
                .builder()
                .put("cluster.name", clusterName)
                .put("client.transport.sniff", true)
                .build();
        client = new PreBuiltTransportClient(settings);
        for (String host: esHost.split(",")) {
            try {
                client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), esPort));
                LOG.info("Address addition successed!");
            } catch (UnknownHostException e) {
                LOG.error("Host can not be identify!");
                e.printStackTrace();
            }
        }
    }
}
