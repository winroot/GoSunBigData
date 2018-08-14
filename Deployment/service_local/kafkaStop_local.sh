#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    kafkaStop_local.sh
## Description: 停止kafka集群的脚本
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

echo "关闭Kafka"
# 删除kafka-manager的PID文件
cd ${KAFKA_HOME}/kafka-manager
rm -f RUNNING_PID

# 关闭每个节点上的Kafka和kafka-manager进程
# 查找Kafka进程，会有Kafka与kafka-manager两个进程，因此将这两个进程号输出到文件，循环读取删除
ps ax | grep -i 'kafka' | grep java | grep -v grep | awk "{print \$1}" > ${BIN_DIR}/kafka_del.tmp
for PID in $(cat ${BIN_DIR}/kafka_del.tmp);do
    if [ -z "$PID" ]; then
	    echo "No kafka server to stop"
	    exit 1
	else
		kill -s TERM $PID
		echo "kafka stop success"
	fi
	done
	rm -f ${BIN_DIR}/kafka_del.tmp



# 验证Kafka是否停止成功
echo -e "********************验证Kafka是否停止成功*********************"
sleep 5s

jps | grep -E 'Kafka|jps show as bellow'
