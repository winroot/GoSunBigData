#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    elasticsearch_local.sh
## Delasticcription: 安装elasticsearch
##              实现自动化的脚本
## Version:     2.0
## Author:      yinhang
## Created:     2018-07-28
################################################################################
#set -x

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
## 日志目录
LOG_DIR=${ROOT_HOME}/logs
## elasticsearch 安装日志
LOG_FILE=${LOG_DIR}/elasticsearchInstall.log
## elastic 安装包目录：
ELASTIC_SOURCE_DIR=${ROOT_HOME}/component/bigdata
## 最终安装的根目录，所有bigdata 相关的根目录：/opt/hzgc/bigdata
INSTALL_HOME=$(grep Install_HomeDir ${CONF_DIR}/cluster_conf.properties|cut -d '=' -f2)
## elasticsearch 安装目录
ELASTIC_INSTALL_HOME=${INSTALL_HOME}/Elastic
## elasticsearch 根目录
ELASTIC_HOME=${ELASTIC_INSTALL_HOME}/elastic
## JAVA_HOME
JAVA_HOME=${INSTALL_HOME}/JDK/jdk
## elasticsearch 集群节点
ELASTIC_HOST=`hostname -i`
## elasticsearch.yml 文件
ELASTIC_YML=${ELASTIC_HOME}/config/elasticsearch.yml

## 打印当前时间
echo "$(date "+%Y-%m-%d  %H:%M:%S")"

if [ ! -d $LOG_DIR ];then
    mkdir -p $LOG_DIR;
fi
## 首先检查本机上是否安装有es 如果有，则删除本机的es
if [ -e ${ELASTIC_HOME} ];then
    echo "删除原有es"
    rm -rf ${ELASTIC_HOME}
fi
mkdir -p ${ELASTIC_INSTALL_HOME}
cp -r ${ELASTIC_SOURCE_DIR}/elastic ${ELASTIC_INSTALL_HOME}
chmod -R 755 ${ELASTIC_INSTALL_HOME}

#####################################################################
# 函数名: config_yml_hostnamelist
# 描述: 将elasticsearch.yml中的discovery.zen.ping.unicast.hosts: [host_name_list]
# 		配置为 ["s1xx", "s1xx","s1xx"]
# 		tmp拼接后是：“s101”,"s102","s103",需要删除最右边的一个逗号“,”，
# 		${tmp%?}中的%号表示截取，以删除右边字符（,），保留左边字符（“s101”,"s102","s103"）
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function config_yml_hostnamelist ()
{
echo "" | tee -a $LOG_FILE
echo "**********************************************" | tee -a $LOG_FILE
echo "please waitinng, 修改elasticsearch.yml的配置........" | tee -a $LOG_FILE
echo "" | tee -a $LOG_FILE

tmp=""
tmp="$tmp\"${ELASTIC_HOST}\""  # 拼接字符串

#替换discovery.zen.ping.unicast.hosts字段的值
sed -i "s#discovery.zen.ping.unicast.hosts:.*#discovery.zen.ping.unicast.hosts: [${tmp}]#g" ${ELASTIC_YML}
echo "修改discovery.zen.ping.unicast.hosts:[${tmp}]成功" | tee -a $LOG_FILE
echo "" | tee -a $LOG_FILE
}

#####################################################################
# 函数名: config_yml_hostandIP
# 描述: 在每个节点上配置安装目录elasticsearch.yml中的:
# 		node.name: 对应节点的主机名
# 		network.host：对应节点的IP
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function config_yml_hostandIP ()
{
echo ""  | tee -a $LOG_FILE
echo "**********************************************" | tee -a $LOG_FILE
echo "每个节点上配置elasticsearch.yml中的node.name和network.host........" | tee -a $LOG_FILE

## 配置elasticsearch.yml中的node.name为当前节点的主机名
sed -i "s#node.name:.*#node.name: ${ELASTIC_HOST}#g" ${ELASTIC_YML}
echo "修改node.name:${ELASTIC_HOST}成功" | tee -a $LOG_FILE

## 配置elasticsearch.yml中的network.host为当前节点的IP
sed -i "s#network.host:.*#network.host: ${ELASTIC_HOST}#g" ${ELASTIC_YML}
echo "修改`hostname -i`的network.host成功" | tee -a $LOG_FILE
echo "**********************************************" | tee -a $LOG_FILE
echo "" | tee -a $LOG_FILE
}

#####################################################################
# 函数名: move_file
# 描述: 每个节点上移动3个文件到相应目录
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function move_file ()
{
echo "" | tee -a $LOG_FILE
echo "**********************************************" | tee -a $LOG_FILE
echo "" | tee -a $LOG_FILE
echo "在节点上移动es的3个文件到相应目录下......" | tee -a $LOG_FILE

echo "移动etc_security_limits.conf 到 目录/etc/security/limits.conf下......" | tee -a $LOG_FILE
cp -f ${ELASTIC_HOME}/config/etc_security_limits.conf /etc/security/limits.conf

echo "移动etc_security_limits.d_90-nproc.conf 到 目录/etc/security/limits.d/90-nproc.conf下......" | tee -a $LOG_FILE
cp -f ${ELASTIC_HOME}/config/etc_security_limits.d_90-nproc.conf /etc/security/limits.d/90-nproc.conf

echo "移动etc_sysctl.conf 到 目录/etc/sysctl.conf下......" | tee -a $LOG_FILE
cp -f ${ELASTIC_HOME}/config/etc_sysctl.conf /etc/sysctl.conf

echo "" | tee -a $LOG_FILE
echo "${ELASTIC_HOST}节点上移动完成." | tee -a $LOG_FILE
echo "动态地修改${ELASTIC_HOST}内核的运行参数.." | tee -a $LOG_FILE
sysctl -p
echo "" | tee -a $LOG_FILE
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
config_yml_hostnamelist
config_yml_hostandIP
move_file
}
#---------------------------------------------------------------------#
#                              执行流程                                #
#---------------------------------------------------------------------#

## 打印时间
echo "" | tee -a $LOG_FILE
echo "" | tee -a $LOG_FILE
echo "===================================================" | tee -a $LOG_FILE
echo "$(date "+%Y-%m-%d  %H:%M:%S")"                       | tee -a $LOG_FILE
main