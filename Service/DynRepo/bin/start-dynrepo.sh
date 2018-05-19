#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    springCloud start face
## Description: 启动dynrepo服务提取特征
## Author:      zhaozhe
## Created:     2018-05-19
################################################################################
#set -x

cd `dirname $0`
BIN_DIR=`pwd`    ##bin目录地址
cd ..
HOME_DIR=`pwd`    ##host目录地址
cd lib
LIB_DIR=`pwd`
DYNREPO_JAR_NAME=`ls | grep ^dynrepo-[0-9].[0-9].[0-9].jar$`
DYNREPO_JAR=${LIB_DIR}/${DYNREPO_JAR_NAME}
cd ..


#---------------------------------------------------------------------#
#                          springcloud配置参数                        #
#---------------------------------------------------------------------#
EUREKA_IP=172.18.18.201     ##注册中心的ip地址
EUREKA_PORT=9000
ES_HOST=172.18.18.100
ZOOKEEPER_HOST=172.18.18.100:2181


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

   nohup java -jar ${DYNREPO_JAR} --spring.profiles.active=pro  \
   --eureka.ip=${EUREKA_IP} \
   --eureka.port=${EUREKA_PORT} \
   --es.hosts=${ES_HOST} \
   --zookeeper.host=${ZOOKEEPER_HOST} 2&>1 &

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