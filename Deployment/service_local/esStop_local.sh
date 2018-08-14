#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    esStop_local.sh
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

# 停止ES服务
echo ""  | tee -a $LOG_FILE
echo "**********************************************" | tee -a $LOG_FILE
echo "" | tee -a $LOG_FILE
echo "开始停止ES服务......"    | tee -a $LOG_FILE

source /etc/profile
es_pid=`jps | grep Elasticsearch | gawk '{print \$1}'`
kill $es_pid
if [ $? -eq 0 ];then
    echo -e "`hostname -i` es stop success\n" | tee -a $LOG_FILE
else
	echo -e "`hostname -i` es stop failed\n" | tee -a $LOG_FILE
fi

echo "" | tee -a $LOG_FILE
echo "停止ES服务完毕."    | tee -a $LOG_FILE


# 验证ES是否启动成功
echo -e "********************验证ES是否停止成功*********************"
sleep 5s
jps | grep -E 'Elasticsearch|jps show as bellow'