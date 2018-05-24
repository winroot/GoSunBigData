#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    create-static-table.sh
## Description: 静态库建表：objectinfo、srecord
## Author:      lidiliang
## Created:     2017-08-03
################################################################################
#set -x  ## 用于调试用，不用的时候可以注释掉

#---------------------------------------------------------------------#
#                              定义变量                               #
#---------------------------------------------------------------------#
cd `dirname $0`
BIN_DIR=`pwd`                           ### bin
cd ..
PHONIX_DIR=`pwd`                        ### phonix
cd ..
CLUSTER_DIR=`pwd`                       ### cluster
cd ..
OBJECT_DIR=`pwd`                        ### RealTimeFaceCompare

LOG_DIR=${PHONIX_DIR}/log              ### 集群Log日志
LOG_FILE=${LOG_DIR}/create-static-table.log

## bigdata cluster path
BIGDATA_CLUSTER_PATH=/opt/hzgc/bigdata
## Phoenix 客户端合并
PHOENIX_PATH=${BIGDATA_CLUSTER_PATH}/Phoenix/phoenix
## bigdata hadoop path
HADOOP_PATH=${BIGDATA_CLUSTER_PATH}/Hadoop/hadoop
## hdfs udf  path
HDFS_UDF_PATH=/user/phoenix/udf/facecomp

UDF_VERSION=`ls ${PHONIX_DIR}/lib | grep ^phonix-udf-[0-9].[0-9].[0-9].jar$`
## hdfs udf Absolute path
HDFS_UDF_ABSOLUTE_PATH=hdfs://hzgc/${HDFS_UDF_PATH}/${UDF_VERSION}

source /opt/hzgc/env_bigdata.sh

if [ ! -d $LOG_DIR ]; then
    mkdir $LOG_DIR;
fi


#####################################################################
# 函数名: create_static_repo
# 描述: 建立HBase 静态信息库的表格，OBJECTINFO 和SEARCHRECORDT
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function create_static_repo() {
    ## 创建person_table
    source /opt/hzgc/env_bigdata.sh
    ${PHOENIX_PATH}/bin/psql.py  ${PHONIX_DIR}/conf/staticrepo.sql

    if [ $? == 0 ];then
            echo "===================================="
            echo "创建objectinfo,searchrecord成功"
            echo "===================================="
    else
            echo "========================================================="
            echo "创建objectinfo,searchrecord失败,请看日志查找失败原因......"
            echo "========================================================="
    fi
}

#####################################################################
# 函数名: create_phoenix_udf
# 描述: 静态信息库phoenix 自定义函数添加
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function create_phoenix_udf() {
    ## 判断hdfs上/user/hive/udf目录是否存在
    ${HADOOP_PATH}/bin/hdfs dfs -test -e ${HDFS_UDF_PATH}
    if [ $? -eq 0 ] ;then
        echo "=================================="
        echo "${HDFS_UDF_PATH}已经存在"
        echo "=================================="
    else
        echo "=================================="
        echo "${HDFS_UDF_PATH}不存在,正在创建"
        echo "=================================="
        ${HADOOP_PATH}/bin/hdfs dfs -mkdir -p ${HDFS_UDF_PATH}
        if [ $? == 0 ];then
        echo "=================================="
        echo "创建${HDFS_UDF_PATH}目录成功......"
        echo "=================================="
    else
        echo "====================================================="
        echo "创建${HDFS_UDF_PATH}目录失败,请检查服务是否启动......"
        echo "====================================================="
        fi
    fi

    ## 上传udf到hdfs指定目录
    ${HADOOP_PATH}/bin/hdfs dfs -test -e ${HDFS_UDF_PATH}/${UDF_VERSION}
    if [ $? -eq 0 ] ;then
        echo "=================================="
        echo "${HDFS_UDF_PATH}/${UDF_VERSION}已经存在"
        echo "=================================="
    else
        echo "=================================="
        echo "${HDFS_UDF_PATH}/${UDF_VERSION}不存在,正在上传"
        echo "=================================="
        ${HADOOP_PATH}/bin/hdfs dfs -put ${PHONIX_DIR}/lib/${UDF_VERSION} ${HDFS_UDF_PATH}
        if [ $? == 0 ];then
            echo "===================================="
            echo "上传udf函数成功......"
            echo "===================================="
        else
            echo "===================================="
            echo "上传udf函数失败,请查找失败原因......"
            echo "===================================="
        fi
    fi

    ## 在hive中添加并注册udf函数
    ${PHOENIX_PATH}/bin/psql.py ${CONF_CLUSTER_DIR}/sql/phoenix-udf.sql
}

#####################################################################
# 函数名: main
# 描述: 脚本主要业务入口
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function main()
{
    create_phoenix_udf
    create_static_repo
}

## 脚本主要业务入口
main