#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    spark_local.sh
## Description: spark 安装
## Version:     2.4
## Author:      yinhang
## Created:     2018-07-28
################################################################################
## set -x  ## 用于调试用，不用的时候可以注释掉

#---------------------------------------------------------------------#
#                              定义变量                                #
#---------------------------------------------------------------------#
cd `dirname $0`
## 脚本所在目录
BIN_DIR=`pwd`
cd ../../..
## 安装包根目录
ROOT_HOME=`pwd`
## 配置文件目录
CONF_DIR=${ROOT_HOME}/conf
## 安装日志目录
LOG_DIR=${ROOT_HOME}/logs
## 安装日志目录
LOG_FILE=${LOG_DIR}/sparkInstall.log
## spark 安装包目录
SPARK_SOURCE_DIR=${ROOT_HOME}/component/bigdata
## 最终安装的根目录，所有bigdata 相关的根目录：/opt/hzgc/bigdata
INSTALL_HOME=$(grep Install_HomeDir ${CONF_DIR}/cluster_conf.properties|cut -d '=' -f2)
## spark 集群节点
SPARK_HOST=`hostname -i`
## spark 安装目录
SPARK_INSTALL_HOME=${INSTALL_HOME}/Spark
## spark 根目录
SPARK_HOME=${SPARK_INSTALL_HOME}/spark
## spark conf 目录
SPARK_CONF_DIR=${SPARK_HOME}/conf
## spark spark-env.sh 文件
SPARK_ENV_FILE=${SPARK_CONF_DIR}/spark-env.sh
## spark slaves 文件
SLAVES_FILE=${SPARK_CONF_DIR}/slaves
## spark beeline 文件
BEELINE_FILE=${SPARK_HOME}/bin/spark-beeline
## spark-defaults.conf 文件
DEFAULT_CONF_FILE=${SPARK_CONF_DIR}/spark-defaults.conf

if [ ! -d $LOG_DIR ];then
    mkdir -p $LOG_DIR;
fi
## 首先检查本机上是否安装有spark 如果有，则删除本机的spark
if [ -e ${SPARK_HOME} ];then
    echo "删除原有spark"  | tee  -a  $LOG_FILE
    rm -rf ${SPARK_HOME}
fi
mkdir -p ${SPARK_INSTALL_HOME}
cp -r ${SPARK_SOURCE_DIR}/spark ${SPARK_INSTALL_HOME}
chmod -R 755 ${SPARK_INSTALL_HOME}

#####################################################################
# 函数名:spark_env
# 描述: 修改spark_env文件
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
zkconf=""
zkconf="${zkconf}${SPARK_HOST}:2181"

function spark_env ()
{
echo "正在修改 spark-env 文件"  | tee  -a  $LOG_FILE
VALUE=$(grep "SPARK_DAEMON_JAVA_OPTS=" ${SPARK_ENV_FILE} | cut -d '=' -f4 | cut -d ' ' -f1)
VALUE2=$(grep "${zkconf}" ${SPARK_ENV_FILE})
if [ -n "${VALUE2}" ];then
    echo "SPARK_DAEMON_JAVA_OPTS 配置本机IP及端口号已存在，不需要添加"  | tee  -a  $LOG_FILE
else
    sed -i "s#${VALUE}#${zkconf}#g" ${SPARK_ENV_FILE}
fi
}

#####################################################################
# 函数名:salves
# 描述: 修改salves文件
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function salves ()
{
echo "正在修改 salves 文件"  | tee  -a  $LOG_FILE
value1=$(grep "${SPARK_HOST}"  ${SLAVES_FILE})
if [ -n "${value1}" ];then
## 不为空
    echo "slaves 文件中已存在本机 ${SPARK_HOST} 的IP，不需要添加"  | tee  -a  $LOG_FILE
else
    echo ${SPARK_HOST} >> ${SLAVES_FILE}
fi
}

#####################################################################
# 函数名:spark_beeline
# 描述: 修改spark-beeline文件
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function spark_beeline ()
{
echo "正在修改 spark-beeline 文件"  | tee  -a  $LOG_FILE
VALUE1=$(grep "jdbc:hive2://" ${BEELINE_FILE} | cut -d '/' -f5)
VALUE2=$(grep "${zkconf}" ${BEELINE_FILE})
if [ -n "${VALUE2}" ];then
    echo "spark_beeline 配置中本机IP及端口号已存在，不需要添加"  | tee  -a  $LOG_FILE
else
    sed -i "s#${VALUE1}#${zkconf}#g" ${BEELINE_FILE}
fi
}

#####################################################################
# 函数名:defaults_conf
# 描述: 修改spark-defaults.conf 文件
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function defaults_conf ()
{
VALUE=$(grep "spark.yarn.historyServer.address" ${DEFAULT_CONF_FILE} )
VALUE2=${VALUE##* }
echo "正在修改 spark-defaults 文件"  | tee  -a  $LOG_FILE
sed -i "s#$VALUE2#${SPARK_HOST}:18080#g" ${DEFAULT_CONF_FILE}
}

#####################################################################
# 函数名: main
# 描述: 脚本主要业务入口
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function main ()
{
spark_env
salves
spark_beeline
defaults_conf
}

#---------------------------------------------------------------------#
#                              执行流程                                #
#---------------------------------------------------------------------#
## 打印时间
echo "" | tee -a $LOG_FILE
echo "$(date "+%Y-%m-%d  %H:%M:%S")" | tee  -a  $LOG_FILE
main





