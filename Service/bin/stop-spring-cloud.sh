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
# 函数名:stop_spring_cloud_for_starepo
# 描述: 停止spring cloud
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function stop_spring_cloud_for_starepo ()
{
    echo ""  | tee -a $LOG_FILE
    echo "****************************************************"  | tee -a $LOG_FILE
    echo "spring cloud procceding......................." | tee  -a $LOG_FILE
    ## 获取spring cloud 的 pid
    # spring_pid=$(lsof  -i | grep 20881  | awk  '{print $2}' | uniq)
    spring_pid=$(ps -ef | grep "com.hzgc.service.starepo.StaRepoApplication" | awk '{print $2}' | uniq)
    echo "spring's pid is: ${spring_pid}"  | tee -a $LOG_FILE
    if [ -n "${spring_pid}" ];then
        echo "spring cloud process is exit,exit with 0,kill spring cloud now " | tee -a $LOG_FILE
        kill -9 ${spring_pid}
        sleep 5s
        spring_pid=$(ps -ef | grep "com.hzgc.service.starepo.StaRepoApplication" | awk '{print $2}' | uniq)
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
# 函数名:stop_spring_cloud_for_face
# 描述: 停止spring cloud
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function stop_spring_cloud_for_face ()
{
    echo ""  | tee -a $LOG_FILE
    echo "****************************************************"  | tee -a $LOG_FILE
    echo "spring cloud procceding......................." | tee  -a $LOG_FILE
    ## 获取spring cloud 的 pid
    # spring_pid=$(lsof  -i | grep 20881  | awk  '{print $2}' | uniq)
    spring_pid=$(ps -ef | grep "com.hzgc.service.face.FaceApplication" | awk '{print $2}' | uniq)
    echo "spring's pid is: ${spring_pid}"  | tee -a $LOG_FILE
    if [ -n "${spring_pid}" ];then
        echo "spring cloud process is exit,exit with 0,kill spring cloud now " | tee -a $LOG_FILE
        kill -9 ${spring_pid}
        sleep 5s
        spring_pid=$(ps -ef | grep "com.hzgc.service.face.FaceApplication" | awk '{print $2}' | uniq)
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
# 函数名:stop_spring_cloud_for_dynrepo
# 描述: 停止spring cloud
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function stop_spring_cloud_for_dynrepo ()
{
    echo ""  | tee -a $LOG_FILE
    echo "****************************************************"  | tee -a $LOG_FILE
    echo "spring cloud procceding......................." | tee  -a $LOG_FILE
    ## 获取spring cloud 的 pid
    # spring_pid=$(lsof  -i | grep 20881  | awk  '{print $2}' | uniq)
    spring_pid=$(ps -ef | grep "com.hzgc.service.dynrepo.DynrepoApplication" | awk '{print $2}' | uniq)
    echo "spring's pid is: ${spring_pid}"  | tee -a $LOG_FILE
    if [ -n "${spring_pid}" ];then
        echo "spring cloud process is exit,exit with 0,kill spring cloud now " | tee -a $LOG_FILE
        kill -9 ${spring_pid}
        sleep 5s
        spring_pid=$(ps -ef | grep "com.hzgc.service.dynrepo.DynrepoApplication" | awk '{print $2}' | uniq)
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
# 函数名:stop_spring_cloud_for_device
# 描述: 停止spring cloud
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function stop_spring_cloud_for_device ()
{
    echo ""  | tee -a $LOG_FILE
    echo "****************************************************"  | tee -a $LOG_FILE
    echo "spring cloud procceding......................." | tee  -a $LOG_FILE
    ## 获取spring cloud 的 pid
    # spring_pid=$(lsof  -i | grep 20881  | awk  '{print $2}' | uniq)
    spring_pid=$(ps -ef | grep "com.hzgc.service.device.DeviceApplication" | awk '{print $2}' | uniq)
    echo "spring's pid is: ${spring_pid}"  | tee -a $LOG_FILE
    if [ -n "${spring_pid}" ];then
        echo "spring cloud process is exit,exit with 0,kill spring cloud now " | tee -a $LOG_FILE
        kill -9 ${spring_pid}
        sleep 5s
        spring_pid=$(ps -ef | grep "com.hzgc.service.device.DeviceApplication" | awk '{print $2}' | uniq)
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
# 函数名:stop_spring_cloud_for_clustering
# 描述: 停止spring cloud
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function stop_spring_cloud_for_clustering ()
{
    echo ""  | tee -a $LOG_FILE
    echo "****************************************************"  | tee -a $LOG_FILE
    echo "spring cloud procceding......................." | tee  -a $LOG_FILE
    ## 获取spring cloud 的 pid
    # spring_pid=$(lsof  -i | grep 20881  | awk  '{print $2}' | uniq)
    spring_pid=$(ps -ef | grep "com.hzgc.service.clustering.ClusteringApplication" | awk '{print $2}' | uniq)
    echo "spring's pid is: ${spring_pid}"  | tee -a $LOG_FILE
    if [ -n "${spring_pid}" ];then
        echo "spring cloud process is exit,exit with 0,kill spring cloud now " | tee -a $LOG_FILE
        kill -9 ${spring_pid}
        sleep 5s
        spring_pid=$(ps -ef | grep "com.hzgc.service.clustering.ClusteringApplication" | awk '{print $2}' | uniq)
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
# 函数名:stop_spring_cloud_for_address
# 描述: 停止spring cloud
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function stop_spring_cloud_for_address ()
{
    echo ""  | tee -a $LOG_FILE
    echo "****************************************************"  | tee -a $LOG_FILE
    echo "spring cloud procceding......................." | tee  -a $LOG_FILE
    ## 获取spring cloud 的 pid
    # spring_pid=$(lsof  -i | grep 20881  | awk  '{print $2}' | uniq)
    spring_pid=$(ps -ef | grep "com.hzgc.service.address.FtpApplication" | awk '{print $2}' | uniq)
    echo "spring's pid is: ${spring_pid}"  | tee -a $LOG_FILE
    if [ -n "${spring_pid}" ];then
        echo "spring cloud process is exit,exit with 0,kill spring cloud now " | tee -a $LOG_FILE
        kill -9 ${spring_pid}
        sleep 5s
        spring_pid=$(ps -ef | grep "com.hzgc.service.address.FtpApplication" | awk '{print $2}' | uniq)
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
    check_spring_pid=$(ps -ef | grep start-check-spring-cloud.sh |grep -v grep | awk  '{print $2}' | uniq)
    echo "check_spring_cloud's pid is: ${check_spring_pid}"  | tee -a $LOG_FILE
    if [ -n "${check_spring_pid}" ];then
        echo "check_spring_cloud is exit,exit with 0,kill check_spring_cloud now " | tee -a $LOG_FILE
        kill -9 ${check_spring_pid}
        sleep 5s
        check_spring_pid_restart=$(ps -ef | grep start-check-spring-cloud.sh |grep -v grep | awk  '{print $2}' | uniq)
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
# 函数名: main
# 描述: 脚本主要业务入口
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function main()
{
    stop_spring_cloud_for_starepo
    stop_spring_cloud_for_face
    stop_spring_cloud_for_dynrepo
    stop_spring_cloud_for_device
    stop_spring_cloud_for_clustering
    stop_spring_cloud_for_address
}

#---------------------------------------------------------------------#
#                              执行流程                                #
#---------------------------------------------------------------------#

## 打印时间
echo ""  | tee  -a  $LOG_FILE
echo ""  | tee  -a  $LOG_FILE
echo "==================================================="  | tee -a $LOG_FILE
echo "$(date "+%Y-%m-%d  %H:%M:%S")"                       | tee  -a  $LOG_FILE
echo "开始配置service中的conf文件"                       | tee  -a  $LOG_FILE
main

set +x