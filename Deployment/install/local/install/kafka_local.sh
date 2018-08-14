#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    kafka_local.sh
## Description: kafka 安装
## Version:     2.4
## Author:      yinhang
## Created:     2018-07-28
################################################################################
## set -x  ## 用于调试用，不用的时候可以注释掉

#---------------------------------------------------------------------#
#                              定义变量                                #
#---------------------------------------------------------------------#
cd `dirname $0`
## 脚本所在目录
BIN_DIR=`pwd`
cd ../../..
## 安装包根目录
ROOT_HOME=`pwd`
## 配置文件目录
CONF_DIR=${ROOT_HOME}/conf
## 安装日志目录
LOG_DIR=${ROOT_HOME}/logs
## 安装日志目录
LOG_FILE=${LOG_DIR}/kafkaInstall.log
## kafka 安装包目录
KAFKA_SOURCE_DIR=${ROOT_HOME}/component/bigdata
## 最终安装的根目录，所有bigdata 相关的根目录：/opt/hzgc/bigdata
INSTALL_HOME=$(grep Install_HomeDir ${CONF_DIR}/cluster_conf.properties|cut -d '=' -f2)
## kafka 集群节点
KAFKA_HOST=`hostname -i`
##kafka日志目录
KAFKA_LOG=$(grep Cluster_LOGSDir ${CONF_DIR}/cluster_conf.properties|cut -d '=' -f2)
## KAFKA_INSTALL_HOME kafka 安装目录
KAFKA_INSTALL_HOME=${INSTALL_HOME}/Kafka
## KAFKA_HOME  kafka 根目录
KAFKA_HOME=${INSTALL_HOME}/Kafka/kafka
## kafka的config目录
KAFKA_CONFIG=${KAFKA_HOME}/config
## kafka的server.properties文件
KAFKA_SERVER_PROPERTIES=${KAFKA_CONFIG}/server.properties
## kafka的server.properties文件
KAFKA_PRODUCER_PROPERTIES=${KAFKA_CONFIG}/producer.properties
## kafka的application.conf文件
KAFKA_APPLICATION_CONF=${KAFKA_HOME}/kafka-manager/conf/application.conf

## server.properties文件节点名称
SERVER_HOSTS=$(grep zookeeper.connect= ${KAFKA_SERVER_PROPERTIES} | cut -d '=' -f2)
SERVER_HOSTNAMES=(${SERVER_HOSTS//;/ })
## producer.properties文件节点名称
PRODUCER_HOSTS=$(grep bootstrap.servers ${KAFKA_PRODUCER_PROPERTIES} | cut -d '=' -f2)
PRODUCER_HOSTNAMES=(${PRODUCER_HOSTS//;/ })
## application.conf文件
APPLICATION_HOSTS=$(grep kafka-manager.zkhosts=\" ${KAFKA_APPLICATION_CONF} | cut -d '=' -f2 | cut -d '"' -f2)
APPLICATION_HOSTNAMES=(${APPLICATION_HOSTS//;/ })

if [ ! -d $LOG_DIR ];then
    mkdir -p $LOG_DIR;
fi

if [ ! -d $KAFKA_LOG ];then
    mkdir -p $KAFKA_LOG;
fi

## 首先检查本机上是否安装有 kafka 如果有，则删除本机的 kafka
if [ -e ${KAFKA_HOME} ];then
    echo "删除原有 kafka"
    rm -rf ${KAFKA_HOME}
fi
mkdir -p ${KAFKA_INSTALL_HOME}
cp -r ${KAFKA_SOURCE_DIR}/kafka ${KAFKA_INSTALL_HOME}
chmod -R 755 ${KAFKA_INSTALL_HOME}

#####################################################################
# 函数名:zookeeper_connect
# 描述: 修改 server_properties 文件中的 zookeeper.connect
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function zookeeper_connect ()
{
if [[ "${SERVER_HOSTS}" =~ "${KAFKA_HOST}" ]] ; then
    echo "zookeeper.connect 配置文件中已添加本机 $KAFKA_HOST 的IP以及端口，不需要重复添加！！！" | tee -a $LOG_FILE
else
    sed -i "s#zookeeper.connect=.*#zookeeper.connect=${KAFKA_HOST}:2181#g" ${KAFKA_SERVER_PROPERTIES}
fi
}

#####################################################################
# 函数名:producer_properties
# 描述: 修改producer.properties文件
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function producer_properties ()
{
if [[ "${PRODUCER_HOSTS}" =~ "${KAFKA_HOST}" ]] ; then
    echo "bootstrap.servers配置文件中已添加本机 $KAFKA_HOST 的IP以及端口，不需要重复操作！！！" | tee -a $LOG_FILE
else
    sed -i "s#bootstrap.servers=.*#bootstrap.servers=${KAFKA_HOST}:9092#g" ${KAFKA_PRODUCER_PROPERTIES}
fi
}

#####################################################################
# 函数名:application_conf
# 描述: 修改application.conf文件
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function application_conf ()
{
if [[ "${APPLICATION_HOSTS}" =~ "${KAFKA_HOST}" ]] ; then
    echo ${KAFKA_HOST} "节点已添加，不需要重复操作！！！" | tee -a $LOG_FILE
else
    value=$(grep kafka-manager.zkhosts=\" ${KAFKA_APPLICATION_CONF} | cut -d '=' -f2 | cut -d '"' -f2)
    sed -i "s#kafka-manager.zkhosts=.*#kafka-manager.zkhosts=\"`hostname -i`:2181\"#g" ${KAFKA_APPLICATION_CONF}
fi
}

#####################################################################
# 函数名:server_properties
# 描述: 修改server.properties文件
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
LISTENERS=PLAINTEXT:
LOG_DIRS=/opt/hzgc/logs/kafka/kafka-logs
function server_properties ()
{
num=1
##修改 brokerID
echo "正在修改${KAFKA_HOST}的 broker.id ..." | tee -a $LOG_FILE
sed -i "s#broker.id=.*#broker.id=${num}#g" ${KAFKA_SERVER_PROPERTIES}
echo "${KAFKA_HOST}的 broker.id 配置修改完毕！！！" | tee -a $LOG_FILE

##修改 listeners
echo "正在修改${KAFKA_HOST}的 listeners ..." | tee -a $LOG_FILE
value2=$(grep "listeners=PLAINTEXT" ${KAFKA_SERVER_PROPERTIES} | cut -d ':' -f2| sed -n '1p;1q')
sed -i "s#listeners=${LISTENERS}${value2}\:9092#listeners=${LISTENERS}\/\/${KAFKA_HOST}\:9092#g" ${KAFKA_SERVER_PROPERTIES}
echo "${KAFKA_HOST}的 listeners 配置修改完毕！！！" | tee -a $LOG_FILE

## 修改 log.dirs
echo "正在修改${KAFKA_HOST}的 log.dirs ..." | tee -a $LOG_FILE
value3=$(grep "log.dirs" ${KAFKA_SERVER_PROPERTIES} | cut -d '=' -f2)
sed -i "s#log.dirs=${value3}#log.dirs=${LOG_DIRS}#g" ${KAFKA_SERVER_PROPERTIES}
echo "${KAFKA_HOST}的 log.dirs 配置修改完毕！！！" | tee -a $LOG_FILE
}

#####################################################################
# 函数名: main
# 描述: 脚本主要业务入口
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function main ()
{
zookeeper_connect
producer_properties
application_conf
server_properties
}

#---------------------------------------------------------------------#
#                              执行流程                                #
#---------------------------------------------------------------------#
## 打印时间
echo "" | tee -a $LOG_FILE
echo "$(date "+%Y-%m-%d  %H:%M:%S")" | tee  -a  $LOG_FILE
main

