#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    phoenix_local.sh
## Description: 安装配置 phoenix
##              实现自动化的脚本
## Version:     1.0
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
## 配置文件目录
CONF_DIR=${ROOT_HOME}/conf
## 日志目录
LOG_DIR=${ROOT_HOME}/logs
## 安装日志
LOG_FILE=${LOG_DIR}/phoenixInstall.log
## 安装包目录
PHOENIX_SOURCE_DIR=${ROOT_HOME}/component/bigdata
## 最终安装的根目录，所有bigdata 相关的根目录
INSTALL_HOME=$(grep Install_HomeDir ${CONF_DIR}/cluster_conf.properties|cut -d '=' -f2)
## phoenix 集群节点
PHOENIX_HOST=`hostname -i`
## 安装目录
PHOENIX_INSTALL_HOME=${INSTALL_HOME}/Phoenix
##组件的根目录
PHOENIX_HOME=${PHOENIX_INSTALL_HOME}/phoenix
## hbase 安装目录
HBASE_INSTALL_HOME=${INSTALL_HOME}/Hbase
## hbase 根目录
HBASE_HOME=${HBASE_INSTALL_HOME}/hbase
## hbase-site.xml 文件
HBASE_SITE_FILE=${PHOENIX_HOME}/bin/hbase-site.xml
## <value>
VALUE="<value>"
## </value>
VALUE_END="</value>"

if [ ! -d $LOG_DIR ];then
    mkdir -p $LOG_DIR;
fi

## 首先检查本机上是否安装有phoenix 如果有，则删除本机的phoenix
if [ -e ${PHOENIX_HOME} ];then
    echo "删除原有 phoenix"
    rm -rf ${PHOENIX_HOME}
fi
mkdir -p ${PHOENIX_INSTALL_HOME}
cp -r ${PHOENIX_SOURCE_DIR}/phoenix ${PHOENIX_INSTALL_HOME}
chmod -R 755 ${PHOENIX_INSTALL_HOME}
ZK_LISTS=""${PHOENIX_HOST}:2181
HBASE_TMP_DIR=${HBASE_HOME}/tmp
HBASE_ZK_DATADIR=${HBASE_HOME}/hbase_zk_datadir
## hbase 存储目录
HBASE_DATA=${HBASE_HOME}/hzgc/hbase
## 配置hbase-site.xml
grep -q "<name>hbase.zookeeper.quorum</name>" ${HBASE_SITE_FILE}
if [[ $? -eq 0 ]]; then
    num1=$[ $(cat ${HBASE_SITE_FILE} | cat -n | grep "<name>hbase.zookeeper.quorum</name>" | awk '{print $1}') + 1 ]
    sed -i "${num1}c ${VALUE}${ZK_LISTS}${VALUE_END}" ${HBASE_SITE_FILE}
else echo "hbase.cluster.distributed 配置失败" | tee -a $LOG_FILE
fi

sed -i "s#hbase_tmp_dir#${HBASE_TMP_DIR}#g" ${HBASE_SITE_FILE}
sed -i "s#hbase_zookeeper_dataDir#${HBASE_ZK_DATADIR}#g" ${HBASE_SITE_FILE}
sed -i "s#hdfs://hzgc/hbase#${HBASE_DATA}#g" ${HBASE_SITE_FILE}

grep -q "hbase.cluster.distributed" ${HBASE_SITE_FILE}
if [[ $? -eq 0 ]]; then
    num2=$[ $(cat ${HBASE_SITE_FILE} | cat -n | grep  hbase.cluster.distributed | awk '{print $1}') + 1 ]
    sed -i "${num2}c ${VALUE}false${VALUE_END}" ${HBASE_SITE_FILE}
else echo "hbase.cluster.distributed 配置失败" | tee -a $LOG_FILE
fi
echo  "配置Hbase-site.xml done ......"  | tee -a $LOG_FILE

grep -q "dfs.replication" ${HBASE_SITE_FILE}
if [[ $? -eq 0 ]]; then
    num3=$[ $(cat ${HBASE_SITE_FILE} | cat -n | grep  dfs.replication | awk '{print $1}') + 1 ]
    sed -i "${num3}c ${VALUE}1${VALUE_END}" ${HBASE_SITE_FILE}
else echo "dfs.replication 配置失败" | tee -a $LOG_FILE
fi


