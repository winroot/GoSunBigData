#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    rocketmqStop_local.sh
## Description: 停止rocket集群的脚本
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
## rocketmq的安装节点
ROCKETQM_HOST=`hostname -i`
## RocketMQ根目录
ROCKETMQ_HOME=${INSTALL_HOME}/RocketMQ/rocketmq
##NameServer的端口号
NameServer_Port="ROCKETQM_HOST:9876"

source /etc/profile
sh ${ROCKETMQ_HOME}/bin/mqshutdown namesrv
if [ $? -eq 0 ];then
    echo  -e 'NameServer stop success \n'
else
    echo  -e 'NameServer stop failed \n'
fi

source /etc/profile
sh ${ROCKETMQ_HOME}/bin/mqshutdown broker
if [ $? -eq 0 ];then
    echo  -e "Broker stop success \n"
else
    echo  -e "Broker stop failed \n"
fi

# 等待五秒后再验证RocketMq是否启动成功
echo -e "********************验证RocketMq是否停止成功*********************"
sleep 5s
jps | grep -E 'NamesrvStartup|BrokerStartup|jps show as bellow'