#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    hbaseStart_local.sh
## Description: 启动hbase集群的脚本.
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
## 安装日志目录
LOG_FILE=${LOG_DIR}/hbaseStart.log
## 最终安装的根目录，所有bigdata 相关的根目录
INSTALL_HOME=$(grep Install_HomeDir ${CONF_DIR}/cluster_conf.properties|cut -d '=' -f2)
## hbase的安装节点
HBASE_HOSTNAME_LISTS=`hostname -i`

echo -e "启动HBase集群 \n"
${INSTALL_HOME}/HBase/hbase/bin/start-hbase.sh
	if [ $? -eq 0 ];then
	    echo  -e 'HBase start success \n'
	else
	    echo  -e 'HBase start failed \n'
	fi

# 验证HBase是否启动成功
echo -e "********************验证HBase是否启动成功*********************"
sleep 10s
jps | grep -E 'HMaster|HRegionServer|jps show as bellow'


