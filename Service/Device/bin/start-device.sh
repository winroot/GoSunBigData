#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    springCloud start device
## Description: 启动 device服务
## Author:      chenke
## Created:     2018-05-19
################################################################################
#set -x

cd `dirname $0`
BIN_DIR=`pwd`                         ##bin目录地址
cd ..
DEVICE_DIR=`pwd`                     ##device目录地址
LIB_DIR=${DEVICE_DIR}/lib            ##lib目录地址
CONF_DIR=${DEVICE_DIR}/conf          ##conf目录地址
DEVICE_JAR_NAME=`ls ${LIB_DIR} | grep ^device-[0-9].[0-9].[0-9].jar$`          ##获取device的jar包名称
DEVICE_JAR=${LIB_DIR}/${DEVICE_JAR_NAME}                        ##获取jar包的全路径



#-----------------------------------------------------------------------------#
#                               springcloud配置参数                            #
#-----------------------------------------------------------------------------#
EUREKA_IP=172.18.18.201     ##注册中心的ip地址
SERVER_IP=172.18.18.104     ##服务的ip地址
EUREKA_PORT=9000            ##服务注册中心端口
ES_HOST=172.18.18.100
ZOOKEEPER_HOST=172.18.18.100:2181


#------------------------------------------------------------------------------#
#                                定义函数                                      #
#------------------------------------------------------------------------------#
#####################################################################
# 函数名: start_clustering
# 描述: 启动 springCloud device服务
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function start_springCloud()
{
   DEVICE_PID=`jps | grep ${DEVICE_JAR_NAME} | awk '{print $1}'`
   if [  -n "${DEVICE_PID}" ];then
      echo "Device service already started!!"
   else
      nohup java -jar ${DEVICE_JAR} --spring.profiles.active=pro \
      --eureka.ip=${EUREKA_IP} \
      --eureka.port=${EUREKA_PORT} \
      --server.ip=${SERVER_IP} \
      --es.host=${ES_HOST} \
      --zookeeper.host=${ZOOKEEPER_HOST} 2>&1 &
   fi
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
    start_springCloud
}

main