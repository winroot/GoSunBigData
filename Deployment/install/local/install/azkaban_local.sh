#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    azkaban_local.sh
## Description: 安装azkaban
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
## 配置文件目录
CONF_DIR=${ROOT_HOME}/conf
## 日志目录
LOG_DIR=${ROOT_HOME}/logs
## azkaban 安装日志
LOG_FILE=${LOG_DIR}/azkabanInstall.log
## azkaban 安装包目录
AZKABAN_SOURCE_DIR=${ROOT_HOME}/component/bigdata
## 最终安装的根目录，所有 bigdata 相关的根目录
INSTALL_HOME=$(grep Install_HomeDir ${CONF_DIR}/cluster_conf.properties|cut -d '=' -f2)
## AZKABAN_INSTALL_HOME azkaban 安装目录
AZKABAN_INSTALL_HOME=${INSTALL_HOME}/Azkaban
## AZKABAN_HOME  azkaban 根目录
AZKABAN_HOME=${INSTALL_HOME}/Azkaban/azkaban
## mysql 安装节点主机名
MYSQL_HOSTNAME=`hostname -i`
## mysql 登录密钥
MYSQL_PASSWORD=$(grep MYSQL_Password ${CONF_DIR}/cluster_conf.properties|cut -d '=' -f2)
AZKABAN_DATABASE="azkaban"
## ssh密钥
SSL_PASSWORD=$(grep SSH_Password ${CONF_DIR}/cluster_conf.properties|cut -d '=' -f2)
## webserver 下面的 azkaban.properties 文件
WEBSERVER_AZKABAN_PROPERTIES=${AZKABAN_HOME}/webserver/conf/azkaban.properties
## executor 下面的 azkaban.properties 文件
EXECUTOR_AZKABAN_PROPERTIES=${AZKABAN_HOME}/webserver/conf/azkaban.properties
## 打印当前时间
echo "$(date "+%Y-%m-%d  %H:%M:%S")"

if [ ! -d $LOG_DIR ];then
    mkdir -p $LOG_DIR;
fi
## 首先检查本机上是否安装有azkaban 如果有，则删除本机的azkaban
if [ -e ${AZKABAN_HOME} ];then
    echo "删除原有azkaban"
    rm -rf ${AZKABAN_HOME}
fi
mkdir -p ${AZKABAN_INSTALL_HOME}
cp -r ${AZKABAN_SOURCE_DIR}/azkaban ${AZKABAN_INSTALL_HOME}
chmod -R 755 ${AZKABAN_INSTALL_HOME}
mysql_ip=$(grep Mysql_InstallNode ${CONF_DIR}/cluster_conf.properties|cut -d '=' -f2)

#####################################################################
# 函数名: webserver_azkaban_properties
# 描述: 修改 webserver 目录下的 azkaban.properties 配置文件
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function webserver_azkaban_properties ()
{
echo ""  | tee -a $LOG_FILE
echo "**********************************************" | tee -a $LOG_FILE
echo "开始配置web目录下的azkaban.properties文件*****" | tee -a $LOG_FILE

sed -i "s#^mysql.host=.*#mysql.host=${mysql_ip}#g" ${WEBSERVER_AZKABAN_PROPERTIES}
flag1=$?
sed -i "s#^mysql.database=.*#mysql.database=${AZKABAN_DATABASE}#g" ${WEBSERVER_AZKABAN_PROPERTIES}
flag2=$?
sed -i "s#^mysql.password=.*#mysql.password=${MYSQL_PASSWORD}#g" ${WEBSERVER_AZKABAN_PROPERTIES}
flag3=$?
sed -i "s#^jetty.password=.*#jetty.password=${SSL_PASSWORD}#g" ${WEBSERVER_AZKABAN_PROPERTIES}
flag4=$?
sed -i "s#^jetty.keypassword=.*#jetty.keypassword=${SSL_PASSWORD}#g" ${WEBSERVER_AZKABAN_PROPERTIES}
flag5=$?
sed -i "s#^jetty.trustpassword=.*#jetty.trustpassword=${SSL_PASSWORD}#g" ${WEBSERVER_AZKABAN_PROPERTIES}
flag6=$?

if [[ ($flag1 == 0)  && ($flag2 == 0)  &&  ($flag3 == 0)  && ($flag4 == 0)  &&  ($flag5 == 0)  && ($flag6 == 0) ]];then
    echo " webserver目录下的azkaban.properties配置成功." | tee -a $LOG_FILE
else
    echo "webserver目录下的azkaban.properties配置失败." | tee -a $LOG_FILE
fi
}

#####################################################################
# 函数名: executor_azkaban_properties
# 描述: 配置 executor 目录下的的 azkaban.properties 配置文件
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function executor_azkaban_properties ()
{
echo "" | tee -a $LOG_FILE
echo "**********************************************" | tee -a $LOG_FILE
echo "开始配置executor目录下的azkaban.properties文件*****" | tee -a $LOG_FILE

sed -i "s#^mysql.host=.*#mysql.host=${mysql_ip}#g" ${EXECUTOR_AZKABAN_PROPERTIES}
flag1=$?
sed -i "s#^mysql.database=.*#mysql.database=${AZKABAN_DATABASE}#g" ${EXECUTOR_AZKABAN_PROPERTIES}
flag2=$?
sed -i "s#^mysql.password=.*#mysql.password=${MYSQL_PASSWORD}#g" ${EXECUTOR_AZKABAN_PROPERTIES}
flag3=$?

if [[ ($flag1 == 0)  && ($flag2 == 0)  &&  ($flag3 == 0) ]];then
    echo " executor 目录下的azkaban.properties配置成功." | tee -a $LOG_FILE
else
    echo "executor 目录下的azkaban.properties配置失败." | tee -a $LOG_FILE
fi
}

#####################################################################
# 函数名: create_database
# 描述: 创建azkaban数据库并建表
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function create_database ()
{
echo "" | tee -a $LOG_FILE
echo "" | tee -a $LOG_FILE
echo "===================================================" | tee -a $LOG_FILE
echo "$(date "+%Y-%m-%d  %H:%M:%S")" | tee -a $LOG_FILE

##判断建表文件是否存在
SQL_FILE=${AZKABAN_HOME}/sql/create-all-sql-2.5.0.sql
if [ -f "${SQL_FILE}" ]; then
    echo "建表文件存在，可以建表...."
else
    echo "建表文件create-all-sql-2.5.0.sql不存在，请检视安装包是否完整...."
    exit 0
fi

## 启动mysql 并创建数据库
service mysqld start
if [ $? == 0 ];then
    echo "开始操作Mysql......"  |  tee  -a  $LOG_FILE
    /usr/bin/mysql -uroot -p"Hzgc@123" -e "source ${AZKABAN_HOME}/sql/create-all-sql-2.5.0.sql"
    if [ $? == 0 ];then
        echo "操作MySQL成功......................." | tee -a $LOG_FILE
    else
        echo "操作MySQL失败......................." | tee -a $LOG_FILE
    fi
else
    echo "启动MySQL失败..........................." | tee -a $LOG_FILE
fi
}

#####################################################################
# 函数名: create_ssl
# 描述: 配置SSL的KeyStore。
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function create_ssl ()
{
echo "" | tee -a $LOG_FILE
echo "**********************************************" | tee -a $LOG_FILE
echo "开始配置SSL的KeyStore************************ " | tee -a $LOG_FILE
rm -rf ${AZKABAN_HOME}/webserver/azkaban.keystore
ssh root@${MYSQL_HOSTNAME} "source /etc/profile;keytool -keystore ${AZKABAN_HOME}/webserver/keystore -alias jetty -genkey -keyalg RSA -dname \"CN=, OU=, O=, L=, ST=, C=CN\"  -keypass ${SSL_PASSWORD} -storepass ${SSL_PASSWORD}"

KEYSTORE_FILE=${AZKABAN_HOME}/webserver/keystore
if [ -f "${KEYSTORE_FILE}" ]; then
    echo "keystore文件创建成功...."
else
    echo "keystore文件创建失败，请检视配置SSL语句是否正确...."
    exit 1
fi
}

#####################################################################
# 函数名: writeUI_file
# 描述: 将Azkaban的UI地址写到指定文件中
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function writeUI_file()
{
echo "" | tee -a $LOG_FILE
echo "***********************************************" | tee -a $LOG_FILE
echo "准备将Azkaban的UI地址写到指定文件中............" | tee -a $LOG_FILE
AzkabanWebUI_Dir=$(grep WebUI_Dir ${CONF_DIR}/cluster_conf.properties|cut -d '=' -f2)
Install_IP=$(grep Mysql_InstallNode ${CONF_DIR}/cluster_conf.properties|cut -d '=' -f2)
Azkaban_UI="https://${Install_IP}:8443"
mkdir -p ${AzkabanWebUI_Dir}
grep -q "AzkabanUI_Address=" ${AzkabanWebUI_Dir}/WebUI_Address
if [ "$?" -eq "0" ];then
    sed -i "s#^AzkabanUI_Address=.*#AzkabanUI_Address=${Azkaban_UI}#g" ${AzkabanWebUI_Dir}/WebUI_Address
else
    echo "##Azkaban_WebUI" >> ${AzkabanWebUI_Dir}/WebUI_Address
    echo "AzkabanUI_Address=${Azkaban_UI}" >> ${AzkabanWebUI_Dir}/WebUI_Address
fi
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
webserver_azkaban_properties
executor_azkaban_properties
create_database
create_ssl
writeUI_file
}


echo "" | tee -a $LOG_FILE
echo "" | tee -a $LOG_FILE
echo "===================================================" | tee -a $LOG_FILE
echo "$(date "+%Y-%m-%d  %H:%M:%S")"                       | tee -a $LOG_FILE
main

