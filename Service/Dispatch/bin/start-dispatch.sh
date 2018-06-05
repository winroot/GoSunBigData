#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    springCloud start dispatch
## Description: 启动 dispatch服务
## Author:      chenke
## Created:     2018-05-19
################################################################################
#set -x

cd `dirname $0`
BIN_DIR=`pwd`                         ##bin目录地址
cd ..
DISPATCH_DIR=`pwd`                     ##dispatch目录地址
LIB_DIR=${DISPATCH_DIR}/lib            ##lib目录地址
CONF_DIR=${DISPATCH_DIR}/conf          ##conf目录地址
DISPATCH_JAR_NAME=`ls ${LIB_DIR} | grep ^dispatch-[0-9].[0-9].[0-9].jar$`          ##获取dispatch的jar包名称
DISPATCH_JAR=${LIB_DIR}/${DISPATCH_JAR_NAME}                        ##获取jar包的全路径



#-----------------------------------------------------------------------------#
#                               springcloud配置参数                            #
#-----------------------------------------------------------------------------#
EUREKA_IP=172.18.18.201     ##注册中心的ip地址
EUREKA_PORT=9000            ##服务注册中心端口


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
   DISPATCH_PID=`jps | grep ${DISPATCH_JAR_NAME} | awk '{print $1}'`
   if [  -n "${DISPATCH_PID}" ];then
      echo "Dispatch service already started!!"
   else
      nohup java -jar ${DISPATCH_JAR} --spring.profiles.active=pro \
      --eureka.ip=${EUREKA_IP} \
      --eureka.port=${EUREKA_PORT} \
      --spring.cloud.config.enabled=false  2>&1 &
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