#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    springCloud start face
## Description: 启动 face服务提取特征
## Author:      yansen
## Created:     2018-05-18
################################################################################
#set -x



cd `dirname $0`

BIN_DIR=`pwd`               ##bin目录地址
cd ..                    
FACE_DIR=`pwd`              ##face目录地址
LIB_DIR=${FACE_DIR}/lib               ##lib目录地址
CONF_DIR=${FACE_DIR}/conf             ##conf目录地址
FACE_JAR_NAME=`ls ${LIB_DIR} | grep ^face-[0-9].[0-9].[0-9].jar$`    ##获取jar包名称
FACE_JAR=${LIB_DIR}/${FACE_JAR_NAME}       ##获取jar包的全路径



#---------------------------------------------------------------------#
#                          springcloud配置参数                        #
#---------------------------------------------------------------------#
EUREKA_IP=172.18.18.201     ##注册中心的ip地址
EUREKA_PORT=9000         ##服务注册中心端口

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
    FACE_PID=`jps | grep ${FACE_JAR_NAME} | awk '{print $1}'`
    if [ -n "${FACE_PID}" ];then
       echo "Face service already started!!"
    else
       nohup java -jar ${FACE_JAR} --spring.profiles.active=pro \
       --eureka.ip=${EUREKA_IP} \
       --spring.cloud.config.enabled=false \
       --eureka.port=${EUREKA_PORT}  2>&1 &
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