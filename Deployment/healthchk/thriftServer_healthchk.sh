#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    thriftServerServiceStatus.sh
## Description: 检查thriftServer服务状态
## Version:     2.4
## Author:      yinhang
## Created:     2018-06-30
################################################################################
#set -x  ##  用于调试用，不用的时候可以注释掉
#set -e
#---------------------------------------------------------------------#
#                              定义变量                                #
#---------------------------------------------------------------------#
cd `dirname $0`
## 脚本所在目录
SERVICE_STATUS_DIR=`pwd`
cd ..
## 安装包根目录
CLUSTER_HOME=`pwd`
## 配置文件目录
CONF_DIR=${CLUSTER_HOME}/conf
# 验证thriftserver是否启动成功
echo -e "********************验证thriftServer服务状态*********************"
source $(grep Source_File ${CONF_DIR}/cluster_conf.properties|cut -d '=' -f2)
xcall ps -ef | grep thriftserver