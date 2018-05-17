#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    start-spring-cloud-dynrepo
## Description: 启动 spring cloud
## Author:      wujiaqi
## Created:     2018-5-7
################################################################################
#set -x  ## 用于调试用，不用的时候可以注释掉

#---------------------------------------------------------------------#
#                              定义变量                                #
#---------------------------------------------------------------------#

cd `dirname $0`
BIN_DIR=`pwd`                                  ### bin 目录

cd ..
DYNREPO_DIR=`pwd`                              ### dynrepo 目录
LIB_DYNREPO_DIR=${DYNREPO_DIR}/lib              ### dynrepo lib
CONF_DYNREPO_DIR=${DYNREPO_DIR}/conf            ### dynrepo 配置文件目录
LOG_DIR=${DYNREPO_DIR}/logs                    ### LOG 目录
LOG_FILE=$LOG_DIR/start-spring-cloud.log
cd ..
SERVICE_DIR=`pwd`                              ### service 目录
CONF_SERVICE_DIR=$SERVICE_DIR/conf             ### service 配置文件

cd ..
OBJECT_DIR=`pwd`                               ### RealTimeFaceCompare 目录

JAR_DYNREPO=`ls ${DYNREPO_DIR}| grep ^dynrepo-[0-9].[0-9].[0-9].jar$`

if [ ! -d $LOG_DIR ]; then
    mkdir $LOG_DIR;
fi
#---------------------------------------------------------------------#
#                              定义函数                                #
#---------------------------------------------------------------------#

#####################################################################
# 函数名: start_spring_cloud
# 描述: 启动 spring cloud
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function start_spring_cloud()
{
    if [ ! -d $LOG_DIR ]; then
        mkdir $LOG_DIR;
    fi
    # java -classpath $CONF_DYNREPO_DIR:$LIB_JARS com.hzgc.service.dynrepo.DynRepoApplication  | tee -a  ${LOG_FILE}
    java -jar ${JAR_DYNREPO} | tee -a  ${LOG_FILE}
}

#####################################################################
# 函数名: update_properties
# 描述: 修改jar包中的配置文件
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function update_properties()
{
    # cd ${OBJECT_LIB_DIR}
    ## 获取需要修改的配置文件
    # jar xf ${LIB_DYNREPO} dynrepoApplication.properties

    # PROPERTIES_DIR=dynrepoApplication.properties
    ## 获取新端口号
    # DYNREPO_SERVER_PORT=${grep "dynrepo.server.port" ${CONF_DIR} | cut -d '=' -f2}
    ## 获取旧端口号
    # old_port=`grep server.port ${PROPERTIES_DIR}`
    ## 替换
    # sed -i "s#^${old_port}#server.port=${DYNREPO_SERVER_PORT}#g" ${PROPERTIES_DIR}

    # DYNREPO_APPLICATION_NAME=${grep "dynrepo.spring.application.name" ${CONF_DIR} | cut -d '=' -f2}
    # old_name=`grep spring.application.name ${PROPERTIES_DIR}`
    # sed -i "s#^${old_name}#spring.application.name={DYNREPO_APPLICATION_NAME}#g" ${PROPERTIES_DIR}

    cd ${DYNREPO_DIR}
    cp conf/dynrepoApplication.properties ./
    jar uf ${JAR_DYNREPO} dynrepoApplication.properties
    rm -f dynrepoApplication.properties

    cp conf/*-site.xml ./
    jar uf ${JAR_DYNREPO} hbase-site.xml
    jar uf ${JAR_DYNREPO} core-site.xml
    jar uf ${JAR_DYNREPO} hdfs-site.xml
    rm -f *-site.xml
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
    update_properties
    start_spring_cloud
}



## 打印时间
echo ""  | tee  -a  ${LOG_FILE}
echo ""  | tee  -a  ${LOG_FILE}
echo "==================================================="  | tee -a ${LOG_FILE}
echo "$(date "+%Y-%m-%d  %H:%M:%S")"                       | tee  -a  ${LOG_FILE}
main

set +x