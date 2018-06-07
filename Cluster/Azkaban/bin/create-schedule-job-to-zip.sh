#!/bin/bash
########################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    schema-merge-parquet-file.sh
## Description: 将定时任务生成job文件并打包成zip包
## Author:      chenke
## Created:     2018-03-27
#########################################################################
#set -x ##用于调试使用，不用的时候可以注释掉

#----------------------------------------------------------------------#
#                              定义变量                                #
#----------------------------------------------------------------------#
cd `dirname $0`
BIN_DIR=`pwd`   ###bin目录
cd ..
AZKABAN_DIR=`pwd`  ###azkaban目录
LOG_DIR=${AZKABAN_DIR}/logs  ###集群log日志目录
LOG_FILE=${LOG_DIR}/create-schedule-job-to-zip.log  ##log日志文件
SCHEMA_FILE="schema-merge-parquet-file.sh"
OFFLINE_FILE="start-face-offline-alarm-job.sh"
DYNAMICSHOW_TABLE="get-dynamicshow-table-run.sh"

cd ..
cd ..
OBJECT_DIR=`pwd`                                 ## 根目录
CLUSTER_BIN_DIR=/opt/RealTimeFaceCompare/cluster/spark/bin
SERVICE_BIN_DIR=/opt/RealTimeFaceCompare/cluster/es/bin
AZKABAN_BIN_DIR=/opt/RealTimeFaceCompare/cluster/azkaban/bin

cd ${CLUSTER_BIN_DIR}  ##进入cluster的bin目录
mkdir -p schema-parquet-one-hour
if [ ! -f "$SCHEMA_FILE" ]; then
   echo "The schema-merge-parquet-file.sh is not exist!!!"
else
   touch mid_table-one-hour.job     ##创建mid_table-one-hour.job文件
   echo "type=command" >> mid_table-one-hour.job
   echo "cluster_home=/opt/RealTimeFaceCompare/cluster/spark/bin" >> mid_table-one-hour.job
   echo "command=sh \${cluster_home}/schema-merge-parquet-file.sh mid_table" >> mid_table-one-hour.job

   touch person_table-one-hour.job  ##创建person_table-one-hour.job文件
   echo "type=command" >> person_table-one-hour.job
   echo "cluster_home=/opt/RealTimeFaceCompare/cluster/spark/bin" >> person_table-one-hour.job
   echo "command=sh \${cluster_home}/schema-merge-parquet-file.sh person_table now" >> person_table-one-hour.job
   echo "dependencies=mid_table-one-hour" >> person_table-one-hour.job

   touch person_table_one-day.job  ##创建person_table_one-day.job文件
   echo "type=command" >> person_table_one-day.job
   echo "cluster_home=/opt/RealTimeFaceCompare/cluster/spark/bin" >> person_table_one-day.job
   echo "command=sh \${cluster_home}/schema-merge-parquet-file.sh person_table before" >> person_table_one-day.job

fi
if [ ! -f "$OFFLINE_FILE" ]; then
   echo "The start-face-offline-alarm-job.sh is not exist!!!"
else
   touch start-face-offline-alarm-job.job  ##创建离线告警的job文件
   echo "type=command" >> start-face-offline-alarm-job.job
   echo "cluster_home=/opt/RealTimeFaceCompare/cluster/spark/bin" >> start-face-offline-alarm-job.job
   echo "command=sh \${cluster_home}/start-face-offline-alarm-job.sh" >> start-face-offline-alarm-job.job
fi

cd ${CLUSTER_BIN_DIR}
mv mid_table-one-hour.job person_table-one-hour.job  schema-parquet-one-hour
zip schema-parquet-one-hour.zip schema-parquet-one-hour/*
zip person_table_one-day.job.zip person_table_one-day.job
zip start-face-offline-alarm-job_oneday.job.zip start-face-offline-alarm-job.job
rm -rf person_table_one-day.job start-face-offline-alarm-job.job schema-parquet-one-hour

cd ${AZKABAN_DIR}
mkdir -p zip
mv ${CLUSTER_BIN_DIR}/schema-parquet-one-hour.zip ${CLUSTER_BIN_DIR}/person_table_one-day.job.zip ${CLUSTER_BIN_DIR}/start-face-offline-alarm-job_oneday.job.zip zip