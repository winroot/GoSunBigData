#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    kafkaStart_local.sh
## Description: 启动kafka集群的脚本
## Version:     1.0
## Author:      yinhang
## Created:     2018-07-28
################################################################################
#set -x

cd `dirname $0`
## 脚本所在目录
BIN_DIR=`pwd`
cd ..
## 安装包根目录
ROOT_HOME=`pwd`
## 配置文件目录
CONF_DIR=${ROOT_HOME}/conf
## 最终安装的根目录，所有bigdata 相关的根目录
INSTALL_HOME=$(grep Install_HomeDir ${CONF_DIR}/cluster_conf.properties|cut -d '=' -f2)
## kafka 的安装节点
KAFKA_HOSTNAME_LISTS=`hostname -i`
## KAFKA_HOME  kafka 根目录
KAFKA_HOME=${INSTALL_HOME}/Kafka/kafka

## 集群组件的日志文件目录:
CLUSTER_LOGSDIR=$(grep Cluster_LOGSDir ${CONF_DIR}/cluster_conf.properties|cut -d '=' -f2)

echo "启动Kafka"
mkdir -p  ${CLUSTER_LOGSDIR}/kafka/kafka-logs
touch  ${CLUSTER_LOGSDIR}/kafka/kafka-logs/kafka-server.log
source /etc/profile
nohup ${INSTALL_HOME}/Kafka/kafka/bin/kafka-server-start.sh ${INSTALL_HOME}/Kafka/kafka/config/server.properties >>${CLUSTER_LOGSDIR}/kafka/kafka-logs/kafka-server.log 2>&1 &

# 启动Kafka的ui工具kafka-manager
echo "kafka-manager"
cd ${KAFKA_HOME}
chmod -R 755 kafka-manager/
cd kafka-manager/
nohup bin/kafka-manager -Dconfig.file=${KAFKA_HOME}/kafka-manager/conf/application.conf &
echo "kafka-manager已启动,请到浏览器访问..(端口号9000)"

# 等待三秒后再验证Kafka是否启动成功
echo -e "********************验证Kafka是否启动成功*********************"
sleep 3s
jps | grep -E 'Kafka|jps show as bellow'
