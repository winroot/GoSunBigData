#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    hadoopInstall.sh
## Description: 安装配置hadoop集群
##              实现自动化的脚本
## Version:     2.0
## Author:      zhangbaolin
## Created:     2018-7-6
################################################################################
#set -e
#set -x

cd `dirname $0`
## 脚本所在目录
BIN_DIR=`pwd`
cd ../..
## 安装包根目录
ROOT_HOME=`pwd`
## 配置文件目录
CONF_DIR=${ROOT_HOME}/conf
##扩展集群配置文件目录
EXPAND_CONF_DIR=${ROOT_HOME}/expand/conf
## 日记目录
LOG_DIR=${ROOT_HOME}/logs
## 安装日记
LOG_FILE=${LOG_DIR}/hbaseInstall.log
## 安装包目录
HBASE_SOURCE_DIR=${ROOT_HOME}/component/bigdata
## 最终安装的根目录，所有bigdata 相关的根目录
INSTALL_HOME=$(grep Install_HomeDir ${CONF_DIR}/cluster_conf.properties|cut -d '=' -f2)
## 集群组件的日志文件目录 /opt/logs
LOGS_PATH=$(grep Cluster_LOGSDir ${CONF_DIR}/cluster_conf.properties|cut -d '=' -f2)
HBASE_LOG_PATH=${LOGS_PATH}/hbase
## hbase的安装节点，需要拼接，放入数组HBASE_HOSTNAME_ARRY中
HBASE_HMASTER=$(grep HBase_Hmaster ${CONF_DIR}/cluster_conf.properties|cut -d '=' -f2)
HBASE_HREGIONSERVER=$(grep HBase_HRegionServer ${CONF_DIR}/cluster_conf.properties|cut -d '=' -f2)


if [[ ${HBASE_HREGIONSERVER} =~ ${HBASE_HMASTER} ]]; then
    HBASE_HOSTNAME_ARRY=(${HBASE_HREGIONSERVER//;/ })
else
    HBASE_HOSTNAME_LISTS=${HBASE_HMASTER}";"${HBASE_HREGIONSERVER}
    HBASE_HOSTNAME_ARRY=(${HBASE_HOSTNAME_LISTS//;/ })
fi

## 集群扩展的节点
EXPAND_NODE=$(grep Node_HostName ${EXPAND_CONF_DIR}/expand_conf.properties | cut -d '=' -f2)
EXPAND_NODE_ARRY=(${EXPAND_NODE//;/ })

## 安装目录
HBASE_INSTALL_HOME=${INSTALL_HOME}/HBase
## 组件的根目录
HBASE_HOME=${HBASE_INSTALL_HOME}/hbase
## JAVA_HOME
JAVA_HOME=${INSTALL_HOME}/JDK/jdk
## HADOOP_HOME
HADOOP_HOME=${INSTALL_HOME}/Hadoop/hadoop
## <value>
VALUE="<value>"
## </value>
VALUE_END="</value>"


echo "-------------------------------------" | tee  -a  $LOG_FILE
echo "准备进行 hbase 扩展安装操作 ing~" | tee  -a  $LOG_FILE
echo "-------------------------------------" | tee  -a  $LOG_FILE

## 设置和获取HBase 集群的配置
mkdir -p ${HBASE_HOME}
ZK_LISTS=""
HBASE_TMP_DIR=${HBASE_HOME}/tmp
HBASE_ZK_DATADIR=${HBASE_HOME}/hbase_zk_datadir

hostname_num=0
for hostname in ${HBASE_HOSTNAME_ARRY[@]};do
    let hostname_num++
    if [ $hostname_num == 1 ];then
        ZK_LISTS="${hostname}:2181"
    else
        ZK_LISTS="${hostname}:2181,${ZK_LISTS}"
    fi
done


echo ""  | tee  -a  $LOG_FILE
echo ""  | tee  -a  $LOG_FILE
echo "==================================================="  | tee -a $LOG_FILE
echo "$(date "+%Y-%m-%d  %H:%M:%S")"                       | tee  -a  $LOG_FILE


## 设置regionserver
echo ""  | tee -a $LOG_FILE
echo "**********************************************" | tee -a $LOG_FILE


## 配置regionservers文件
## Hbase的从节点
HBASE_HREGION_ARRY=(${HBASE_HREGIONSERVER//;/ })
echo "" > ${HBASE_HOME}/conf/regionservers
for hostname in ${HBASE_HREGION_ARRY[@]}
do
	echo $hostname >> ${HBASE_HOME}/conf/regionservers
done
echo "设置regionservers done"  | tee -a $LOG_FILE

function config_conf_slaves()
{
    ## 设置hbase-site.xml
    echo ""  | tee -a $LOG_FILE
    echo "**********************************************" | tee -a $LOG_FILE
    cd ${HBASE_HOME}/conf
    mkdir -p ${HBASE_TMP_DIR}
    mkdir -p ${HBASE_ZK_DATADIR}
        NUM=$[`grep -n hbase.zookeeper.quorum hbase-site.xml | cut -d ':' -f1`+1]
        sed -i "${NUM}c ${VALUE}${ZK_LISTS}${VALUE_END}" hbase-site.xml
        echo  "配置Hbase-site.xml done ......"  | tee -a $LOG_FILE
        ## 拷贝Hadoop 的两个文件到hbase conf 目录下，拷贝前先确认是否安装配置了HADOOP
        echo ""  | tee -a $LOG_FILE
        echo "**********************************************" | tee -a $LOG_FILE
        if [ -d ${HADOOP_HOME}/etc/hadoop ];then
            cp -f ${HADOOP_HOME}/etc/hadoop/core-site.xml ${HBASE_HOME}/conf
            cp -f ${HADOOP_HOME}/etc/hadoop/hdfs-site.xml ${HBASE_HOME}/conf
            echo "拷贝 core-site.xml, hdfs.xml"  | tee  -a  $LOG_FILE
        else
            echo "hadoop 没有安装正确，请检查hadoop 的安装配置。"  | tee  -a  $LOG_FILE
        fi
}

function writeUI_file(){
    ## 将HBase的UI地址写到指定文件中
    echo ""  | tee -a $LOG_FILE
    echo "**********************************************" | tee -a $LOG_FILE
    echo "准备将hbase的UI地址写到指定文件中............"    | tee -a $LOG_FILE
    HBaseWebUI_Dir=$(grep WebUI_Dir ${CONF_DIR}/cluster_conf.properties|cut -d '=' -f2)
    HMASTER_IP=$(cat /etc/hosts|grep "$HBASE_HMASTER" | awk '{print $1}')
    HBase_UI="http://${HMASTER_IP}:60010"
    mkdir -p ${HBaseWebUI_Dir}
    grep -q "HBaseUI_Address=" ${HBaseWebUI_Dir}/WebUI_Address
    if [ "$?" -eq "0" ]  ;then
        sed -i "s#^HBaseUI_Address=.*#HBaseUI_Address=${HBase_UI}#g" ${HBaseWebUI_Dir}/WebUI_Address
    else
        echo "##HBase_WebUI" >> ${HBaseWebUI_Dir}/WebUI_Address
        echo "HBaseUI_Address=${HBase_UI}" >> ${HBaseWebUI_Dir}/WebUI_Address
    fi
}

function xync_hbase()
{
    ## 分发hbase 配置文件。
    echo ""  | tee -a $LOG_FILE
    echo "**********************************************" | tee -a $LOG_FILE
    echo "文件分发中，please waiting....."  | tee -a $LOG_FILE
    for hostname in ${EXPAND_NODE_ARRY[@]};do
        ssh $hostname "mkdir   -p ${HBASE_INSTALL_HOME}"
        rsync -rvl ${HBASE_HOME} root@${hostname}:${HBASE_INSTALL_HOME}  > /dev/null
        ssh $hostname "chmod -R 755 ${HBASE_HOME}"
        ssh root@${hostname} "mkdir -p ${HBASE_LOG_PATH};chmod -R 777 ${HBASE_LOG_PATH}"
    done
    for hostname in ${HBASE_HOSTNAME_ARRY[@]};do
         scp  ${HBASE_INSTALL_HOME}/hbase/conf/hbase-site.xml  root@${hostname}:${HBASE_INSTALL_HOME}/hbase/conf
        ssh $hostname "chmod -R 755 ${HBASE_HOME}"
    done
    echo "hbase 文件分发完成，安装完成......"  | tee  -a  $LOG_FILE
}

function main()
{
    config_conf_slaves
    writeUI_file
    xync_hbase
}


main

echo "-------------------------------------" | tee  -a  $LOG_FILE
echo "hbase 扩展安装操作完成 zzZ~" | tee  -a  $LOG_FILE
echo "-------------------------------------" | tee  -a  $LOG_FILE
set +x
