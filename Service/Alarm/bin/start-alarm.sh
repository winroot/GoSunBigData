#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    springCloud start alarm
## Description: 启动 alarm服务
## Author:      chenke
## Created:     2018-05-19
################################################################################
#set -x

cd `dirname $0`
BIN_DIR=`pwd`                         ##bin目录地址
cd ..
ALARM_DIR=`pwd`                     ##alarm目录地址
LIB_DIR=${ALARM_DIR}/lib                                          ##lib目录地址
ALARM_JAR_NAME=`ls ${LIB_DIR}  | grep ^alarm-[0-9].[0-9].[0-9].jar$`          ##获取alarm的jar包名称
ALARM_JAR=${LIB_DIR}/${ALARM_JAR_NAME}                        ##获取jar包的全路径
CONF_DIR=${ALARM_DIR}/conf                                        ##conf目录地址



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
# 函数名: start_alarm
# 描述: 启动 springCloud alarm服务
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function start_springCloud()
{
   ALARM_PID=`jps | grep ${ALARM_JAR_NAME} | awk '{print $1}'`
   if [  -n "${ALARM_PID}" ];then
      echo "Alarm service already started!!"
   else
      nohup java -jar ${ALARM_JAR} --spring.profiles.active=pro  \
      --eureka.ip=${EUREKA_IP} \
      --eureka.port=${EUREKA_PORT} \
      --server.ip=${SERVER_IP} \
      --es.hosts=${ES_HOST} \
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