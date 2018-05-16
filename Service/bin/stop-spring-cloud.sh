#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    stop-spring-cloud
## Description: 关闭 spring cloud
## Author:      wujiaqi
## Created:     2018-5-4
################################################################################
#set -x  ## 用于调试用，不用的时候可以注释掉

#---------------------------------------------------------------------#
#                              定义变量                                #
#---------------------------------------------------------------------#

cd `dirname $0`
BIN_DIR=`pwd`                               ### bin 目录

cd ..
SERVICE_DIR=`pwd`                           ### service 目录
CONF_SERVICE_DIR=$SERVICE_DIR/conf          ### service 配置文件
LIB_STAREPO_DIR=${SERVICE_DIR}/starepo/lib  ### starepo lib
LOG_DIR=${SERVICE_DIR}/logs                 ### LOG 目录
LOG_FILE=${LOG_DIR}/stop-spring-cloud.log
cd ..
OBJECT_DIR=`pwd`                            ### RealTimeFaceCompare 目录
OBJECT_LIB_DIR=${OBJECT_DIR}/lib            ### lib
OBJECT_JARS=`ls ${OBJECT_LIB_DIR} | grep .jar | awk '{print "'${OBJECT_LIB_DIR}'/"$0}'|tr "\n" ":"`

if [ ! -d $LOG_DIR ]; then
    mkdir $LOG_DIR;
fi

#---------------------------------------------------------------------#
#                              定义函数                                #
#---------------------------------------------------------------------#


#####################################################################
# 函数名: stop_check_spring_cloud
# 描述:  停止check_spring_cloud的入口函数
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function stop_check_spring_cloud()
{
    echo ""  | tee -a $LOG_FILE
    echo "****************************************************"  | tee -a $LOG_FILE
    echo " start stop check_spring_cloud ......................." | tee  -a $LOG_FILE
    check_spring_pid=$(ps -ef | grep check-spring-cloud.sh |grep -v grep | awk  '{print $2}' | uniq)
    echo "check_spring_cloud's pid is: ${check_spring_pid}"  | tee -a $LOG_FILE
    if [ -n "${check_spring_pid}" ];then
        echo "check_spring_cloud is exit,exit with 0,kill check_spring_cloud now " | tee -a $LOG_FILE
        kill -9 ${check_spring_pid}
        sleep 5s
        check_spring_pid_restart=$(ps -ef | grep check-spring-cloud.sh |grep -v grep | awk  '{print $2}' | uniq)
        if [ -n "${check_spring_pid_restart}" ];then
            stop_check_dubbo=1
            echo "stop check_spring_cloud failure, retry it again."  | tee -a  $LOG_FILE
        else
            stop_check_dubbo=0
            echo "stop check_spring_cloud sucessed, just to start check_spring_cloud."  | tee -a  $LOG_FILE
        fi
    else
        echo "check_spring_cloud is not exit, just to start check_spring_cloud."   | tee -a $LOG_FILE
        stop_check_dubbo=0
    fi
}

#####################################################################
# 函数名: stop
# 描述: 脚本主要业务入口
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################

function stop()
{
     case $class in
         [sS][tT][aA][rR][eE][pP][oO] )
             sh ${SERVICE_DIR}/starepo/bin/stop-spring-cloud-starepo.sh;;
         [fF][aA][cC][eE] )
             sh ${SERVICE_DIR}/face/bin/stop-spring-cloud-face.sh;;
         [dD][yY][nN][rR][eE][pP][oO] )
             sh ${SERVICE_DIR}/dynrepo/bin/stop-spring-cloud-dynrepo.sh;;
         [dD][eE][vV][iI][cC][eE] )
             sh ${SERVICE_DIR}/device/bin/stop-spring-cloud-device.sh;;
         [cC][lL][uU][sS][tT][eE][rR][iI][nN][gG] )
             sh ${SERVICE_DIR}/clustering/bin/stop-spring-cloud-clustering.sh;;
         [aA][dD][dD][rR][eE][sS][sS] )
             sh ${SERVICE_DIR}/address/bin/stop-spring-cloud-address.sh;;
         [vV][iI][sS][uU][aA][lL] )
             sh ${SERVICE_DIR}/visual/bin/stop-spring-cloud-address.sh;;
     esac
}

#####################################################################
# 函数名: stop_all
# 描述: 停止所有Application
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function stop_all()
{
    SPRING_CLASS=$(grep spring_cloud_service_classes ${CONF_FILE}|cut -d '=' -f2)
    spring_arr=(${SPRING_CLASS//;/ })
    for spring_class in ${spring_arr[@]}
    do
        echo "停止${spring_class}................."  | tee  -a  $LOG_FILE
        class=${spring_class}
        stop
    done
    stop_check_spring_cloud
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
    stop_all
}

#---------------------------------------------------------------------#
#                              执行流程                                #
#---------------------------------------------------------------------#

## 打印时间
echo ""  | tee  -a  $LOG_FILE
echo ""  | tee  -a  $LOG_FILE
echo "==================================================="  | tee -a $LOG_FILE
echo "$(date "+%Y-%m-%d  %H:%M:%S")"                       | tee  -a  $LOG_FILE
main

set +x