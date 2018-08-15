#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    esStart_local.sh
## Description: 启动es集群的脚本
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
## 安装日志目录
LOG_DIR=${ROOT_HOME}/logs
## 安装日志
LOG_FILE=${LOG_DIR}/esStart.log
## 最终安装的根目录，所有bigdata 相关的根目录
INSTALL_HOME=$(grep Install_HomeDir ${CONF_DIR}/cluster_conf.properties|cut -d '=' -f2)
## es的安装节点
ES_HOST=`hostname -i`

## ELASTIC_INSTALL_HOME elastic 安装目录：/opt/hzgc/bigdata/Elastic
ELASTIC_INSTALL_HOME=${INSTALL_HOME}/Elastic
## ELASTIC_HOME  elastic 根目录：/opt/hzgc/bigdata/Elastic/elastic
ELASTIC_HOME=${ELASTIC_INSTALL_HOME}/elastic


if [ ! -d "$LOG_DIR" ];then
	mkdir ${LOG_DIR}
fi

# 启动ES服务
echo "" | tee -a $LOG_FILE
echo "开始启动ES服务......"    | tee -a $LOG_FILE

# 创建elsearch用户
echo -e "在每个节点上创建elsearch用户："              | tee -a $LOG_FILE

groupadd elsearch
useradd elsearch -g elsearch -p elastic
chown -R elsearch:elsearch ${ELASTIC_HOME}

echo "" | tee -a $LOG_FILE
echo "创建elsearch用户完毕......"    | tee -a $LOG_FILE
echo "" | tee -a $LOG_FILE

# 启动ES
echo "**********************************************" | tee -a $LOG_FILE
echo -e "启动${ES_HOST}节点下ES..."                    | tee -a $LOG_FILE
chmod 777 /tmp #修改tmp目录的权限，不修改会报错
source /etc/profile
# 切换为elsearch用户
su -c "${ELASTIC_HOME}/bin/elasticsearch -d" elsearch

echo "" | tee -a $LOG_FILE
echo "启动ES完毕......"    | tee -a $LOG_FILE

# 验证ES是否启动成功
echo -e "********************验证ES是否启动成功*********************"
sleep 5s
jps | grep -E 'Elasticsearch|jps show as bellow'
