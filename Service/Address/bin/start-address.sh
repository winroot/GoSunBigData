#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    springCloud start address
## Description: 启动 address服务
## Author:      chenke
## Created:     2018-05-19
################################################################################
#set -x

cd `dirname $0`
BIN_DIR=`pwd`                         ##bin目录地址
cd ..
ADDRESS_DIR=`pwd`                     ##address目录地址
LIB_DIR=${ADDRESS_DIR}/lib
ADDRESS_JAR_NAME=`ls ${LIB_DIR} | grep ^address-[0-9].[0-9].[0-9].jar$`          ##获取address的jar包名称
ADDRESS_JAR=${LIB_DIR}/${ADDRESS_JAR_NAME}                        ##获取jar包的全路径
CONF_DIR=${ADDRESS_DIR}/conf                                          ##conf目录地址



#-----------------------------------------------------------------------------#
#                               springcloud配置参数                            #
#-----------------------------------------------------------------------------#
EUREKA_IP=172.18.18.201     ##注册中心的ip地址
EUREKA_PORT=9000            ##服务注册中心端口
ZOOKEEPER_HOST=172.18.18.100:2181


#------------------------------------------------------------------------------#
#                                定义函数                                      #
#------------------------------------------------------------------------------#
#####################################################################
# 函数名: start_address
# 描述: 启动 springCloud address服务
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function start_springCloud()
{
   ADDRESS_PID=`jps | grep ${ADDRESS_JAR_NAME} | awk '{print $1}'`
   if [  -n "${ADDRESS_PID}" ];then
       echo "address service already started !!"
   else
       nohup java -jar ${ADDRESS_JAR} --spring.profiles.active=pro \
       --eureka.ip=${EUREKA_IP} \
       --eureka.port=${EUREKA_PORT} \
       --spring.cloud.config.enabled=false \
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