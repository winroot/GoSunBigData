#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    dos2unixInstall.sh
## Description: 安装 dos2unix 工具，解决windows下编辑过的脚本在linux下执行时格式不一致的问题
##              实现自动化的脚本
## Version:     2.0
## Author:      yinhang
## Created:     2018-07-28
################################################################################

#set -x

cd `dirname $0`
## 脚本所在目录
BIN_DIR=`pwd`
cd ../../..
## 安装包根目录
ROOT_HOME=`pwd`
## 安装日记目录
LOG_DIR=${ROOT_HOME}/logs
## 安装日记目录
LOG_FILE=${LOG_DIR}/dos2unixInstall.log
## 基础工具安装路径
INSTALL_HOME_BASIC=${ROOT_HOME}/component/basic_suports
## dos2unix rpm 软件最终目录
DOS2UNIX_RPM_INSTALL_HOME=${INSTALL_HOME_BASIC}/dos2unixRpm

if [ ! -d $LOG_DIR ];then
    mkdir -p $LOG_DIR;
fi

echo "**********************************************" | tee -a $LOG_FILE
echo "intall dos2unix in  `hostname -i`...... "  | tee -a $LOG_FILE
rpm -ivh ${DOS2UNIX_RPM_INSTALL_HOME}/dos2unix-3.1-37.el6.x86_64.rpm
which dos2unix
#rm -rf ${INSTALL_HOME_BASIC}

##把执行脚本的节点上的脚本转成unix编码
echo "开始转换脚本编码格式" | tee -a $LOG_FILE
dos2unix `find ${ROOT_HOME} -name '*.sh' -or -name '*.properties'`
dos2unix ${ROOT_HOME}/tool/*
echo "转换脚本编码格式完成" | tee -a $LOG_FILE

