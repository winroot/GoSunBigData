#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    antInstall.sh
## Description: 安装ant，实现自动化的脚本
## Version:     1.0
## Author:      mashencai
## Created:     2018-4-24
################################################################################

#set -x

cd `dirname $0`
## 脚本所在目录
BIN_DIR=`pwd`
cd ..
## 安装包根目录
ROOT_HOME=`pwd`
## 配置文件目录
CONF_DIR=${ROOT_HOME}/conf
## 日记目录
LOG_DIR=${ROOT_HOME}/logs
## ant 安装日记
LOG_FILE=${LOG_DIR}/antInstall.log
## ant 安装包目录
ANT_SOURCE_DIR=${ROOT_HOME}/component/basic_suports/antTar
## 基础工具安装路径
INSTALL_HOME_BASIC=$(grep System_SuportDir ${CONF_DIR}/cluster_conf.properties|cut -d '=' -f2)
## ant 软件最终目录
export ANT_HOME=${INSTALL_HOME_BASIC}/ant


mkdir -p ${ANT_HOME}
mkdir -p ${LOG_DIR} 


echo ""  | tee  -a  $LOG_FILE
echo ""  | tee  -a  $LOG_FILE
echo "==================================================="  | tee -a $LOG_FILE
echo "$(date "+%Y-%m-%d  %H:%M:%S")"   | tee -a $LOG_FILE

echo “解压ant tar 包中，请稍候.......”  | tee -a $LOG_FILE
tar -xf ${ANT_SOURCE_DIR}/ant.tar.gz -C $INSTALL_HOME_BASIC
if [ $? == 0 ];then
    echo "解压缩ant 安装包成功......"  | tee -a $LOG_FILE 
else 
    echo "解压ant 安装包失败。请检查安装包是否损坏，或者重新安装."  | tee -a $LOG_FILE
    exit 1
fi


echo "添加ant环境变量......"  | tee -a $LOG_FILE 
### 增加ant环境变量，若先前有配置，要先删除原来的
### 查找etc/profile中是否存在ant系统变量行，若存在，则替换；若不存在，则追加。
anthome_exists=$(grep "export ANT_HOME=" /etc/profile)
antpath_exists=$(grep "export PATH=\$ANT_HOME" /etc/profile)
# 存在"export ANT_HOME="这一行：则替换这一行
if [ "${anthome_exists}" != "" ];then
    sed -i "s#^export ANT_HOME=.*#export ANT_HOME=$ANT_HOME#g" /etc/profile
fi
# 存在"export PATH=$ANT_HOME"这一行：则替换这一行
if [ "${antpath_exists}" != "" ];then
    sed -i "s#^export PATH=\$ANT_HOME.*#export PATH=\$ANT_HOME/bin:\$PATH#g" /etc/profile
fi
# 不存在这两行，则追加在文件末尾
if [ "${anthome_exists}" = "" ] && [ "${antpath_exists}" = "" ]; then
    echo "#ANT_HOME">>/etc/profile ;echo export ANT_HOME=$ANT_HOME >> /etc/profile
    echo export PATH=\$ANT_HOME/bin:\$PATH  >> /etc/profile; echo "">> /etc/profile
fi    
source /etc/profile

echo "添加ant环境变量done..."

set +x    
