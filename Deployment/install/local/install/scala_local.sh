#!/bin/bash
################################################################################
## Copyright:    HZGOSUN Tech. Co, BigData
## Filename:     scalaInstall.sh
## Description:  安装scala。
## Version:      1.0
## Scala.Version:2.11.8
## Author:       qiaokaifeng
## Created:      2017-10-24
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
## scala 安装日志
LOG_FILE=${LOG_DIR}/scalaInstall.log
##  scala 安装包目录
SCALA_SOURCE_DIR=${ROOT_HOME}/component/bigdata
## 最终安装的根目录，所有bigdata 相关的根目录
INSTALL_HOME=$(grep Install_HomeDir ${CONF_DIR}/cluster_conf.properties|cut -d '=' -f2)
## SCALA_INSTALL_HOME scala 安装目录
SCALA_INSTALL_HOME=${INSTALL_HOME}/Scala
## Scala_HOME  scala根目录
SCALA_HOME=${INSTALL_HOME}/Scala/scala

if [ ! -d $LOG_DIR ];then
    mkdir -p $LOG_DIR;
fi

echo ""  | tee  -a  $LOG_FILE
echo "************************************************"
mkdir -p  ${SCALA_INSTALL_HOME}
cp -r ${SCALA_SOURCE_DIR}/scala ${SCALA_INSTALL_HOME}
chmod -R 755 ${SCALA_INSTALL_HOME}

echo "最终的scala 版本如下:" | tee -a $LOG_FILE
source /etc/profile
${SCALA_HOME}/bin/scala -version | tee -a $LOG_FILE


