#!/bin/bash
################################################################################
##opyright:   HZGOSUN Tech. Co, BigData
## Filename:    springCloud stop visual
## Description:  停止visual服务
## Author:      chenke
## Created:     2018-05-19
################################################################################
#set -x              ##用于调试

cd `dirname $0`
BIN_DIR=`pwd`                           ##bin目录地址
cd ..
VISUAL_DIR=`pwd`                       ##visual目录地址
LIB_DIR=${VISUAL_DIR}/lib              ##lib目录地址
CONF_DIR=${VISUAL_DIR}/CONF            ##conf目录地址
VISUAL_JAR_NAME=`ls ${LIB_DIR} | grep ^visual-[0-9].[0-9].[0-9].jar$`
VISUAL_JAR_PID=`jps | grep $VISUAL_JAR_NAME} | awk '{print $1}'`


#---------------------------------------------------------------------#
#                              定义函数                                #
#---------------------------------------------------------------------#

#####################################################################
# 函数名:stop_spring_cloud
# 描述: 停止spring cloud
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function stop_springCloud()
{
    if [ -n "${VISUAL_JAR_PID}" ];then
       echo "Visual service is exist, exit with 0,kill service now!!"
  	   ##杀掉进程
	   kill -9 ${VISUAL_JAR_PID}
	   echo "stop service successfully!!"
	else
	   echo "Visual service is not start!!"
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
    stop_springCloud
}

main