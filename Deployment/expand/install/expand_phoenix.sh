#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    expand_phoenix.sh
## Description: 扩展phoenix
##              实现自动化的脚本
## Version:     2.0
## Author:      zhangbaolin
## Created:     2018-07-20
################################################################################
#set -e
#set -x

cd `dirname $0`
## 脚本所在目录
BIN_DIR=`pwd`
cd ../..
## 安装包根目录
ROOT_HOME=`pwd`
## 配置文件目录
CONF_DIR=${ROOT_HOME}/conf
##扩展集群配置文件目录
EXPAND_CONF_DIR=${ROOT_HOME}/expand/conf
## 日记目录
LOG_DIR=${ROOT_HOME}/logs
## 安装日记
LOG_FILE=${LOG_DIR}/phoenixInstall.log
##  安装包目录
SOURCE_DIR=${ROOT_HOME}/component/bigdata
## 最终安装的根目录，所有bigdata 相关的根目录
INSTALL_HOME=$(grep Install_HomeDir ${CONF_DIR}/cluster_conf.properties|cut -d '=' -f2)
## 安装目录
PHOENIX_INSTALL_HOME=${INSTALL_HOME}/Phoenix
##组件的根目录
PHOENIX_HOME=${PHOENIX_INSTALL_HOME}/phoenix
##主配置文件
CLUSTER_CONF_FILE=${CONF_DIR}/cluster_conf.properties
##扩展配置文件
EXPAND_CONF_FILE=${EXPAND_CONF_DIR}/expand_conf.properties
## <value>
VALUE="<value>"
## </value>
VALUE_END="</value>"


## 设置和获取HBase 集群的配置
ZK_LIST=""
i=0
for host in $(grep  Zookeeper_InstallNode ${CLUSTER_CONF_FILE} | awk -F "=" '{print $2}' | awk -F ";" '{for(i=1;i<=NF;++i) print $i}');do
    if [ $i == 0 ];then
        ZK_LIST=${host}
    else
        TMP=",${host}"
        ZK_LIST=${ZK_LIST}${TMP}
    fi
    i=1;
done


## 解压phoenix tar 包
#cd ${SOURCE_DIR}
#rm -rf phoenix
#tar -xf phoenix.tar.gz
cd ${PHOENIX_HOME}/bin
## 配置hbase-site.xml
## cd phoenix/bin
num=$[`grep -n "hbase.zookeeper.quorum" hbase-site.xml | cut -d " " -f1`+1]
sed -i "c${num} ${VALUE}${ZK_LIST}${VALUE_END}" hbase-site.xml

## 分发phoenix
cd  ${SOURCE_DIR}
for host in $(grep Node_HostName  ${EXPAND_CONF_FILE} | awk -F "=" '{print $2}' | awk -F ";" '{for(i=1;i<=NF;++i) print $i}');do
    echo "往$host 分发phoenix 配置好的安装包，主要用来作为客户端shell 使用..."
    ssh $host "mkdir -p ${PHOENIX_INSTALL_HOME}"
    scp -r phoenix  $host:${PHOENIX_INSTALL_HOME} > /dev/null
done

set +x



