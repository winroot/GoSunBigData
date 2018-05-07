#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    stop-spring-cloud-all
## Description: 在配置节点下停止spring cloud
## Author:      wujiaqi
## Created:     2017-5-5
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
LOG_FILE=$LOG_DIR/stop-all-spring-cloud.log

cd ..
OBJECT_DIR=`pwd`                            ### RealTimeFaceCompare 目录
CONF_FILE=${OBJECT_DIR}/conf/project-conf.properties
OBJECT_LIB_DIR=${OBJECT_DIR}/lib            ### lib
OBJECT_JARS=`ls ${OBJECT_LIB_DIR} | grep .jar | awk '{print "'${OBJECT_LIB_DIR}'/"$0}'|tr "\n" ":"`

if [ ! -d $LOG_DIR ]; then
    mkdir $LOG_DIR;
fi

#---------------------------------------------------------------------#
#                              定义函数                                #
#---------------------------------------------------------------------#

#####################################################################
# 函数名: stop_all_spring_cloud
# 描述: 停止spring cloud
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function stop_all_spring_cloud()
{
    echo ""  | tee -a $LOG_FILE
    echo "**********************************************" | tee -a $LOG_FILE
    echo "" | tee -a $LOG_FILE
    ## 获取spring cloud节点IP
    SPRING_HOSTS=$(grep spring_cloud_servicenode ${CONF_FILE}|cut -d '=' -f2)
    spring_arr=(${SPRING_HOSTS//;/ })
    for spring_host in ${spring_arr[@]}
    do
        echo "${spring_host}节点下停止spring cloud进程................."  | tee  -a  $LOG_FILE
        ssh root@${spring_host}  "source /etc/profile;cd ${BIN_DIR};sh stop-spring-cloud.sh"
        if [ $? -eq 0 ];then
            echo  -e '${spring_host}节点下停止spring cloud成功\n'
        else
            echo  -e '${spring_host}节点下停止spring cloud失败 \n'
        fi
    done
    echo "停止spring cloud完毕......"  | tee  -a  $LOG_FILE
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
    stop_all_spring_cloud
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
