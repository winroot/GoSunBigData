#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    stop-spring-cloud-starepo.sh
## Description: 大数据spring cloud 守护脚本
## Author:      wujiaqi
## Created:     2018-01-08
################################################################################
#set -x  ## 用于调试用，不用的时候可以注释掉

#crontab 里面不会读取jdk环境变量的值
source /etc/profile

#set -x
#---------------------------------------------------------------------#
#                              定义变量                                #
#---------------------------------------------------------------------#

cd `dirname $0`
BIN_DIR=`pwd`                                   ### bin 目录

cd ..
STAREPO_DIR=`pwd`                               ### starepo 目录
LOG_DIR=${STAREPO_DIR}/logs                     ### LOG 目录
LOG_FILE=$LOG_DIR/check-spring-cloud.log

cd ..
SERVICE_DIR=`pwd`                               ### service 目录
CONF_SERVICE_DIR=$SERVICE_DIR/conf              ### service 配置文件

cd ..
OBJECT_DIR=`pwd`                                ### RealTimeFaceCompare 目录
OBJECT_LIB_DIR=${OBJECT_DIR}/lib                ### lib
OBJECT_JARS=`ls ${OBJECT_LIB_DIR} | grep .jar | awk '{print "'${OBJECT_LIB_DIR}'/"$0}'|tr "\n" ":"`

if [ ! -d $LOG_DIR ]; then
    mkdir $LOG_DIR;
fi
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
function stop_spring_cloud ()
{
    echo ""  | tee -a $LOG_FILE
    echo "****************************************************"  | tee -a $LOG_FILE
    echo "spring cloud procceding......................." | tee  -a $LOG_FILE
    ## 获取spring cloud 的 pid
    spring_pid=$(jps | grep "StaRepoApplication" | awk '{print $1}' | uniq)
    echo "spring's pid is: ${spring_pid}"  | tee -a $LOG_FILE
    if [ -n "${spring_pid}" ];then
        echo "spring cloud process is exit,exit with 0,kill spring cloud now " | tee -a $LOG_FILE
        kill -9 ${spring_pid}
        sleep 5s
        spring_pid=$(jps | grep "StaRepoApplication" | awk '{print $1}' | uniq)
        if [ -n "${spring_pid}" ];then
            echo "stop spring cloud failure, retry it again."  | tee -a  $LOG_FILE
        else
            echo "stop spring cloud sucessed, just to start spring cloud."  | tee -a  $LOG_FILE
        fi
    else
        echo "spring cloud process is not exit, just to start spring cloud."   | tee -a $LOG_FILE
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
    stop_spring_cloud
}

## 打印时间
echo ""  | tee  -a  ${LOG_FILE}
echo ""  | tee  -a  ${LOG_FILE}
echo "==================================================="  | tee -a ${LOG_FILE}
echo "$(date "+%Y-%m-%d  %H:%M:%S")"                       | tee  -a  ${LOG_FILE}
main

set +x