#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    config-projectconf
## Description: 一键配置脚本：执行common、cluster、ftp、service的一键配置脚本
## Author:      mashencai
## Created:     2017-11-30
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
    
	CLUSTER_HOSTNAME_LISTS=$(grep Cluster_HostName ${CONF_FILE}|cut -d '=' -f2)
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
# 函数名: sh_ftp
# 描述: 执行config-ftpconf.sh脚本
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function sh_ftp()
{
	if [ -f "${FTP_SCRPIT}" ]; then 
		sh ${FTP_SCRPIT}
	else
		echo "config-ftpconf.sh脚本不存在...." 
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
#    config_common_dubbo
	# config_common_ftp
	distribute_common
    sh_cluster
    # sh_ftp
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
