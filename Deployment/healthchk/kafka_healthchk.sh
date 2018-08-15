#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    kafka_healthchk.sh
## Description: 集群Kafka服务状态检查脚本.
## Version:     1.0
## Author:      zhangbaolin
## Created:     2018-6-25
################################################################################
#set -x
#set -e
cd `dirname $0`
## 脚本所在目录
BIN_DIR=`pwd`
cd ..
## 安装包根目录
ROOT_HOME=`pwd`
## 配置文件目录
CONF_DIR=${ROOT_HOME}/conf
# 验证kafka是否启动成功
source $(grep Source_File ${CONF_DIR}/cluster_conf.properties|cut -d '=' -f2)
xcall jps | grep -E 'Kafka|jps show as bellow'