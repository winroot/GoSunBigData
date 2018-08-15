#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    rocketmqStart_local.sh
## Description: 启动rocket集群的脚本.
## Version:     1.0
## Author:      yinhang
## Created:     2018-07-28
################################################################################

cd `dirname $0`
## 脚本所在目录
BIN_DIR=`pwd`
cd ..
## 安装包根目录
ROOT_HOME=`pwd`
## 配置文件目录
CONF_DIR=${ROOT_HOME}/conf
## 安装日记目录
LOG_DIR=${ROOT_HOME}/logs
## 安装日记目录
LOG_FILE=${LOG_DIR}/rocketmqStart.log
## 最终安装的根目录，所有bigdata 相关的根目录
INSTALL_HOME=$(grep Install_HomeDir ${CONF_DIR}/cluster_conf.properties|cut -d '=' -f2)
## rocketmq的安装节点，放入数组中
ROCKETQM_HOST=`hostname -i`
## RocketMQ根目录
ROCKETMQ_HOME=${INSTALL_HOME}/RocketMQ/rocketmq
##NameServer的端口号
NameServer_Port="ROCKETQM_HOST:9876"

source /etc/profile
chmod 755 ${ROCKETMQ_HOME}/bin/*
mkdir -p $LOG_DIR
nohup ${ROCKETMQ_HOME}/bin/mqnamesrv -n ${ROCKETQM_HOST}:9876 >> ${LOG_FILE} 2>&1 &
if [ $? -eq 0 ];then
    echo  -e 'NameServer start success \n'
else
    echo  -e 'NameServer start failed \n'
fi


mkdir -p $LOG_DIR
source /etc/profile
chmod 755 ${ROCKETMQ_HOME}/bin/*
nohup ${ROCKETMQ_HOME}/bin/mqbroker -n ${ROCKETQM_HOST}:9876 -c ${ROCKETMQ_HOME}/conf/2m-noslave/broker-$ROCKETQM_HOST.properties >> ${LOG_FILE} 2>&1 &
if [ $? -eq 0 ];then
    echo  -e "$host_name 节点下的broker启动成功 \n"
else
    echo  -e "$host_name 节点下的broker启动失败 \n"
fi

source /etc/profile
nohup java -jar ${ROCKETMQ_HOME}/lib/rocketmq-console-ng-1.0.0.jar --server.port=8083 --rocketmq.config.namesrvAddr=${NameServer_Port} >> ${LOG_FILE} 2>&1 &
if [ $? -eq 0 ];then
    echo  -e 'RocketMQ UI 配置成功 \n'
else
    echo  -e 'RocketMQ UI 配置失败 \n'
fi

# 等待五秒后再验证RocketMq是否启动成功
echo -e "********************验证RocketMq是否启动成功*********************"
sleep 5s
jps | grep -E 'NamesrvStartup|BrokerStartup|jps show as bellow'

