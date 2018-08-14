#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    kinbana_local.sh
## Description: 安装并启动 kibana
##              实现自动化的脚本
## Version:     2.0
## Author:      yinhang
## Created:     2018-07-28
################################################################################
## set -x

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
LOG_FILE=${LOG_DIR}/kibanaInstall.log
## kibana 安装包目录
KIBANA_SOURCE_DIR=${ROOT_HOME}/component/bigdata
## 最终安装的根目录，所有bigdata 相关的根目录
INSTALL_HOME=$(grep Install_HomeDir ${CONF_DIR}/cluster_conf.properties|cut -d '=' -f2)
## kibana的安装节点，放入数组中
KIBANA_NODE=`hostname -i`

## KIBANA 安装目录
KIBANA_INSTALL_HOME=${INSTALL_HOME}/Kibana
## KIBANA 根目录
KIBANA_HOME=${INSTALL_HOME}/Kibana/kibana
## kibana.yml 文件
KIBANA_YML=${KIBANA_HOME}/config/kibana.yml
if [ ! -d $LOG_DIR ];then
    echo "创建安装日志目录"
    mkdir -p $LOG_DIR
fi

echo "==================================================="  | tee -a $LOG_FILE
echo "准备安装 kibana ..." | tee -a $LOG_FILE
echo "$(date "+%Y-%m-%d  %H:%M:%S")" | tee -a $LOG_FILE

#####################################################################
# 函数名: sync_file
# 描述: 检查节点上有没有kibana，有的话就删除，然后分发
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function sync_file ()
{
## 首先检查本机上是否安装有kibana 如果有，则删除本机的kibana
if [ -e ${KIBANA_HOME} ];then
    echo "删除原有kibana"
    rm -rf ${KIBANA_HOME}
fi
mkdir -p ${KIBANA_INSTALL_HOME}
cp -r ${KIBANA_SOURCE_DIR}/kibana ${KIBANA_INSTALL_HOME}
}

#####################################################################
# 函数名: mod_conf
# 描述: 检查节点上有没有kibana，有的话就删除，然后分发
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function mod_conf ()
{
## 修改 server.host
echo "正在修改 server.host "
num=`grep -n "server.host:" ${KIBANA_YML} | cut -d ':' -f1`
sed -i "${num}c server.host: \"${KIBANA_NODE}\"" ${KIBANA_YML}

## 修改 server.name
echo "正在修改 server.name "
num=`grep -n "server.name:" ${KIBANA_YML} | cut -d ':' -f1`
sed -i "${num}c server.name: \"${KIBANA_NODE}\"" ${KIBANA_YML}

## 修改 es url
echo "正在修改 es url "
num=`grep -n "elasticsearch.url:" ${KIBANA_YML} | cut -d ':' -f1`
sed -i "${num}c elasticsearch.url: \"http://${KIBANA_NODE}:9200\"" ${KIBANA_YML}

## 修改 requestTimeout
echo "正在修改 修改requestTimeout "
num=`grep -n "elasticsearch.requestTimeout:" ${KIBANA_YML} | cut -d ':' -f1`
sed -i "${num}c elasticsearch.requestTimeout:\ 3000000" ${KIBANA_YML}
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
sync_file
mod_conf
}
#---------------------------------------------------------------------#
#                              执行流程                                #
#---------------------------------------------------------------------#
main
## 打印时间
echo ""  | tee  -a  $LOG_FILE
echo ""  | tee  -a  $LOG_FILE
echo "$(date "+%Y-%m-%d  %H:%M:%S")"                       | tee -a $LOG_FILE
echo " kibana 安装完毕..." | tee  -a  $LOG_FILE
echo "===================================================" | tee -a $LOG_FILE