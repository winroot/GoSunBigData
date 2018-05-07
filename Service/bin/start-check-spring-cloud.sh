#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    start-check-spring-cloud.sh
## Description: 大数据spring cloud 守护脚本
## Author:      wujiaqi
## Created:     2018-01-08
################################################################################
#set -x  ## 用于调试用，不用的时候可以注释掉

#crontab 里面不会读取jdk环境变量的值
source /etc/profile

#set -x



#---------------------------------------------------------------------#
#                              定义变量                               #
#---------------------------------------------------------------------#
cd `dirname $0`
BIN_DIR=`pwd`                               ### bin 目录

cd ..
SERVICE_DIR=`pwd`                           ### service 目录
CONF_SERVICE_DIR=$SERVICE_DIR/conf          ### service 配置文件
LIB_STAREPO_DIR=${SERVICE_DIR}/starepo/lib  ### starepo lib
LOG_DIR=${SERVICE_DIR}/logs                 ### LOG 目录
CHECK_LOG_FILE=$LOG_DIR/check-spring-cloud.log

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
# 函数名: check_spring_cloud_for_starepo
# 描述: 把脚本定时执行，spring_cloud 服务是否挂掉，如果挂掉则重启。
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function check_spring_cloud_for_starepo()
{
    echo ""  | tee  -a  $CHECK_LOG_FILE
    echo "****************************************************"  | tee -a $CHECK_LOG_FILE
    echo "spring cloud procceding ing......................."  | tee  -a  $CHECK_LOG_FILE
    spring_pid=$(ps -ef | grep "com.hzgc.service.starepo.StaRepoApplication" | awk '{print $2}' | uniq)
    echo "spring cloud's pid is: ${spring_pid}"  | tee  -a  $CHECK_LOG_FILE
    if [ -n "${spring_pid}" ];then
        echo "spring cloud process is exit,do not need to do anything. exit with 0 " | tee -a $CHECK_LOG_FILE
    else
        echo "spring cloud process is not exit, just to restart spring cloud."  | tee -a $CHECK_LOG_FILE
        sh ${BIN_DIR}/start-spring-cloud.sh starepo
        echo "starting, please wait........" | tee -a $CHECK_LOG_FILE
        sleep 1m
        spring_pid_restart=$(ps -ef | grep "com.hzgc.service.starepo.StaRepoApplication" | awk '{print $2}' | uniq)
        if [ -z "${spring_pid_restart}" ];then
            echo "start spring cloud failed.....,retrying to start it second time" | tee -a $CHECK_LOG_FILE
            sh ${BIN_DIR}/start-spring-cloud.sh starepo
            echo "second try starting, please wait........" | tee -a $CHECK_LOG_FILE
            sleep 1m
            spring_pid_retry=$(ps -ef | grep "com.hzgc.service.starepo.StaRepoApplication" | awk '{print $2}' | uniq)
            if [ -z  "${spring_pid_retry}" ];then
                echo "retry start spring cloud failed, please check the config......exit with 1"  | tee -a $CHECK_LOG_FILE
            else
                echo "secondary try start ftp sucess. exit with 0." | tee -a $CHECK_LOG_FILE
            fi
        else
            echo "trying to restart spring cloud sucess. exit with 0."  | tee -a $CHECK_LOG_FILE
        fi
    fi
}

#####################################################################
# 函数名: check_spring_cloud_for_face
# 描述: 把脚本定时执行，spring_cloud 服务是否挂掉，如果挂掉则重启。
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function check_spring_cloud_for_face()
{
    echo ""  | tee  -a  $CHECK_LOG_FILE
    echo "****************************************************"  | tee -a $CHECK_LOG_FILE
    echo "spring cloud procceding ing......................."  | tee  -a  $CHECK_LOG_FILE
    spring_pid=$(ps -ef | grep "com.hzgc.service.starepo.FaceApplication" | awk '{print $2}' | uniq)
    echo "spring cloud's pid is: ${spring_pid}"  | tee  -a  $CHECK_LOG_FILE
    if [ -n "${spring_pid}" ];then
        echo "spring cloud process is exit,do not need to do anything. exit with 0 " | tee -a $CHECK_LOG_FILE
    else
        echo "spring cloud process is not exit, just to restart spring cloud."  | tee -a $CHECK_LOG_FILE
        sh ${BIN_DIR}/start-spring-cloud.sh face
        echo "starting, please wait........" | tee -a $CHECK_LOG_FILE
        sleep 1m
        spring_pid_restart=$(ps -ef | grep "com.hzgc.service.starepo.FaceApplication" | awk '{print $2}' | uniq)
        if [ -z "${spring_pid_restart}" ];then
            echo "start spring cloud failed.....,retrying to start it second time" | tee -a $CHECK_LOG_FILE
            sh ${BIN_DIR}/start-spring-cloud.sh face
            echo "second try starting, please wait........" | tee -a $CHECK_LOG_FILE
            sleep 1m
            spring_pid_retry=$(ps -ef | grep "com.hzgc.service.starepo.FaceApplication" | awk '{print $2}' | uniq)
            if [ -z  "${spring_pid_retry}" ];then
                echo "retry start spring cloud failed, please check the config......exit with 1"  | tee -a $CHECK_LOG_FILE
            else
                echo "secondary try start ftp sucess. exit with 0." | tee -a $CHECK_LOG_FILE
            fi
        else
            echo "trying to restart spring cloud sucess. exit with 0."  | tee -a $CHECK_LOG_FILE
        fi
    fi
}

#####################################################################
# 函数名: check_spring_cloud_for_dynrepo
# 描述: 把脚本定时执行，spring_cloud 服务是否挂掉，如果挂掉则重启。
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function check_spring_cloud_for_dynrepo()
{
    echo ""  | tee  -a  $CHECK_LOG_FILE
    echo "****************************************************"  | tee -a $CHECK_LOG_FILE
    echo "spring cloud procceding ing......................."  | tee  -a  $CHECK_LOG_FILE
    spring_pid=$(ps -ef | grep "com.hzgc.service.starepo.DynrepoApplication" | awk '{print $2}' | uniq)
    echo "spring cloud's pid is: ${spring_pid}"  | tee  -a  $CHECK_LOG_FILE
    if [ -n "${spring_pid}" ];then
        echo "spring cloud process is exit,do not need to do anything. exit with 0 " | tee -a $CHECK_LOG_FILE
    else
        echo "spring cloud process is not exit, just to restart spring cloud."  | tee -a $CHECK_LOG_FILE
        sh ${BIN_DIR}/start-spring-cloud.sh dynrepo
        echo "starting, please wait........" | tee -a $CHECK_LOG_FILE
        sleep 1m
        spring_pid_restart=$(ps -ef | grep "com.hzgc.service.starepo.DynrepoApplication" | awk '{print $2}' | uniq)
        if [ -z "${spring_pid_restart}" ];then
            echo "start spring cloud failed.....,retrying to start it second time" | tee -a $CHECK_LOG_FILE
            sh ${BIN_DIR}/start-spring-cloud.sh dynrepo
            echo "second try starting, please wait........" | tee -a $CHECK_LOG_FILE
            sleep 1m
            spring_pid_retry=$(ps -ef | grep "com.hzgc.service.starepo.DynrepoApplication" | awk '{print $2}' | uniq)
            if [ -z  "${spring_pid_retry}" ];then
                echo "retry start spring cloud failed, please check the config......exit with 1"  | tee -a $CHECK_LOG_FILE
            else
                echo "secondary try start ftp sucess. exit with 0." | tee -a $CHECK_LOG_FILE
            fi
        else
            echo "trying to restart spring cloud sucess. exit with 0."  | tee -a $CHECK_LOG_FILE
        fi
    fi
}

#####################################################################
# 函数名: check_spring_cloud_for_device
# 描述: 把脚本定时执行，spring_cloud 服务是否挂掉，如果挂掉则重启。
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function check_spring_cloud_for_device()
{
    echo ""  | tee  -a  $CHECK_LOG_FILE
    echo "****************************************************"  | tee -a $CHECK_LOG_FILE
    echo "spring cloud procceding ing......................."  | tee  -a  $CHECK_LOG_FILE
    spring_pid=$(ps -ef | grep "com.hzgc.service.starepo.DeviceApplication" | awk '{print $2}' | uniq)
    echo "spring cloud's pid is: ${spring_pid}"  | tee  -a  $CHECK_LOG_FILE
    if [ -n "${spring_pid}" ];then
        echo "spring cloud process is exit,do not need to do anything. exit with 0 " | tee -a $CHECK_LOG_FILE
    else
        echo "spring cloud process is not exit, just to restart spring cloud."  | tee -a $CHECK_LOG_FILE
        sh ${BIN_DIR}/start-spring-cloud.sh device
        echo "starting, please wait........" | tee -a $CHECK_LOG_FILE
        sleep 1m
        spring_pid_restart=$(ps -ef | grep "com.hzgc.service.starepo.DeviceApplication" | awk '{print $2}' | uniq)
        if [ -z "${spring_pid_restart}" ];then
            echo "start spring cloud failed.....,retrying to start it second time" | tee -a $CHECK_LOG_FILE
            sh ${BIN_DIR}/start-spring-cloud.sh device
            echo "second try starting, please wait........" | tee -a $CHECK_LOG_FILE
            sleep 1m
            spring_pid_retry=$(ps -ef | grep "com.hzgc.service.starepo.DeviceApplication" | awk '{print $2}' | uniq)
            if [ -z  "${spring_pid_retry}" ];then
                echo "retry start spring cloud failed, please check the config......exit with 1"  | tee -a $CHECK_LOG_FILE
            else
                echo "secondary try start ftp sucess. exit with 0." | tee -a $CHECK_LOG_FILE
            fi
        else
            echo "trying to restart spring cloud sucess. exit with 0."  | tee -a $CHECK_LOG_FILE
        fi
    fi
}

#####################################################################
# 函数名: check_spring_cloud_for_clustering
# 描述: 把脚本定时执行，spring_cloud 服务是否挂掉，如果挂掉则重启。
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function check_spring_cloud_for_clustering()
{
    echo ""  | tee  -a  $CHECK_LOG_FILE
    echo "****************************************************"  | tee -a $CHECK_LOG_FILE
    echo "spring cloud procceding ing......................."  | tee  -a  $CHECK_LOG_FILE
    spring_pid=$(ps -ef | grep "com.hzgc.service.starepo.ClusteringApplication" | awk '{print $2}' | uniq)
    echo "spring cloud's pid is: ${spring_pid}"  | tee  -a  $CHECK_LOG_FILE
    if [ -n "${spring_pid}" ];then
        echo "spring cloud process is exit,do not need to do anything. exit with 0 " | tee -a $CHECK_LOG_FILE
    else
        echo "spring cloud process is not exit, just to restart spring cloud."  | tee -a $CHECK_LOG_FILE
        sh ${BIN_DIR}/start-spring-cloud.sh clustering
        echo "starting, please wait........" | tee -a $CHECK_LOG_FILE
        sleep 1m
        spring_pid_restart=$(ps -ef | grep "com.hzgc.service.starepo.ClusteringApplication" | awk '{print $2}' | uniq)
        if [ -z "${spring_pid_restart}" ];then
            echo "start spring cloud failed.....,retrying to start it second time" | tee -a $CHECK_LOG_FILE
            sh ${BIN_DIR}/start-spring-cloud.sh clustering
            echo "second try starting, please wait........" | tee -a $CHECK_LOG_FILE
            sleep 1m
            spring_pid_retry=$(ps -ef | grep "com.hzgc.service.starepo.ClusteringApplication" | awk '{print $2}' | uniq)
            if [ -z  "${spring_pid_retry}" ];then
                echo "retry start spring cloud failed, please check the config......exit with 1"  | tee -a $CHECK_LOG_FILE
            else
                echo "secondary try start ftp sucess. exit with 0." | tee -a $CHECK_LOG_FILE
            fi
        else
            echo "trying to restart spring cloud sucess. exit with 0."  | tee -a $CHECK_LOG_FILE
        fi
    fi
}

#####################################################################
# 函数名: check_spring_cloud_for_address
# 描述: 把脚本定时执行，spring_cloud 服务是否挂掉，如果挂掉则重启。
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function check_spring_cloud_for_address()
{
    echo ""  | tee  -a  $CHECK_LOG_FILE
    echo "****************************************************"  | tee -a $CHECK_LOG_FILE
    echo "spring cloud procceding ing......................."  | tee  -a  $CHECK_LOG_FILE
    spring_pid=$(ps -ef | grep "com.hzgc.service.starepo.AddressApplication" | awk '{print $2}' | uniq)
    echo "spring cloud's pid is: ${spring_pid}"  | tee  -a  $CHECK_LOG_FILE
    if [ -n "${spring_pid}" ];then
        echo "spring cloud process is exit,do not need to do anything. exit with 0 " | tee -a $CHECK_LOG_FILE
    else
        echo "spring cloud process is not exit, just to restart spring cloud."  | tee -a $CHECK_LOG_FILE
        sh ${BIN_DIR}/start-spring-cloud.sh address
        echo "starting, please wait........" | tee -a $CHECK_LOG_FILE
        sleep 1m
        spring_pid_restart=$(ps -ef | grep "com.hzgc.service.starepo.AddressApplication" | awk '{print $2}' | uniq)
        if [ -z "${spring_pid_restart}" ];then
            echo "start spring cloud failed.....,retrying to start it second time" | tee -a $CHECK_LOG_FILE
            sh ${BIN_DIR}/start-spring-cloud.sh address
            echo "second try starting, please wait........" | tee -a $CHECK_LOG_FILE
            sleep 1m
            spring_pid_retry=$(ps -ef | grep "com.hzgc.service.starepo.AddressApplication" | awk '{print $2}' | uniq)
            if [ -z  "${spring_pid_retry}" ];then
                echo "retry start spring cloud failed, please check the config......exit with 1"  | tee -a $CHECK_LOG_FILE
            else
                echo "secondary try start ftp sucess. exit with 0." | tee -a $CHECK_LOG_FILE
            fi
        else
            echo "trying to restart spring cloud sucess. exit with 0."  | tee -a $CHECK_LOG_FILE
        fi
    fi
}

#####################################################################
# 函数名: check_all
# 描述: 检查所有spring cloud
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function check_all()
{
    check_spring_cloud_for_starepo
    check_spring_cloud_for_face
    check_spring_cloud_for_dynrepo
    check_spring_cloud_for_device
    check_spring_cloud_for_clustering
    check_spring_cloud_for_address
}

#####################################################################
# 函数名: main
# 描述: 模块功能main 入口，即程序入口, 用来监听整个大数据服务的情况。
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function main()
{
   while true
   do
       check_all
       sleep 5m
   done
}


# 主程序入口
main
