#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    springCloud start starepo
## Description: 启动starepo服务提取特征
## Author:      chenke
## Created:     2018-05-19
################################################################################
#set -x

cd `dirname $0`
BIN_DIR=`pwd`    ##bin目录地址
cd ..
STAREPO_DIR=`pwd`    ##host目录地址
LIB_DIR=${STAREPO_DIR}/lib
STAREPO_JAR_NAME=`ls ${LIB_DIR} | grep ^starepo-[0-9].[0-9].[0-9].jar$`
STAREPO_JAR=${LIB_DIR}/${STAREPO_JAR_NAME}
CONF_DIR=${STAREPO_DIR}/conf

#---------------------------------------------------------------------#
#                          springcloud配置参数                        #
#---------------------------------------------------------------------#
EUREKA_IP=172.18.18.201     ##注册中心的ip地址
EUREKA_PORT=9000
ES_HOST=172.18.18.100
KAFKA_HOST=172.18.18.100:9092
ZOOKEEPER_HOST=172.18.18.100:2181
MYSQL_HOST=172.18.18.105


#---------------------------------------------------------------------#
#                              定义函数                               #
#---------------------------------------------------------------------#

#####################################################################
# 函数名: start_spring_cloud
# 描述: 启动 springCloud face服务
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function start_springCloud()
{
   STAREPO_PID=`jps | grep ${STAREPO_JAR_NAME} | awk '{print $1}'`
   if [ -n "${STAREPO_PID}" ];then
       echo "dynrepo service already started"
   else
       nohup java -jar ${STAREPO_JAR} --spring.profiles.active=pro  \
       --eureka.ip=${EUREKA_IP} \
       --eureka.port=${EUREKA_PORT} \
       --es.host=${ES_HOST} \
       --kafka.host=${KAFKA_HOST} \
       --mysql.host=${MYSQL_HOST} \
       --spring.cloud.config.enabled=false \
       --zookeeper.host=${ZOOKEEPER_HOST} 2>&1 &
   fi

}

#---------------------------------------------------------------------#
#                              定义函数                               #
#---------------------------------------------------------------------#

#####################################################################
# 函数名: start_spring_cloud
# 描述: 启动 springCloud face服务
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function prepare_resource_file()
{
  cp ${CONF_DIR}/hbase-site.xml .
  jar -uf ${STAREPO_JAR} hbase-site.xml
  rm -rf hbase-site.xml
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
    prepare_resource_file
    start_springCloud
}

main
