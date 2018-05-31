#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    springCloud start clustering
## Description: 启动 clustering服务
## Author:      chenke
## Created:     2018-05-19
################################################################################
#set -x

cd `dirname $0`
BIN_DIR=`pwd`                         ##bin目录地址
cd ..
CLUSTERING_DIR=`pwd`                     ##clustering目录地址
LIB_DIR=${CLUSTERING_DIR}/lib            ##lib目录地址
CONF_DIR=${CLUSTERING_DIR}/conf          ##conf目录地址
CLUSTERING_JAR_NAME=`ls ${LIB_DIR} | grep ^clustering-[0-9].[0-9].[0-9].jar$`          ##获取clustering的jar包名称
CLUSTERING_JAR=${LIB_DIR}/${CLUSTERING_JAR_NAME}                        ##获取jar包的全路径



#-----------------------------------------------------------------------------#
#                               springcloud配置参数                            #
#-----------------------------------------------------------------------------#
EUREKA_IP=172.18.18.201     ##注册中心的ip地址
EUREKA_PORT=9000            ##服务注册中心端口
ES_HOST=172.18.18.100


#------------------------------------------------------------------------------#
#                                定义函数                                      #
#------------------------------------------------------------------------------#
#####################################################################
# 函数名: start_clustering
# 描述: 启动 springCloud clustering服务
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function start_springCloud()
{
   CLUSTERING_PID=`jps | grep ${CLUSTERING_JAR_NAME} | awk '{print $1}'`
   if [  -n "${CLUSTERING_PID}" ];then
      echo "Clustering service already started!!"
   else
      nohup java -jar ${CLUSTERING_JAR} --spring.profiles.active=pro \
       --eureka.ip=${EUREKA_IP} \
       --eureka.port=${EUREKA_PORT} \
       --es.host=${ES_HOST}  2>&1 &
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