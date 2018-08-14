#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    hbase_local.sh
## Description: 安装配置 hbase
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
## 安装日志目录
LOG_DIR=${ROOT_HOME}/logs
## 安装日志目录
LOG_FILE=${LOG_DIR}/hbaseInstall.log
## hbase 安装包目录
HBASE_SOURCE_DIR=${ROOT_HOME}/component/bigdata
## 最终安装的根目录，所有bigdata 相关的根目录：/opt/hzgc/bigdata
INSTALL_HOME=$(grep Install_HomeDir ${CONF_DIR}/cluster_conf.properties|cut -d '=' -f2)
## hbase 集群节点
HBASE_HOST=`hostname -i`
## hbase 安装目录
HBASE_INSTALL_HOME=${INSTALL_HOME}/HBase
## hbase 根目录
HBASE_HOME=${HBASE_INSTALL_HOME}/hbase
## JAVA_HOME
JAVA_HOME=${INSTALL_HOME}/JDK/jdk
## hbase-env.sh 文件
HBASE_ENV_FILE=${HBASE_HOME}/conf/hbase-env.sh
## hbase-site.xml 文件
HBASE_SITE_FILE=${HBASE_HOME}/conf/hbase-site.xml
## <value>
VALUE="<value>"
## </value>
VALUE_END="</value>"
if [ ! -d $LOG_DIR ];then
    mkdir -p $LOG_DIR;
fi
## 首先检查本机上是否安装有 hbase 如果有，则删除本机的 hbase
if [ -e ${HBASE_HOME} ];then
    echo "删除原有 hbase"
    rm -rf ${HBASE_HOME}
fi

ZK_LISTS=""${HBASE_HOST}:2181
HBASE_TMP_DIR=${HBASE_HOME}/tmp
HBASE_ZK_DATADIR=${HBASE_HOME}/hbase_zk_datadir
## hbase 存储目录
HBASE_DATA=${HBASE_HOME}/hzgc/hbase
mkdir -p ${HBASE_INSTALL_HOME}
cp -r ${HBASE_SOURCE_DIR}/hbase ${HBASE_INSTALL_HOME}
chmod -R 755 ${HBASE_INSTALL_HOME}

#####################################################################
# 函数名:hbase_env
# 描述: 修改 hbase-env.sh 文件
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function hbase_env ()
{
echo ""  | tee -a $LOG_FILE
echo "**********************************************" | tee -a $LOG_FILE
sed -i "s#java_home#${JAVA_HOME}#g" ${HBASE_ENV_FILE}
echo "设置jdk 路径........." | tee -a $LOG_FILE
}

#####################################################################
# 函数名:hbase_site
# 描述: 修改 hbase-site.xml 文件
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function hbase_site ()
{
echo ""  | tee -a $LOG_FILE
echo "**********************************************" | tee -a $LOG_FILE
cd ${HBASE_HOME}/conf
mkdir -p ${HBASE_TMP_DIR}
mkdir -p ${HBASE_ZK_DATADIR}

sed -i "s#zkaddress#${ZK_LISTS}#g" ${HBASE_SITE_FILE}
sed -i "s#hbase_tmp_dir#${HBASE_TMP_DIR}#g" ${HBASE_SITE_FILE}
sed -i "s#hbase_zookeeper_dataDir#${HBASE_ZK_DATADIR}#g" ${HBASE_SITE_FILE}
sed -i "s#hdfs://hzgc/hbase#${HBASE_DATA}#g" ${HBASE_SITE_FILE}

grep -q "dfs.replication" ${HBASE_SITE_FILE}
if [[ $? -eq 0 ]]; then
    num=$[ $(cat ${HBASE_SITE_FILE} | cat -n | grep  dfs.replication | awk '{print $1}') + 1 ]
    sed -i "${num}c ${VALUE}1${VALUE_END}" ${HBASE_SITE_FILE}
else echo "dfs.replication 配置失败" | tee -a $LOG_FILE
fi
echo  "配置Hbase-site.xml done ......"  | tee -a $LOG_FILE
}

## 将本机IP 添加到 regionservers
echo "${HBASE_HOST}" >> ${HBASE_HOME}/conf/regionservers

#####################################################################
# 函数名:hbase_web
# 描述: 将HBase的UI地址写到指定文件中
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function hbase_web ()
{
echo ""  | tee -a $LOG_FILE
echo "**********************************************" | tee -a $LOG_FILE
echo "准备将hbase的UI地址写到指定文件中............"   | tee -a $LOG_FILE
HBaseWebUI_Dir=$(grep WebUI_Dir ${CONF_DIR}/cluster_conf.properties|cut -d '=' -f2)
HBase_UI="http://${HBASE_HOST}:60010"
mkdir -p ${HBaseWebUI_Dir}
VALUE=$(grep -q "HBaseUI_Address=" ${HBaseWebUI_Dir}/WebUI_Address)
if [ -n "${VALUE}" ];then
    ## 存在就替换
    sed -i "s#^HBaseUI_Address=.*#HBaseUI_Address=${HBase_UI}#g" ${HBaseWebUI_Dir}/WebUI_Address
else
    ## 不存在就添加
    echo "##HBase_WebUI" >> ${HBaseWebUI_Dir}/WebUI_Address
    echo "HBaseUI_Address=${HBase_UI}" >> ${HBaseWebUI_Dir}/WebUI_Address
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
hbase_env
hbase_site
hbase_web
}

#---------------------------------------------------------------------#
#                              执行流程                                #
#---------------------------------------------------------------------#
## 打印时间
echo "" | tee -a $LOG_FILE
echo "$(date "+%Y-%m-%d  %H:%M:%S")" | tee  -a  $LOG_FILE
main