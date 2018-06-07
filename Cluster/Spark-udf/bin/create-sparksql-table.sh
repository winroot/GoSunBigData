#!/bin/bash
################################################################################
## Copyright:    HZGOSUN Tech. Co, BigData
## Filename:     create-sparksql-table.sh
## Description:  创建动态库表的所有索引
## Author:       chenke
## Created:      2017-11-28
################################################################################

#set -x
#---------------------------------------------------------------------#
#                              定义变量                               #
#---------------------------------------------------------------------#
cd `dirname $0`
BIN_DIR=`pwd`                                   ### bin 目录
cd ..
SPARK_DIR=`pwd`                                 ###spark目录
cd ..
CLUSTER_DIR=`pwd`                               ###cluster目录
cd ..
OBJECT_DIR=`pwd`                                ### Real 根目录
COMMON_DIR=${OBJECT_DIR}/common                 ### common 目录
CONF_FILE=${COMMON_DIR}/conf/project-conf.properties

## bigdata cluster path
BIGDATA_CLUSTER_PATH=/opt/hzgc/bigdata
## bigdata hive path
SPARK_PATH=${BIGDATA_CLUSTER_PATH}/Spark/spark
## HBase_home
HBASE_HOME=${BIGDATA_CLUSTER_PATH}/HBase/hbase
## bigdata hadoop path
HADOOP_PATH=${BIGDATA_CLUSTER_PATH}/Hadoop/hadoop
## bigdata hive path
HIVE_PATH=${BIGDATA_CLUSTER_PATH}/Hive/hive
## udf function name
UDF_FUNCTION_NAME=compare
## udf class path
UDF_CLASS_PATH=com.hzgc.cluster.spark.udf.spark.UDFArrayCompare
## hdfs udf  path
HDFS_UDF_PATH=/user/hive/udf
## udf jar version
UDF_VERSION=`ls ${SPARK_DIR}/lib | grep ^spark-udf-[0-9].[0-9].[0-9].jar$`
## hdfs udf Absolute path
HDFS_UDF_ABSOLUTE_PATH=hdfs://hzgc/${HDFS_UDF_PATH}/${UDF_VERSION}
#####################################################################
# 函数名: create_person_table_mid_table
# 描述: 创建person表， mid_table 表格
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function create_person_table_mid_table() {
    ${SPARK_PATH}/bin/spark-sql -e "CREATE EXTERNAL TABLE IF NOT EXISTS default.person_table( \
                                    ftpurl        string, \
                                    ipcid         string, \
                                    feature       array<float>, \
                                    eyeglasses    int, \
                                    gender        int, \
                                    haircolor     int, \
                                    hairstyle     int, \
                                    hat           int, \
                                    huzi          int, \
                                    tie           int, \
                                    timeslot      int, \
                                    exacttime     Timestamp, \
                                    searchtype    string, \
                                    sharpness     int) \
                                    partitioned by (date string) \
                                    STORED AS PARQUET \
                                    LOCATION '/user/hive/warehouse/person_table';
                                    CREATE EXTERNAL TABLE IF NOT EXISTS default.mid_table( \
                                    ftpurl        string, \
                                    feature       array<float>, \
                                    eyeglasses    int, \
                                    gender        int, \
                                    haircolor     int, \
                                    hairstyle     int, \
                                    hat           int, \
                                    huzi          int, \
                                    tie           int, \
                                    timeslot      int, \
                                    exacttime     Timestamp, \
                                    searchtype    string, \
                                    date          string, \
                                    ipcid         string, \
                                    sharpness     int) \
                                    STORED AS PARQUET \
                                    LOCATION '/user/hive/warehouse/mid_table';
                                    show tables"

    if [ $? == 0 ];then
            echo "===================================="
            echo "创建person_table,mid_table成功"
            echo "===================================="
    else
            echo "========================================================="
            echo "创建person_table,mid_table失败,请看日志查找失败原因......"
            echo "========================================================="
    fi
}

function main() {
    create_person_table_mid_table
}

main