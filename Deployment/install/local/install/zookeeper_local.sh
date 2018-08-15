#!/bin/bash
################################################################################
## Copyright:    HZGOSUN Tech. Co, BigData
## Filename:     zookeeper_local.sh
## Description:  zookeeper
## Version:      1.0
## Scala.Version:2.11.8
## Author:       yinhang
## Created:      2018-07-28
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
## zookeeper 安装日志
LOG_FILE=${LOG_DIR}/zkInstall.log
## ZOOKEEPER 安装包目录
ZOOKEEPER_SOURCE_DIR=${ROOT_HOME}/component/bigdata
## 最终安装的根目录，所有bigdata 相关的根目录
INSTALL_HOME=$(grep Install_HomeDir ${CONF_DIR}/cluster_conf.properties|cut -d '=' -f2)
## ZOOKEEPER_INSTALL_HOME zookeeper 安装目录
ZOOKEEPER_INSTALL_HOME=${INSTALL_HOME}/Zookeeper
## ZOOKEEPER_HOME  zookeeper 根目录
ZOOKEEPER_HOME=${INSTALL_HOME}/Zookeeper/zookeeper
## ZOOKEEPER集群节点
ZOOKEEPER_HOST=`hostname -i`

## zookeeper conf 目录
ZOOKEEPER_CONF=${ZOOKEEPER_HOME}/conf
## zookeeper zoo,cfg 文件
ZOO_CFG_FILE=${ZOOKEEPER_CONF}/zoo.cfg
## zookeeper data 目录
ZOOKEEPER_DATA=${ZOOKEEPER_HOME}/data
## zookeeper myid 文件
ZOOKEEPER_MYID_FILE=${ZOOKEEPER_DATA}/myid

## 打印当前时间
echo "$(date "+%Y-%m-%d  %H:%M:%S")"

if [ ! -d $LOG_DIR ];then
    mkdir -p $LOG_DIR;
fi
## 首先检查本机上是否安装有zookeeper 如果有，则删除本机的zookeeper
if [ -e ${ZOOKEEPER_HOME} ];then
    echo "删除原有zookeeper"
    rm -rf ${ZOOKEEPER_HOME}
fi
mkdir -p ${ZOOKEEPER_INSTALL_HOME}
cp -r ${ZOOKEEPER_SOURCE_DIR}/zookeeper ${ZOOKEEPER_INSTALL_HOME}
chmod -R 755 ${ZOOKEEPER_INSTALL_HOME}

#####################################################################
# 函数名:zoo_cfg
# 描述: 修改 zoo.cfg
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function zoo_cfg ()
{
NUMBER=1
## 修改 zoo.cfg
echo "server.${NUMBER}=${ZOOKEEPER_HOST}:2888:3888" >> ${ZOO_CFG_FILE}
}

#####################################################################
# 函数名:myid
# 描述: 修改 myid
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function myid ()
{
num=1
##修改 myid
echo "正在修改${ZOOKEEPER_HOST}的 myid ..." | tee -a $LOG_FILE
echo "${num}" > ${ZOOKEEPER_MYID_FILE}
echo "${ZOOKEEPER_HOST}的 myid 配置修改完毕！！！" | tee -a $LOG_FILE
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
zoo_cfg
myid
}

#---------------------------------------------------------------------#
#                              执行流程                                #
#---------------------------------------------------------------------#
## 打印时间
echo "" | tee -a $LOG_FILE
echo "$(date "+%Y-%m-%d  %H:%M:%S")" | tee  -a  $LOG_FILE
main