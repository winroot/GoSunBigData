#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    local_jdk.sh
## Description: 本地模式 jdk 的安装
##              实现自动化的脚本
## Version:     2.0
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
## 集群配置文件目录
CONF_DIR=${ROOT_HOME}/conf
## 日志目录
LOG_DIR=${ROOT_HOME}/logs
## jdk 安装日志
LOG_FILE=${LOG_DIR}/jdkInstall.log
##  jdk 安装包目录
JDK_SOURCE_DIR=${ROOT_HOME}/component/bigdata
## 最终安装的根目录，所有bigdata 相关的根目录
INSTALL_HOME=$(grep Install_HomeDir ${CONF_DIR}/cluster_conf.properties|cut -d '=' -f2)
## JAVA_INSTALL_HOME jdk 安装目录
JAVA_INSTALL_HOME=${INSTALL_HOME}/JDK
## JAVA_HOME  jdk 根目录
JAVA_HOME=${INSTALL_HOME}/JDK/jdk
## /etc/profile
ETC_FILE=/etc/profile

mkdir -p ${JAVA_INSTALL_HOME}
mkdir -p ${LOG_DIR}

echo ""  | tee  -a  $LOG_FILE
echo "************************************************"
echo "准备将 JDK 安装到节点`hostname -i`："  | tee -a $LOG_FILE
source ${ETC_FILE}
mkdir -p  ${JAVA_INSTALL_HOME}
mkdir /home/test
cd /home/test
rpm -qa | grep java   > java.tmp
for rpm_pak in $(cat  java.tmp)
do
    echo "删除原先系统java rpm 软件包: ${rpm_pak}"  |  tee  -a  $LOG_FILE;
    rpm -e --nodeps ${rpm_pak};
done;
rm -rf /home/test

echo "jdk 安装中,请稍候..."  | tee -a $LOG_FILE
cp -r ${JDK_SOURCE_DIR}/jdk ${JAVA_INSTALL_HOME}
chmod -R 755 ${JAVA_INSTALL_HOME}
echo "jdk 安装完毕!!!"  | tee -a $LOG_FILE

## 增加java环境变量，若先前有配置，要先删除原来的
echo "准备修改 /etc/profile 环境变量,请稍候..."  | tee -a $LOG_FILE
## ssh到每个节点，查找etc/profile中是否存在java系统变量行，若存在，则替换；若不存在，则追加。
javahome_exists=$(grep "export JAVA_HOME=" ${ETC_FILE})
javapath_exists=$(grep "export PATH=\$JAVA_HOME" ${ETC_FILE})
# 存在"export JAVA_HOME="这一行：则替换这一行
if [ "${javahome_exists}" != "" ];then
    sed -i "s#^export JAVA_HOME=.*#export JAVA_HOME=${JAVA_HOME}#g" ${ETC_FILE}
fi
# 存在"export PATH=$JAVA_HOME"这一行：则替换这一行
if [ "${javapath_exists}" != "" ];then
    sed -i "s#^export PATH=\$JAVA_HOME.*#export PATH=\$JAVA_HOME/bin:\$PATH#g" ${ETC_FILE}
fi
# 不存在这两行，则追加在文件末尾
if [ "${javahome_exists}" = "" ] && [ "${javapath_exists}" = "" ]; then
    echo "#JAVA_HOME">>${ETC_FILE}
    echo "export JAVA_HOME=$JAVA_HOME" >> ${ETC_FILE}
    echo "export PATH=\$JAVA_HOME/bin:\$PATH"  >> ${ETC_FILE}
    echo "">> ${ETC_FILE}
fi

echo "/etc/profile 环境变量修改完毕！！！"  | tee -a $LOG_FILE
source ${ETC_FILE}