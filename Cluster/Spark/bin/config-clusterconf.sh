#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    config-clusterconf
## Description: 一键配置脚本：配置项目cluster中的conf配置文件
## Author:      mashencai
## Created:     2017-11-29
################################################################################
#set -x  ## 用于调试用，不用的时候可以注释掉

#---------------------------------------------------------------------#
#                              定义变量                                #
#---------------------------------------------------------------------#

cd `dirname $0`
BIN_DIR=`pwd`                                         ### bin目录：脚本所在目录
cd ..
SPARK_DIR=`pwd`                                      ### spark模块部署目录
CONF_SPARK_DIR=$SPARK_DIR/conf                       ### 配置文件目录
LOG_DIR=$SPARK_DIR/logs                              ### log日志目录
LOG_FILE=$LOG_DIR/config-cluster.log                  ### log日志目录
cd ..
CLUSTER_DIR=`pwd`                                     ### cluster 模块目录
cd ..
OBJECT_DIR=`pwd`                                      ### 项目根目录

CLUSTER_BUILD_DIR=$OBJECT_DIR/common                   ### common 目录
CONF_CLUSTER_DIR=$CLUSTER_BUILD_DIR/conf               ### 配置文件目录
CONF_FILE=$CONF_CLUSTER_DIR/project-conf.properties    ### 项目配置文件

## 安装的根目录，所有bigdata 相关的根目录
INSTALL_HOME=$(grep install_homedir ${CONF_FILE}|cut -d '=' -f2)

HADOOP_INSTALL_HOME=${INSTALL_HOME}/Hadoop            ### hadoop 安装目录
HADOOP_HOME=${HADOOP_INSTALL_HOME}/hadoop             ### hadoop 根目录
HBASE_INSTALL_HOME=${INSTALL_HOME}/HBase              ### hbase 安装目录
HBASE_HOME=${HBASE_INSTALL_HOME}/hbase                ### hbase 根目录
HIVE_INSTALL_HOME=${INSTALL_HOME}/Hive                ### hive 安装目录
HIVE_HOME=${HIVE_INSTALL_HOME}/hive                   ### hive 根目录
SPARK_INSTALL_HOME=${INSTALL_HOME}/Spark              ### spark 安装目录
SPARK_HOME=${SPARK_INSTALL_HOME}/spark                ### spark 根目录

mkdir -p $LOG_DIR
#---------------------------------------------------------------------#
#                              定义函数                                #
#---------------------------------------------------------------------#

#####################################################################
# 函数名: move_xml
# 描述: 配置Hbase服务，移动所需文件到cluster/spark/conf下
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function move_xml()
{
    echo ""  | tee -a $LOG_FILE
    echo "**********************************************" | tee -a $LOG_FILE
    echo "" | tee -a $LOG_FILE
    echo "copy 文件 hbase-site.xml core-site.xml hdfs-site.xml hive-site.xml到 cluster/conf......"  | tee  -a  $LOG_FILE

    cp ${HBASE_HOME}/conf/hbase-site.xml ${CONF_SPARK_DIR}
    cp ${HADOOP_HOME}/etc/hadoop/core-site.xml ${CONF_SPARK_DIR}
    cp ${HADOOP_HOME}/etc/hadoop/hdfs-site.xml ${CONF_SPARK_DIR}
    cp ${HIVE_HOME}/conf/hive-site.xml ${CONF_SPARK_DIR}

    echo "copy完毕......"  | tee  -a  $LOG_FILE
}

#####################################################################
# 函数名: config_sparkJob
# 描述: 配置文件：cluster/spark/conf/sparkJob.properties
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function config_sparkJob()
{
    echo ""  | tee -a $LOG_FILE
    echo "**********************************************" | tee -a $LOG_FILE
    echo "" | tee -a $LOG_FILE
    echo "配置cluster/spark/conf/sparkJob 文件......"  | tee  -a  $LOG_FILE

    ### 从project-conf.properties读取sparkJob所需配置IP
    # 根据字段kafka，查找配置文件中，Kafka的安装节点所在IP端口号的值，这些值以分号分割
	KAFKA_IP=$(grep kafka_install_node ${CONF_FILE}|cut -d '=' -f2)
    # 将这些分号分割的ip用放入数组
    spark_arr=(${KAFKA_IP//;/ })
    sparkpro=''    
    for spark_host in ${spark_arr[@]}
    do
        sparkpro="$sparkpro$spark_host:9092,"
    done
    sparkpro=${sparkpro%?}
    
    # 替换sparkJob.properties中：key=value（替换key字段的值value）
    sed -i "s#^kafka.metadata.broker.list=.*#kafka.metadata.broker.list=${sparkpro}#g" ${CONF_SPARK_DIR}/sparkJob.properties
    sed -i "s#^job.faceObjectConsumer.broker.list=.*#job.faceObjectConsumer.broker.list=${sparkpro}#g" ${CONF_SPARK_DIR}/sparkJob.properties

    # 根据字段zookeeper_installnode，查找配置文件中，Zk的安装节点所在IP端口号的值，这些值以分号分割
    ZK_IP=$(grep zookeeper_installnode ${CONF_FILE}|cut -d '=' -f2)
    # 将这些分号分割的ip用放入数组
    zk_arr=(${ZK_IP//;/ })
    zkpro=''
    phoenixpro=''
    phoenixpro=$phoenixpro${zk_arr[0]}":2181"
    for zk_ip in ${zk_arr[@]}
    do
        zkpro="$zkpro$zk_ip:2181,"
    done
    zkpro=${zkpro%?}
    # 替换sparkJob.properties中：key=value（替换key字段的值value）
    sed -i "s#^job.zkDirAndPort=.*#job.zkDirAndPort=${zkpro}#g" ${CONF_SPARK_DIR}/sparkJob.properties
    # 替换sparkJob.properties中：key=value(替换key字段的值value)
    sed -i "s#^phoenix.jdbc.url=jdbc:phoenix:.*#phoenix.jdbc.url=jdbc:phoenix:${phoenixpro}#g"  ${CONF_SPARK_DIR}/sparkJob.properties

    #根据字段es_service_node，查找配置文件中，es的安装节点所在ip端口号的值，这些值以分号分割
    ES_IP=$(grep es_service_node ${CONF_FILE} | cut -d '=' -f2)
    #将这些分号分割的ip用于放入数组中
    es_arr=(${ES_IP//;/ })
    espro=''
    espro=$espro${es_arr[0]}
    echo $espro
    echo "++++++++++++++++++++++++++++++++++"
    #替换sparkJob.properties中：key=value(替换key字段的值value)
    sed -i "s#^es.hosts=.*#es.hosts=${espro}#g" ${CONF_SPARK_DIR}/sparkJob.properties

    #根据字段rocketmq_nameserver，查找配置文件中，rocketmq的nameserver安装节点所在IP端口号的值，这些值以分号分割
    ROCK_IP=$(grep rocketmq_nameserver ${CONF_FILE} | cut -d '=' -f2)
    rockpro=''
    rockpro=$rockpro$ROCK_IP":9876"
    #替换sparkJob.properties中：key=value(替换key字段的值value)
    sed -i "s#^rocketmq.nameserver=.*#rocketmq.nameserver=${rockpro}#g"  ${CONF_SPARK_DIR}/sparkJob.properties


    # 根据job_clustering_mysql_url字段设置常驻人口管理告警信息MYSQL数据库地址
    num=$[ $(cat ${CONF_SPARK_DIR}/sparkJob.properties | cat -n | grep job.clustering.mysql.url  | awk '{print $1}') ]
    value=$(grep job_clustering_mysql_url ${CONF_FILE}  |  awk  -F  "url=" '{print $2}')
    value="job.clustering.mysql.url=${value}"
    sed -i "${num}c ${value}"  ${CONF_SPARK_DIR}/sparkJob.properties

    echo "配置完毕......"  | tee  -a  $LOG_FILE

    echo "开始分发SparkJob文件......"  | tee  -a  $LOG_FILE
    for spark_hname in ${spark_arr[@]}
    do
        scp -r ${CONF_SPARK_DIR}/sparkJob.properties root@${spark_hname}:${SPARK_HOME}/conf
    done
}

#####################################################################
# 函数名: main
# 描述: 脚本主要业务入口
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function main()
{
    move_xml
    config_sparkJob
}


#---------------------------------------------------------------------#
#                              执行流程                                #
#---------------------------------------------------------------------#

## 打印时间
echo ""  | tee  -a  $LOG_FILE
echo ""  | tee  -a  $LOG_FILE
echo "==================================================="  | tee -a $LOG_FILE
echo "$(date "+%Y-%m-%d  %H:%M:%S")"                       | tee  -a  $LOG_FILE
echo "开始配置cluster中的conf文件"                       | tee  -a  $LOG_FILE
main

set +x
