#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    config-serviceconf
## Description: 配置项目service中的conf配置文件
## Author:      caodabao
## Created:     2017-11-28 
################################################################################
#set -x  ## 用于调试用，不用的时候可以注释掉

#---------------------------------------------------------------------#
#                              定义变量                                #
#---------------------------------------------------------------------#
cd `dirname $0`
BIN_DIR=`pwd`                                         ### bin目录：脚本所在目录
cd ..
DEPLOY_DIR=`pwd`                                      ### service模块部署目录
LOG_DIR=$DEPLOY_DIR/logs                              ### log日志目录
LOG_FILE=$LOG_DIR/config-service.log                  ### log日志目录
cd ..
OBJECT_DIR=`pwd`                                      ### 项目根目录 
CONF_DIR=$OBJECT_DIR/common/conf/project-conf.properties   ### 项目配置文件
cd ../hzgc/conf

## 最终安装的根目录，所有bigdata 相关的根目录
INSTALL_HOME=$(grep Install_HomeDir $CONF_DIR |cut -d '=' -f2)
HADOOP_INSTALL_HOME=${INSTALL_HOME}/Hadoop            ### hadoop 安装目录
HADOOP_HOME=${HADOOP_INSTALL_HOME}/hadoop             ### hadoop 根目录
HBASE_INSTALL_HOME=${INSTALL_HOME}/HBase              ### hbase 安装目录
HBASE_HOME=${HBASE_INSTALL_HOME}/hbase                ### hbase 根目录


#---------------------------------------------------------------------#
#                              定义函数                                #
#---------------------------------------------------------------------#

#####################################################################
# 函数名: move_xml
# 描述: 配置Hbase服务，移动所需文件到cluster/conf下
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function move_xml()
{
    echo ""  | tee -a $LOG_FILE
    echo "**********************************************" | tee -a $LOG_FILE
    echo "" | tee -a $LOG_FILE
    echo "copy 文件 hbase-site.xml core-site.xml hdfs-site.xml 到 cluster/conf......"  | tee  -a  $LOG_FILE

    cp ${HBASE_HOME}/conf/hbase-site.xml ${DEPLOY_DIR}/clustering/conf
    cp ${HADOOP_HOME}/etc/hadoop/core-site.xml ${DEPLOY_DIR}/clustering/conf
    cp ${HADOOP_HOME}/etc/hadoop/hdfs-site.xml ${DEPLOY_DIR}/clustering/conf

    cp ${HBASE_HOME}/conf/hbase-site.xml ${DEPLOY_DIR}/device/conf
    cp ${HADOOP_HOME}/etc/hadoop/core-site.xml ${DEPLOY_DIR}/device/conf
    cp ${HADOOP_HOME}/etc/hadoop/hdfs-site.xml ${DEPLOY_DIR}/device/conf

    cp ${HBASE_HOME}/conf/hbase-site.xml ${DEPLOY_DIR}/dynrepo/conf
    cp ${HADOOP_HOME}/etc/hadoop/core-site.xml ${DEPLOY_DIR}/dynrepo/conf
    cp ${HADOOP_HOME}/etc/hadoop/hdfs-site.xml ${DEPLOY_DIR}/dynrepo/conf
    
    echo "copy完毕......"  | tee  -a  $LOG_FILE
}

#####################################################################
# 函数名: config_es
# 描述: 配置common_service_es.properties的es.hosts
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function config_es()
{
    echo ""  | tee -a $LOG_FILE
    echo "**********************************************" | tee -a $LOG_FILE
    echo "" | tee -a $LOG_FILE
    echo "配置service/conf/common_service_es.properties......"  | tee  -a  $LOG_FILE

    # 配置es.hosts：
    # 从project-conf.properties读取es所需配置IP
    # 根据字段es，查找配置文件，这些值以分号分割
    ES_IP=$(grep es_servicenode ${CONF_DIR} | cut -d '=' -f2)
    # 将这些分号分割的ip用放入数组
    es_arr=(${ES_IP//;/ })
    espro=''    
    for es_host in ${es_arr[@]}
    do
        espro="$espro$es_host,"
    done
    espro=${espro%?}
    
    # 替换es-config.properties中：key=value（替换key字段的值value）
    sed -i "s#^es.hosts=.*#es.hosts=${espro}#g" ${DEPLOY_DIR}/conf/common_service_es.properties
    echo "common_service_es.properties配置es完毕......"  | tee  -a  $LOG_FILE
}

#####################################################################
# 函数名: config_jdbc
# 描述: 配置service/conf/common_service_jdbc.properties
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function config_jdbc()
{
    echo ""  | tee -a $LOG_FILE
    echo "**********************************************" | tee -a $LOG_FILE
    echo "" | tee -a $LOG_FILE
    echo "配置jdbc相关参数......"  | tee  -a  $LOG_FILE
    ##jdbc节点IP
    JDBC_IPS=$(grep jdbc_servicenode ${CONF_DIR} | cut -d '=' -f2)
    jdbc_arr=(${JDBC_IPS//;/ })
    jdbc_ips=''    
    for jdbc_ip in ${jdbc_arr[@]}
    do
        jdbc_ips="$jdbc_ips$jdbc_ip:2181,"
    done
    JDBC="jdbc:hive2://${jdbc_ips%?}/;serviceDiscoveryMode=zooKeeper;zooKeeperNamespace=thriftserver"
    sed -i "s#^url=.*#url=${JDBC}#g"  ${CONF_SERVICE_DIR}/common_service_jdbc.properties

    jdbc_ips=''
    for jdbc_ip in ${jdbc_arr[@]}
    do
        jdbc_ips="$jdbc_ips$jdbc_ip,"
    done
    JDBC=${jdbc_ips%?}
    sed -i "s#^phoenixJDBCURL.*#phoenixJDBCURL=jdbc:phoenix:${JDBC}:2181#g"  ${CONF_SERVICE_DIR}/common_service_jdbc.properties

    echo "配置jdbc.properties完毕......"  | tee  -a  $LOG_FILE
}

#####################################################################
# 函数名: send_to_all
# 描述: 分发并配置每个节点下的spring cloud 配置文件
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
func send_to_all()
{

    echo ""  | tee -a $LOG_FILE
    echo "**********************************************" | tee -a $LOG_FILE
    echo "" | tee -a $LOG_FILE
    echo "分发service................."  | tee  -a  $LOG_FILE
    ## 获取dubbo节点IP
    SPRING_HOSTS=$(grep spring_cloud_servicenode ${CONF_DIR} | cut -d '=' -f2)
    spring_arr=(${SPRING_HOSTS//;/ })
    for spring_host in ${spring_arr[@]}
    do
        ssh root@${spring_host}  "mkdir -p ${OBJECT_DIR}"
        rsync -rvl ${OBJECT_DIR}/service   root@${spring_host}:${OBJECT_DIR}  >/dev/null
        ssh root@${spring_host}  "chmod -R 755   ${OBJECT_DIR}/service"
    done
    echo "分发service完毕......"  | tee  -a  $LOG_FILE
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
    move_xml
    # config_es
    # config_jdbc
    sh ${BIN_DIR}/config-spring-cloud.sh
    send_to_all
}


#---------------------------------------------------------------------#
#                              执行流程                                #
#---------------------------------------------------------------------#

## 打印时间
echo ""  | tee  -a  $LOG_FILE
echo ""  | tee  -a  $LOG_FILE
echo "==================================================="  | tee -a $LOG_FILE
echo "$(date "+%Y-%m-%d  %H:%M:%S")"                       | tee  -a  $LOG_FILE
echo "开始配置service中的conf文件"                       | tee  -a  $LOG_FILE
main

set +x
