#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    delete_env_variable.sh
## Description: 删除系统中环境变量
##              实现自动化的脚本
## Version:     1.0
## Author:      yinhang
## Created:     2018-07-28
################################################################################
#set -x

#---------------------------------------------------------------------#
#                              定义变量                                #
#---------------------------------------------------------------------#
cd `dirname $0`
## 脚本所在目录
BIN_DIR=`pwd`
cd ../../..
## 安装包根目录
ROOT_HOME=`pwd`
## 日记目录
LOG_DIR=${ROOT_HOME}/logs
## 日记文件
LOG_FILE=${LOG_DIR}/delete_env_variable.log
##etc所在目录
ETC_FILE=/etc/profile

mkdir -p ${LOG_DIR}

#---------------------------------------------------------------------#
#                              定义函数                                #
#---------------------------------------------------------------------#

#####################################################################
# 函数名: delete_java_variable
# 描述: 删除各个节点上/etc/profile中的Java环境变量
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function delete_java_variable ()
{
echo ""  | tee -a $LOG_FILE
echo "**********************************************" | tee -a $LOG_FILE
echo "please waitinng, 删除java环境变量........"  | tee -a $LOG_FILE

# java变量的注释行“#JAVA_HOME”是否存在，存在则删除
javaComments_exists=$(grep "#JAVA_HOME" ${ETC_FILE})
if [ "${javaComments_exists}" != "" ];then
    sed -i '/#JAVA_HOME/d' ${ETC_FILE}
fi

# java变量的“export JAVA_HOME=”行是否存在，存在则删除
javahome_exists=$(grep "export JAVA_HOME=" ${ETC_FILE})
if [ "${javahome_exists}" != "" ];then
    sed -i '/export JAVA_HOME=/d' ${ETC_FILE}
fi

# java变量的“export PATH=$JAVA_HOME”行是否存在，存在则删除
javapath_exists=$(grep "export PATH=\$JAVA_HOME" ${ETC_FILE})
if [ "${javapath_exists}" != "" ];then
    sed -i '/export PATH=\$JAVA_HOME/d' ${ETC_FILE}
fi

echo "删除java环境变量完成........"  | tee -a $LOG_FILE
}

#####################################################################
# 函数名: delete_namesrv_variable
# 描述: 删除各个节点上/etc/profile中的NAMESRV_ADDR环境变量
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function delete_namesrv_variable ()
{	
echo ""  | tee -a $LOG_FILE
echo "**********************************************" | tee -a $LOG_FILE
echo "please waitinng, 删除namesrv环境变量........"  | tee -a $LOG_FILE

## 判断是否存在export NAMESRV_ADDR=172.18.18.108:9876这一行，若存在删除
namesrv_exists=$(grep "export NAMESRV_ADDR=" ${ETC_FILE})
if [ "${namesrv_exists}" != "" ];then
sed -i '/export NAMESRV_ADDR=/d' ${ETC_FILE}
fi

echo "删除namesrv环境变量完成........"  | tee -a $LOG_FILE
}

#####################################################################
# 函数名: delete_openblas_num_variable
# 描述: 删除各个节点上/etc/profile中的OPENBLAS_NUM_THREADS环境变量
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function delete_openblas_num_variable ()
{
echo ""  | tee -a $LOG_FILE
echo "**********************************************" | tee -a $LOG_FILE
echo "please waitinng, 删除openblas_num环境变量........"  | tee -a $LOG_FILE

# 判断是否存在export OPENBLAS_NUM_THREADS=1这一行，若存在删除
openblas_num_exists=$(grep "export OPENBLAS_NUM_THREADS=" ${ETC_FILE})
if [ "${openblas_num_exists}" != "" ];then
sed -i '/export OPENBLAS_NUM_THREADS=/d' ${ETC_FILE}
fi

	echo "删除openblas_num环境变量完成........"  | tee -a $LOG_FILE
}

#####################################################################
# 函数名: delete_gsfacelib_variable
# 描述: 删除各个节点上/etc/profile中的gsfacelib环境变量
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function delete_gsfacelib_variable ()
{
echo ""  | tee -a $LOG_FILE
echo "**********************************************" | tee -a $LOG_FILE
echo "please waitinng, 删除gsfacelib环境变量........"  | tee -a $LOG_FILE

## gsfacelib算法的注释行“#LD_LIBRARY_PATH”是否存在，存在则删除
facelibComments_exists=$(grep "#LD_LIBRARY_PATH" ${ETC_FILE})
if [ "${facelibComments_exists}" != "" ];then
    sed -i '/#LD_LIBRARY_PATH/d' ${ETC_FILE}
fi
## gsfacelib算法的“export LD_LIBRARY_PATH=”行是否存在，存在则删除
facelibpath_exists=$(grep "export LD_LIBRARY_PATH=" ${ETC_FILE})
if [ "${facelibpath_exists}" != "" ];then
    sed -i '/export LD_LIBRARY_PATH=/d' ${ETC_FILE}
fi

echo "删除gsfacelib环境变量完成........"  | tee -a $LOG_FILE
}
cp ${ROOT_HOME}/env_bigdata.sh /opt/hzgc/
#rm -rf ${ROOT_HOME}/conf/*
#cp ${CONF_DIR}/local_conf.properties ${ROOT_HOME}/conf/
#cp -r ${ROOT_HOME}/tool /opt/hzgc/
#chmod -R 755 /opt/hzgc/toolcd /opc

function main
{
delete_java_variable
delete_namesrv_variable
delete_gsfacelib_variable
delete_openblas_num_variable
}

echo ""  | tee  -a  $LOG_FILE
echo ""  | tee  -a  $LOG_FILE
echo "==================================================="  | tee -a $LOG_FILE
echo "$(date "+%Y-%m-%d  %H:%M:%S")"   | tee -a $LOG_FILE
main

