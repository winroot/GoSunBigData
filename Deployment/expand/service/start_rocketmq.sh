#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    start_rocketmq.sh
## Description: 启动新增节点rocketmq服务
##              实现自动化的脚本
## Version:     2.0
## Author:      yinhang
## Created:     2018-7-20
################################################################################
#set -e
#set -x

cd `dirname $0`
## 脚本所在目录
cd ..
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
## RocketMQ根目录
ROCKETMQ_HOME=${INSTALL_HOME}/RocketMQ/rocketmq
##NameServer的主机名和ip地址
NameServer_Host=$(grep RocketMQ_Namesrv ${CONF_DIR}/cluster_conf.properties|cut -d '=' -f2)

NameServer_IP=$(cat /etc/hosts|grep "$NameServer_Host" | awk '{print $1}')
##NameServer的端口号
NameServer_Port="$NameServer_IP:9876"

## 集群新增节点主机名，放入数组中
CLUSTER_HOST=$(grep Node_HostName ${ROOT_HOME}/expand/conf/expand_conf.properties | cut -d '=' -f2)
echo "读取的新增集群节点IP为："${CLUSTER_HOST} | tee -a $LOG_FILE
HOSTNAMES=(${CLUSTER_HOST//;/ })

for host_name in ${HOSTNAMES[@]}
do
    ssh root@${host_name} "mkdir -p $LOG_DIR"
    ssh root@${host_name} "source /etc/profile;chmod 755 ${ROCKETMQ_HOME}/bin/*; nohup ${ROCKETMQ_HOME}/bin/mqbroker -n ${NameServer_IP}:9876 -c ${ROCKETMQ_HOME}/conf/2m-noslave/broker-$host_name.properties >> ${LOG_FILE} 2>&1 & "
    if [ $? -eq 0 ];then
        echo  -e "$host_name 节点下的broker启动成功 \n"
    else
        echo  -e "$host_name 节点下的broker启动失败 \n"
    fi
done

# 等待7秒后再验证RocketMq是否启动成功
echo -e "********************验证RocketMq是否启动成功*********************"
sleep 7s
source $(grep Source_File ${CONF_DIR}/cluster_conf.properties|cut -d '=' -f2)
xcall jps | grep -E 'NamesrvStartup|BrokerStartup|jps show as bellow'
