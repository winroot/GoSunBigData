#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    hbaseStart.sh
## Description: 启动集群扩展节点regionserver服务的脚本.
## Version:     2.0
## Author:      zhangbaolin
## Editor:      mashencai
## Created:     2017-10-24
################################################################################
#set -e
#set -x

cd `dirname $0`
## 脚本所在目录
cd ..
BIN_DIR=`pwd`
cd ..
## 安装包根目录
ROOT_HOME=`pwd`
## 配置文件目录
CONF_DIR=${ROOT_HOME}/conf
##扩展集群配置文件目录
EXPAND_CONF_DIR=${BIN_DIR}/conf
## 安装日记目录
LOG_DIR=${ROOT_HOME}/logs
## 安装日记目录
LOG_FILE=${LOG_DIR}/hbaseStart.log
## 最终安装的根目录，所有bigdata 相关的根目录
INSTALL_HOME=$(grep Install_HomeDir ${CONF_DIR}/cluster_conf.properties|cut -d '=' -f2)
## 集群扩展节点
EXPAND_NODE=$(grep Node_HostName ${EXPAND_CONF_DIR}/expand_conf.properties | cut -d '=' -f2)
EXPAND_NODE_ARRY=(${EXPAND_NODE//;/ })

echo -e "启动HBase集群 \n"

for node in ${EXPAND_NODE_ARRY[@]}
do
    ssh root@$node "sh ${INSTALL_HOME}/HBase/hbase/bin/hbase-daemon.sh start regionserver"
	if [ $? -eq 0 ];then
	    echo -e 'hbase success \n'
	else
	    echo -e 'hbase failed \n'
	fi
done

sleep 5s


# 验证HBase是否启动成功
echo -e "********************验证HRegionServer是否启动成功*********************"
sleep 7s
source $(grep Source_File ${CONF_DIR}/cluster_conf.properties|cut -d '=' -f2)
xcall jps | grep -E 'HRegionServer|jps show as bellow'
#xcall jps | grep HMaster
#xcall jps | grep HRegionServer

