#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    config-projectconf
## Description: 一键配置脚本：执行common、cluster、service的一键配置脚本
## Author:      chenke
## Created:     2018-05-28
################################################################################
#set -x  ## 用于调试用，不用的时候可以注释掉

#---------------------------------------------------------------------#
#                              定义变量                                #
#---------------------------------------------------------------------#

cd `dirname $0`
BIN_DIR=`pwd`                                         ### bin目录：脚本所在目录
cd ..
COMMON_DIR=`pwd`                                      ### common模块部署目录
CONF_COMMON_DIR=$COMMON_DIR/conf                      ### 配置文件目录
CONF_FILE=$CONF_COMMON_DIR/project-conf.properties    ### 项目配置文件

LOG_DIR=$COMMON_DIR/logs                              ### log日志目录
LOG_FILE=$LOG_DIR/config-project.log                  ### log日志目录
cd ..
OBJECT_DIR=`pwd`                                      ### 项目根目录

SPARK_DIR=$OBJECT_DIR/cluster/spark                 ### spark模块部署目录
SERVICE_DIR=$OBJECT_DIR/service                       ### service模块部署目录
ADDRESS_DIR=$SERVICE_DIR/address                     ###address模块部署目录
ALARM_DIR=$SERVICE_DIR/alarm                         ###alarm模块部署目录
CLUSTERING_DIR=$SERVICE_DIR/clustering               ###clustering模块部署目录
DISPATCH_DIR=$SERVICE_DIR/dispatch                   ###dispatch模块部署目录
DYNREPO_DIR=$SERVICE_DIR/dynRepo                     ###dynrepo模块部署目录
FACE_DIR=$SERVICE_DIR/face                           ###face模块部署目录
STAREPO_DIR=$SERVICE_DIR/staRepo                     ###starepo模块目录
VISUAL_DIR=$SERVICE_DIR/visual                       ###visual模块目录

CLUSTER_SCRPIT=$SPARK_DIR/bin/config-clusterconf.sh ### 一键配置spark脚本
SERVICE_SCRPIT=$SERVICE_DIR/bin/config-serviceconf.sh ### 一键配置service脚本

SPARKLOG_DIR=$SPARK_DIR/logs                          ### spark的log日志目录
SERVICE_LOG_DIR=$SERVICE_DIR/logs                     ### service的log日志目录

# 创建日志目录
mkdir -p $LOG_DIR
mkdir -p $SPARKLOG_DIR
mkdir -p $SERVICE_LOG_DIR



#####################################################################
# 函数名: distribute_common
# 描述: 将common文件夹分发到所有节点
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function distribute_common()
{
    echo ""  | tee -a $LOG_FILE
    echo "**********************************************" | tee -a $LOG_FILE
    echo "" | tee -a $LOG_FILE
    echo "分发common......"  | tee  -a  $LOG_FILE
    
	CLUSTER_HOSTNAME_LISTS=$(grep cluster_hostname ${CONF_FILE}|cut -d '=' -f2)
	CLUSTER_HOSTNAME_ARRY=(${CLUSTER_HOSTNAME_LISTS//;/ })
    for hostname in ${CLUSTER_HOSTNAME_ARRY[@]}
    do
        ssh root@${hostname} "if [ ! -x "${OBJECT_DIR}" ]; then mkdir "${OBJECT_DIR}"; fi"
        rsync -rvl ${OBJECT_DIR}/common   root@${hostname}:${OBJECT_DIR}  >/dev/null
        ssh root@${hostname}  "chmod -R 755   ${OBJECT_DIR}/common"
        echo "${hostname}上分发common完毕......"  | tee  -a  $LOG_FILE
    done 
    
    echo "配置完毕......"  | tee  -a  $LOG_FILE
}

#####################################################################
# 函数名: distribute_service
# 描述: 将service下各个组件分发到需要的节点下
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function distribute_service()
{
    echo "" | tee -a $LOG_FILE
    echo "**************************************************" | tee -a $LOG_FILE
    echo "" | tee -a $LOG_FILE
    echo "分发service的各个组件......" | tee -a $LOG_FILE

    ##开始分发address
    ADDRESS_HOST_LISTS=$(grep address_distribution ${CONF_FILE} | cut -d '=' -f2)
    ADDRESS_HOST_ARRAY=(${ADDRESS_HOST_LISTS//;/ })
    for hostname in ${ADDRESS_HOST_ARRAY[@]}
    do
      ssh root@${hostname} "if [ ! -x "${SERVICE_DIR}" ];then mkdir -p "${SERVICE_DIR}"; fi"
      rsync -rvl ${ADDRESS_DIR} root@${hostname}:${SERVICE_DIR} >/dev/null
      ssh root@${hostname} "chmod -R 755 ${ADDRESS_DIR}"
      echo "${hostname}上分发address完毕........" | tee -a $LOG_FILE
    done

    ##开始分发alarm
    ALARM_HOST_LISTS=$(grep alarm_distribution ${CONF_FILE} | cut -d '=' -f2)
    ALARM_HOST_ARRAY=(${ALARM_HOST_LISTS//;/ })
    for hostname in ${ALARM_HOST_ARRAY[@]}
    do
      ssh root@${hostname} "if [ ! -x "${SERVICE_DIR}" ];then mkdir -p "${SERVICE_DIR}"; fi"
      rsync -rvl ${ALARM_DIR} root@${hostname}:${SERVICE_DIR} >/dev/null
      ssh root@${hostname} "chmod -R 755 ${ALARM_DIR}"
      echo "${hostname}上分发alarm完毕........" | tee -a $LOG_FILE
    done

    ##开始分发clustering
    CLUSTERING_HOST_LISTS=$(grep clustering_distribution ${CONF_FILE} | cut -d '=' -f2)
    CLUSTERING_HOST_ARRAY=(${CLUSTERING_HOST_LISTS//;/ })
    for hostname in ${CLUSTERING_HOST_ARRAY[@]}
    do
      ssh root@${hostname} "if [ ! -x "${SERVICE_DIR}" ];then mkdir -p "${SERVICE_DIR}"; fi"
      rsync -rvl ${CLUSTERING_DIR} root@${hostname}:${SERVICE_DIR} >/dev/null
      ssh root@${hostname} "chmod -R 755 ${CLUSTERING_DIR}"
      echo "${hostname}上分发clustering完毕......." | tee -a $LOG_FILE
    done

    ##开始分发dispatch
    DISPATCH_HOST_LISTS=$(grep dispatch_distribution ${CONF_FILE} | cut -d '=' -f2)
    DISPATCH_HOST_ARRAY=(${DISPATCH_HOST_LISTS//;/ })
    for hostname in ${DISPATCH_HOST_ARRAY[@]}
    do
      ssh root@${hostname} "if [ ! -x "${SERVICE_DIR}" ];then mkdir -p "${SERVICE_DIR}";fi"
      rsync -rvl ${DISPATCH_DIR} root@${hostname}:${SERVICE_DIR} >/dev/null
      ssh root@${hostname} "chmod -R 755 ${DISPATCH_DIR}"
      echo "${hostname}上分发dispatch完毕........." | tee -a $LOG_FILE
    done

    ##开始分发dynrepo
    DYNREPO_HOST_LISTS=$(grep dynrepo_distribution ${CONF_FILE} | cut -d '=' -f2)
    DYNREPO_HOST_ARRAY=(${DYNREPO_HOST_LISTS//;/ })
    for hostname in ${DYNREPO_HOST_ARRAY[@]}
    do
       ssh root@${hostname} "if [ ! -x "${SERVICE_DIR}" ];then mkdir -p "${SERVICE_DIR}";fi"
       rsync -rvl ${DYNREPO_DIR} root@${hostname}:${SERVICE_DIR} >/dev/null
       ssh root@${hostname} "chmod -R 755 ${DYNREPO_DIR}"
       echo "${hostname}上分发dynrepo完毕........." | tee -a $LOG_FILE
    done

     ##开始分发face
     FACE_HOST_LISTS=$(grep face_distribution ${CONF_FILE} | cut -d '=' -f2)
     FACE_HOST_ARRAY=(${FACE_HOST_LISTS//;/ })
     for hostname in ${FACE_HOST_ARRAY[@]}
     do
        ssh root@${hostname} "if [ ! -x "${SERVICE_DIR}" ];then mkdir -p "${SERVICE_DIR}";fi"
        rsync -rvl ${FACE_DIR} root@${hostname}:${SERVICE_DIR} >/dev/null
        ssh root@${hostname} "chmod -R 755 ${FACE_DIR}"
        echo "${hostname}上分发face完毕......." | tee -a $LOG_FILE
     done

     ##开始分发starepo
     STAREPO_HOST_LISTS=$(grep starepo_distribution ${CONF_FILE} | cut -d '=' -f2)
     STAREPO_HOST_ARRAY=(${STAREPO_HOST_LISTS//;/ })
     for hostname in ${STAREPO_HOST_ARRAY[@]}
     do
        ssh root@${hostname} "if [ ! -x "${SERVICE_DIR}" ];then mkdir -p "${SERVICE_DIR}";fi"
        rsync -rvl ${STAREPO_DIR} root@${hostname}:${SERVICE_DIR} >/dev/null
        ssh root@${hostname} "chmod -R 755 ${STAREPO_DIR}"
        echo "${hostname}上分发starepo完毕......." | tee -a $LOG_FILE
     done

     ##开始分发visual
     VISUAL_HOST_LISTS=$(grep visual_distribution ${CONF_FILE} | cut -d '=' -f2)
     VISUAL_HOST_ARRAY=(${VISUAL_HOST_LISTS//;/ })
     for hostname in ${VISUAL_HOST_ARRAY[@]}
     do
       ssh root@${hostname} "if [ ! -x "${SERVICE_DIR}" ];then mkdir -p "${SERVICE_DIR}";fi"
       rsync -rvl ${VISUAL_DIR} root@${hostname}:${SERVICE_DIR} >/dev/null
       ssh root@${hostname} "chmod -R 755 ${VISUAL_DIR}"
       echo "${hostname}上分发visual完毕........" | tee -a $LOG_FILE
     done

    echo "配置完毕......" | tee -a $LOG_FILE
}
#####################################################################
# 函数名: sh_cluster
# 描述: 执行config-clusterconf.sh脚本
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function sh_cluster()
{
	# 判断脚本是否存在，存在才执行
	if [ -f "${CLUSTER_SCRPIT}" ]; then
		sh ${CLUSTER_SCRPIT}
	else
		echo "config-clusterconf.sh脚本不存在...."
	fi
}

#####################################################################
# 函数名: sh_service
# 描述: 执行config-serviceconf.sh脚本
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function sh_service()
{
	if [ -f "${SERVICE_SCRPIT}" ]; then 
		sh ${SERVICE_SCRPIT}
	else
		echo "config-serviceconf.sh脚本不存在...." 
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
	distribute_common
	distribute_service
    sh_cluster
    sh_service
}

#---------------------------------------------------------------------#
#                              执行流程                                #
#---------------------------------------------------------------------#

## 打印时间
echo ""  
echo ""  
echo "==================================================="
echo "$(date "+%Y-%m-%d  %H:%M:%S")"

main


set +x
