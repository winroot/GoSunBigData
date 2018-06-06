#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    start-kafka-to-parquet.sh
## Description: to start kafkaToParquet
## Author:      chenke
## Created:     2017-11-09
################################################################################
#set -x  ## 用于调试使用，不用的时候可以注释掉

#---------------------------------------------------------------------#
#                              定义变量                               #
#---------------------------------------------------------------------#
cd `dirname $0`
## bin目录
BIN_DIR=`pwd`
cd ..
SPARK_DIR=`pwd`                                    #spark模块目录
cd ..
DEPLOY_DIR=`pwd`                                       #cluster目录
REALTIME_DIR=`pwd`                                     #RealTimeFaceCompare目录
######## cluster目录########
SPARK_CONF_DIR=${SPARK_DIR}/conf
SPARK_LIB_DIR=${SPARK_DIR}/lib
SPARK_LOG_DIR=${SPARK_DIR}/logs
LOG_FILE=${SPARK_LOG_DIR}/KafkaToParquet.log
######## common目录########
COMMON_CONF_DIR=${DEPLOY_DIR}/common/conf
########service目录###########

## bigdata_env
BIGDATA_ENV=/opt/hzgc/env_bigdata.sh
## spark class
SPARK_CLASS_PARAM=com.hzgc.cluster.spark.consumer.KafkaToParquet
## bigdata cluster path
BIGDATA_CLUSTER_PATH=/opt/hzgc/bigdata
##deploy-mode
DEPLOY_MODE=client
#---------------------------------------------------------------------#
#                              jar版本控制                            #
#---------------------------------------------------------------------#

## module version(模块)
SPARK_API_VERSION=`ls ${SPARK_LIB_DIR} | grep ^spark-[0-9].[0-9].[0-9].jar$`
JNI_VERSION=`ls ${SPARK_LIB_DIR}| grep ^jni-[0-9].[0-9].jar$`
ADDRESS_VERSION=`ls /opt/RealTimeFaceCompare/service/address | grep ^address-[0-9].[0-9].[0-9].jar$`
ALARM_VERSION=`ls /opt/RealTimeFaceCompare/service/alarm | grep ^alarm-[0-9].[0-9].[0-9].jar$`
CLUSTERING_VERSION=`ls /opt/RealTimeFaceCompare/service/clustering | grep ^clustering-[0-9].[0-9].[0-9].jar$`
COMMON_UTIL_VERSION=`ls ${SPARK_LIB_DIR} | grep ^common-util-[0-9].[0-9].[0-9].jar$`
COMMON_ES_VERSION=`ls ${SPARK_LIB_DIR} | grep ^common-es-[0-9].[0-9].[0-9].jar$`

## quote version(引用)
TRANSPORT_VERSION=transport-5.5.0.jar
TRANSPORT_NETTY3_VERSION=transport-netty3-client-5.5.0.jar
TRANSPORT_NETTY4_VERSION=transport-netty4-client-5.5.0.jar
LUCENE_ANALYZERS_VERSION=lucene-analyzers-common-6.6.0.jar
LUCENE_BACKWARD_VERSION=lucene-backward-codecs-6.6.0.jar
LUCENE_CORE_VERSION=lucene-core-6.6.0.jar
LUCENE_GROUPING_VERSION=lucene-grouping-6.6.0.jar
LUCENE_HIGHLIGHTER_VERSION=lucene-highlighter-6.6.0.jar
LUCENE_JOIN_VERSION=lucene-join-6.6.0.jar
LUCENE_MEMORY_VERSION=lucene-memory-6.6.0.jar
LUCENE_MISC_VERSION=lucene-misc-6.6.0.jar
LUCENE_QUERIES_VERSION=lucene-queries-6.6.0.jar
LUCENE_QUERYPARSER_VERSION=lucene-queryparser-6.6.0.jar
LUCENE_SANDBOX_VERSION=lucene-sandbox-6.6.0.jar
LUCENE_SPATIAL3D_VERSION=lucene-spatial3d-6.6.0.jar
LUCENE_SPATIAL_VERSION=lucene-spatial-6.6.0.jar
LUCENE_SPATIAL_EXTARS_VERSION=lucene-spatial-extras-6.6.0.jar
LUCENE_SUGGEST_VERSION=lucene-suggest-6.6.0.jar
GSON_VERSION=gson-2.8.0.jar
JACKSON_CORE_VERSION=jackson-core-2.8.6.jar
SPARK_STREAMING_KAFKA_VERSION=spark-streaming-kafka-0-8_2.11-2.2.0.jar
HBASE_SERVER_VERSION=hbase-server-1.3.2.jar
HBASE_CLIENT_VERSION=hbase-client-1.3.2.jar
HBASE_COMMON_VERSION=hbase-common-1.3.2.jar
HBASE_PROTOCOL_VERSION=hbase-protocol-1.3.2.jar
KAFKA_VERSION=kafka_2.11-0.8.2.1.jar
ELASTICSEARCH_VERSION=elasticsearch-5.5.0.jar
ROCKETMQ_CLIENT_VERSION=rocketmq-client-4.2.0.jar
ROCKETMQ_COMMON_VERSION=rocketmq-common-4.2.0.jar
ROCKETMQ_REMOTING_VERSION=rocketmq-remoting-4.2.0.jar
FASTJSON_VERSION=fastjson-1.2.29.jar
KAFKA_CLIENTS_VERSION=kafka-clients-1.0.0.jar
METRICS_CORE_VERSION=metrics-core-2.2.0.jar
ZKCLIENT_VERSION=zkclient-0.3.jar
TRANSPORT_VERSION=transport-5.5.0.jar
REINDEX_CLIENT=reindex-client-5.5.0.jar
PERCOLATOR_CLIENT=percolator-client-5.5.0.jar
LANG_MUSTACHE_CLIENT=lang-mustache-client-5.5.0.jar
PARENT_JOIN_CLIENT=parent-join-client-5.5.0.jar
REST_VERSION=rest-5.5.0.jar
HPPC_VERSION=hppc-0.7.1.jar
LOG4J_VERSION=log4j-api-2.7.jar
T_DIGEST_VERSION=t-digest-3.0.jar
NETTY_COMMON_VERSION=netty-common-4.1.11.Final.jar

############ 创建log目录 ###############
if [ ! -d ${SPARK_LOG_DIR} ];then
   mkdir ${SPARK_LOG_DIR}
fi

############ 判断是否存在大数据集群###################
if [ ! -d ${BIGDATA_CLUSTER_PATH} ];then
   echo "${BIGDATA_CLUSTER_PATH} does not exit,please go to the node of the existing bigdata cluster !"
   exit 0
fi

############### 判断是否存在配置文件 ##################
if [ ! -e ${SPARK_CONF_DIR}/sparkJob.properties ];then
    echo "${SPARK_CONF_DIR}/sparkJob.properties does not exit!"
    exit 0
else
    cp ${SPARK_CONF_DIR}/sparkJob.properties /opt/hzgc/bigdata/Spark/spark/conf/
fi
if [ ! -e ${SPARK_CONF_DIR}/log4j.properties ];then
    echo "${SPARK_CONF_DIR}/log4j.properties does not exit!"
    exit 0
else
    echo "===================开始配置log4j.properties===================="
    sed -i "s#^log4j.appender.FILE.File=.*#log4j.appender.FILE.File=${LOG_FILE}#g" ${SPARK_CONF_DIR}/log4j.properties
fi

################# 判断是否存在jar ###################
if [ ! -e ${SPARK_LIB_DIR}/${COMMMON_UTIL_VERSION} ];then
    echo "${SPARK_LIB_DIR}/${COMMMON_UTIL_VERSION} does not exit!"
    exit 0
fi
if [ ! -e ${SPARK_LIB_DIR}/${JNI_VERSION} ];then
    echo "${SPARK_LIB_DIR}/${JNI_VERSION} does not exit!"
    exit 0
fi
if [ ! -e ${SPARK_LIB_DIR}/${SPARK_API_VERSION} ];then
    echo "${SPARK_LIB_DIR}/${SPARK_API_VERSION} does not exit!"
    exit 0
fi
if [ ! -e ${SPARK_LIB_DIR}/${HBASE_CLIENT_VERSION} ];then
    echo "${SPARK_LIB_DIR}/${HBASE_CLIENT_VERSION} does not exit!"
    exit 0
fi
if [ ! -e ${SPARK_LIB_DIR}/${HBASE_COMMON_VERSION} ];then
    echo "${SPARK_LIB_DIR}/${HBASE_COMMON_VERSION} does not exit!"
    exit 0
fi
if [ ! -e ${SPARK_LIB_DIR}/${GSON_VERSION} ];then
    echo "${SPARK_LIB_DIR}/${GSON_VERSION} does not exit!"
    exit 0
fi
if [ ! -e ${SPARK_LIB_DIR}/${JACKSON_CORE_VERSION} ];then
    echo "${SPARK_LIB_DIR}/${JACKSON_CORE_VERSION} does not exit!"
    exit 0
fi
if [ ! -e ${SPARK_LIB_DIR}/${SPARK_STREAMING_KAFKA_VERSION} ];then
    echo "${SPARK_LIB_DIR}/${SPARK_STREAMING_KAFKA_VERSION} does not exit!"
    exit 0
fi
if [ ! -e ${SPARK_LIB_DIR}/${SERVICE_VERSION} ];then
    echo "${SPARK_LIB_DIR}/${SERVICE_VERSION} does not exit!"
    exit 0
fi
if [ ! -e ${SPARK_LIB_DIR}/${HBASE_SERVER_VERSION} ];then
    echo "${SPARK_LIB_DIR}/${HBASE_SERVER_VERSION} does not exit!"
    exit 0
fi
if [ ! -e ${SPARK_LIB_DIR}/${HBASE_PROTOCOL_VERSION} ];then
    echo "${SPARK_LIB_DIR}/${HBASE_PROTOCOL_VERSION} does not exit!"
    exit 0
fi
if [ ! -e ${SPARK_LIB_DIR}/${JNI_VERSION} ];then
    echo "${SPARK_LIB_DIR}/${JNI_VERSION} does not exit!"
    exit 0
fi
if [ ! -e ${SPARK_LIB_DIR}/${KAFKA_VERSION} ];then
    echo "${SPARK_LIB_DIR}/${KAFKA_VERSION} does not exit!"
    exit 0
fi
if [ ! -e ${SPARK_LIB_DIR}/${ELASTICSEARCH_VERSION} ];then
    echo "${SPARK_LIB_DIR}/${ELASTICSEARCH_VERSION} does not exit!"
    exit 0
fi
if [ ! -e ${SPARK_LIB_DIR}/${ROCKETMQ_CLIENT_VERSION} ];then
     echo "${SPARK_LIB_DIR}/${ROCKETMQ_CLIENT_VERSION} does not exit!"
     exit 0
fi
if [ ! -e ${SPARK_LIB_DIR}/${ROCKETMQ_COMMON_VERSION} ];then
     echo "${SPARK_LIB_DIR}/${ROCKETMQ_COMMON_VERSION} does not exit!"
     exit 0
fi
if [ ! -e ${SPARK_LIB_DIR}/${ROCKETMQ_REMOTING_VERSION} ];then
     echo "${SPARK_LIB_DIR}/${ROCKETMQ_REMOTING_VERSION} does not exit!"
     exit 0
fi
if [ ! -e ${SPARK_LIB_DIR}/${FASTJSON_VERSION} ];then
     echo "${SPARK_LIB_DIR}/${FASTJSON_VERSION} does not exit!"
     exit 0
fi
if [ ! -e ${SPARK_LIB_DIR}/${KAFKA_CLIENTS_VERSION} ];then
    echo "${SPARK_LIB_DIR}/${KAFKA_CLIENTS_VERSION} does not exit!"
    exit 0
fi
if [ ! -e ${SPARK_LIB_DIR}/${METRICS_CORE_VERSION} ];then
    echo "${SPARK_LIB_DIR}/${METRICS_CORE_VERSION} does not exit!"
    exit 0
fi

################## 存放数据至parquet任务 ###################
source /etc/profile
source ${BIGDATA_ENV}
nohup spark-submit \
--master yarn \
--deploy-mode ${DEPLOY_MODE} \
--executor-memory 4g \
--executor-cores 2 \
--num-executors 4 \
--class ${SPARK_CLASS_PARAM} \
--jars ${SPARK_LIB_DIR}/${GSON_VERSION},\
${SPARK_LIB_DIR}/${TRANSPORT_VERSION},\
${SPARK_LIB_DIR}/${TRANSPORT_NETTY4_VERSION},\
${SPARK_LIB_DIR}/${LUCENE_ANALYZERS_VERSION},\
${SPARK_LIB_DIR}/${LUCENE_BACKWARD_VERSION},\
${SPARK_LIB_DIR}/${LUCENE_CORE_VERSION},\
${SPARK_LIB_DIR}/${LUCENE_GROUPING_VERSION},\
${SPARK_LIB_DIR}/${LUCENE_HIGHLIGHTER_VERSION},\
${SPARK_LIB_DIR}/${LUCENE_JOIN_VERSION},\
${SPARK_LIB_DIR}/${LUCENE_MEMORY_VERSION},\
${SPARK_LIB_DIR}/${LUCENE_MISC_VERSION},\
${SPARK_LIB_DIR}/${LUCENE_QUERIES_VERSION},\
${SPARK_LIB_DIR}/${LUCENE_QUERYPARSER_VERSION},\
${SPARK_LIB_DIR}/${LUCENE_SANDBOX_VERSION},\
${SPARK_LIB_DIR}/${LUCENE_SPATIAL3D_VERSION},\
${SPARK_LIB_DIR}/${LUCENE_SPATIAL_VERSION},\
${SPARK_LIB_DIR}/${LUCENE_SPATIAL_EXTARS_VERSION},\
${SPARK_LIB_DIR}/${LUCENE_SUGGEST_VERSION},\
${SPARK_LIB_DIR}/${HPPC_VERSION},\
${SPARK_LIB_DIR}/${LOG4J_VERSION},\
${SPARK_LIB_DIR}/${T_DIGEST_VERSION},\
${SPARK_LIB_DIR}/${NETTY_COMMON_VERSION},\
${SPARK_LIB_DIR}/${REST_VERSION},\
${SPARK_LIB_DIR}/${PARENT_JOIN_CLIENT},\
${SPARK_LIB_DIR}/${REINDEX_CLIENT},\
${SPARK_LIB_DIR}/${PERCOLATOR_CLIENT},\
${SPARK_LIB_DIR}/${TRANSPORT_NETTY3_VERSION},\
${SPARK_LIB_DIR}/${LANG_MUSTACHE_CLIENT},\
${SPARK_LIB_DIR}/${JACKSON_CORE_VERSION},\
${SPARK_LIB_DIR}/${COMMON_UTIL_VERSION},\
${SPARK_LIB_DIR}/${COMMON_ES_VERSION},\
${SPARK_LIB_DIR}/${SPARK_STREAMING_KAFKA_VERSION},\
${SPARK_LIB_DIR}/${HBASE_SERVER_VERSION},\
${SPARK_LIB_DIR}/${HBASE_CLIENT_VERSION},\
${SPARK_LIB_DIR}/${HBASE_COMMON_VERSION},\
${SPARK_LIB_DIR}/${HBASE_PROTOCOL_VERSION},\
${SPARK_LIB_DIR}/${ZKCLIENT_VERSION},\
${SPARK_LIB_DIR}/${JNI_VERSION},\
${SPARK_LIB_DIR}/${KAFKA_VERSION},\
${SPARK_LIB_DIR}/${ELASTICSEARCH_VERSION},\
${SPARK_LIB_DIR}/${TRANSPORT_VERSION},\
${SPARK_LIB_DIR}/${ROCKETMQ_CLIENT_VERSION},\
${SPARK_LIB_DIR}/${ROCKETMQ_COMMON_VERSION},\
${SPARK_LIB_DIR}/${ROCKETMQ_REMOTING_VERSION},\
${SPARK_LIB_DIR}/${FASTJSON_VERSION},\
${SPARK_LIB_DIR}/${KAFKA_CLIENTS_VERSION},\
${SPARK_LIB_DIR}/${METRICS_CORE_VERSION} \
--conf "spark.driver.extraJavaOptions=-Dlog4j.configuration=file:${SPARK_CONF_DIR}/log4j.properties" \
--files ${SPARK_CONF_DIR}/sparkJob.properties,\
/opt/hzgc/bigdata/HBase/hbase/conf/hbase-site.xml \
${SPARK_LIB_DIR}/${SPARK_API_VERSION} > ${LOG_FILE} 2>&1 &