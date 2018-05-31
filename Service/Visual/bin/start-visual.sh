#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    springCloud start visual
## Description: 启动 visual服务
## Author:      chenke
## Created:     2018-05-19
################################################################################
#set -x

cd `dirname $0`
BIN_DIR=`pwd`                         ##bin目录地址
cd ..
VISUAL_DIR=`pwd`                     ##visual目录地址
LIB_DIR=${VISUAL_DIR}/lib            ##lib目录地址
CONF_DIR=${VISUAL_DIR}/conf          ##conf目录地址
VISUAL_JAR_NAME=`ls ${LIB_DIR} | grep ^visual-[0-9].[0-9].[0-9].jar$`          ##获取visual的jar包名称
VISUAL_JAR=${LIB_DIR}/${VISUAL_JAR_NAME}                        ##获取jar包的全路径



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
# 描述: 启动 springCloud visual服务
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function start_springCloud()
{
    VISUAL_PID=`jps | grep ${VISUAL_JAR_NAME} | awk '{print $1}'`
    if [ -n "${VISUAL_PID}" ];then
       echo "Visual service already started!!"
    else
       nohup java -jar ${VISUAL_JAR} --spring.profiles.active=pro  \
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