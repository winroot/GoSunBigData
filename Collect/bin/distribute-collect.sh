#!/bin/bash
#########################################################################
## Copyright:  HZGOSUN Tech. Co, BigData
## Filename:   distribute-collect.sh
## Description: 一键配置及分发Collect模块
## Author:     chenke
## Created:    2018-06-01
##########################################################################
#set -x   ##用于调试使用，不用的时候可以注释掉

#-----------------------------------------------------------------------#
#                                定义变量                               #
#-----------------------------------------------------------------------#

cd `dirname $0`
BIN_DIR=`pwd`                                        ##bin目录：脚本所在目录
cd ..
COLLECT_DIR=`pwd`                                    ##collect模块部署目录
COLLECT_CONF_DIR=$COLLECT_DIR/conf                   ##collect模块conf目录
COLLECT_PRO_FILE=$COLLECT_CONF_DIR/collect.properties  ##collect的properties配置文件

COLLECT_LOG_DIR=$COLLECT_DIR/logs                      ##collect的log目录
LOG_FILE=$COLLECT_LOG_DIR/collect.log

#创建日志目录
mkdir -p $COLLECT_LOG_DIR

##########################################################################
# 函数名： distribute_collect
# 描述：将collect模块分发到需要的节点上
# 参数：N/A
# 返回值：N/A
# 其他：N/A
##########################################################################
function distribute_collect()
{
   echo "" | tee -a $LOG_FILE
   echo "**************************************" | tee -a $LOG_FILE
   echo "" | tee -a $LOG_FILE
   echo "分发collect.................." | tee -a $LOG_FILE

   HOST_IP=`hostname -i`
   sed -i "s#^ftp.ip=.*#ftp.ip=${HOST_IP}#g" ${COLLECT_PRO_FILE}

   COLLECT_HOST_LISTS=$(grep collect.distribute ${COLLECT_PRO_FILE} | cut -d '=' -f2)
   COLLECT_HOST_ARRAY=(${COLLECT_HOST_LISTS//;/ })
   for host in ${COLLECT_HOST_ARRAY[@]}
   do
     rsync -rvl ${COLLECT_DIR} root@${host}:/opt  >/dev/null
     ssh root@${host} "chmod -R 755 /opt/Collect"
     echo "${host}上分发collect完毕，开始配置配置文件中ftp.ip................." | tee -a $LOG_FILE
     ssh root@${host} "sed -i "s#^ftp.ip=.*#ftp.ip=${host}#g" ${COLLECT_PRO_FILE}"
     echo "${host}上配置ftp.ip完成.................." | tee -a $LOG_FILE
   done
}

##############################################################################
# 函数名： main
# 描述： 脚本主要业务入口
# 参数： N/A
# 返回值： N/A
# 其他： N/A
##############################################################################
function main()
{
  distribute_collect
}

#--------------------------------------------------------------------------#
#                                  执行流程                                #
#--------------------------------------------------------------------------#
##打印时间
echo ""
echo "=========================================================="
echo "$(date "+%Y-%m-%d  %H:%M:%S")"

main


set +x