#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    start-spring-cloud
## Description: 启动 spring cloud
## Author:      wujiaqi
## Created:     2018-5-4
################################################################################
#set -x  ## 用于调试用，不用的时候可以注释掉

#---------------------------------------------------------------------#
#                              定义变量                                #
#---------------------------------------------------------------------#

cd `dirname $0`
BIN_DIR=`pwd`                               ### bin 目录

cd ..
SERVICE_DIR=`pwd`                           ### service 目录
CONF_SERVICE_DIR=$SERVICE_DIR/conf          ### service 配置文件
LIB_STAREPO_DIR=${SERVICE_DIR}/starepo/lib  ### starepo lib
LOG_DIR=${SERVICE_DIR}/logs                 ### LOG 目录
LOG_FILE=$LOG_DIR/start-spring-cloud.log

cd ..
OBJECT_DIR=`pwd`                            ### RealTimeFaceCompare 目录
OBJECT_LIB_DIR=${OBJECT_DIR}/lib            ### lib
OBJECT_JARS=`ls ${OBJECT_LIB_DIR} | grep .jar | awk '{print "'${OBJECT_LIB_DIR}'/"$0}'|tr "\n" ":"`

if [ ! -d $LOG_DIR ]; then
    mkdir $LOG_DIR;
fi

#---------------------------------------------------------------------#
#                              定义函数                                #
#---------------------------------------------------------------------#

#####################################################################
# 函数名: start_spring_cloud_for_starepo
# 描述: 脚本主要业务入口
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function start_spring_cloud_for_starepo()
{
    CONF=${SERVICE_DIR}/starepo/conf
    LIB_JARS=`ls $LIB_STAREPO_DIR|grep .jar | grep -v avro-ipc-1.7.7-tests.jar \
    | grep -v avro-ipc-1.7.7.jar | grep -v spark-network-common_2.10-1.5.1.jar | \
    awk '{print "'$LIB_STAREPO_DIR'/"$0}'|tr "\n" ":"`   ## jar包位置以及第三方依赖jar包，绝对路径

    LIB_JARS=${LIB_JARS}${OBJECT_JARS}
    if [ ! -d $LOG_DIR ]; then
        mkdir $LOG_DIR;
    fi
    java -classpath $CONF:$LIB_JARS com.hzgc.service.starepo.StaRepoApplication  | tee -a  ${LOG_FILE}

#    spring_pid=$(ps -ef | grep "com.hzgc.service.starepo.StaRepoApplication" | awk '{print $2}' | uniq)
#    if [ -n "${spring_pid}" ];then
#        echo "start check_spring_cloud_for_starepo success."  | tee -a  $LOG_FILE
#    else
#        echo "stop check_spring_cloud sucessed, just to start check_spring_cloud."  | tee -a  $LOG_FILE
#    fi
}

#####################################################################
# 函数名: start_spring_cloud_for_face
# 描述: 脚本主要业务入口
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function start_spring_cloud_for_face()
{
    CONF=${SERVICE_DIR}/face/conf
    LIB_FACE_DIR=${SERVICE_DIR}/face/lib
    LIB_JARS=`ls $LIB_FACE_DIR|grep .jar | grep -v avro-ipc-1.7.7-tests.jar \
    | grep -v avro-ipc-1.7.7.jar | grep -v spark-network-common_2.10-1.5.1.jar | \
    awk '{print "'$LIB_FACE_DIR'/"$0}'|tr "\n" ":"`   ## jar包位置以及第三方依赖jar包，绝对路径

    LIB_JARS=${LIB_JARS}${OBJECT_JARS}
    if [ ! -d $LOG_DIR ]; then
        mkdir $LOG_DIR;
    fi
    java -classpath $CONF:$LIB_JARS com.hzgc.service.face.FaceApplication  | tee -a  ${LOG_FILE}
}

#####################################################################
# 函数名: start_spring_cloud_for_dynrepo
# 描述: 脚本主要业务入口
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function start_spring_cloud_for_dynrepo()
{
    CONF=${SERVICE_DIR}/dynrepo/conf
    LIB_DYNREPO_DIR=${SERVICE_DIR}/dynrepo/lib
    LIB_JARS=`ls $LIB_DYNREPO_DIR|grep .jar | grep -v avro-ipc-1.7.7-tests.jar \
    | grep -v avro-ipc-1.7.7.jar | grep -v spark-network-common_2.10-1.5.1.jar | \
    awk '{print "'$LIB_DYNREPO_DIR'/"$0}'|tr "\n" ":"`   ## jar包位置以及第三方依赖jar包，绝对路径

    LIB_JARS=${LIB_JARS}${OBJECT_JARS}
    if [ ! -d $LOG_DIR ]; then
        mkdir $LOG_DIR;
    fi
    java -classpath $CONF:$LIB_JARS com.hzgc.service.dynrepo.DynrepoApplication  | tee -a  ${LOG_FILE}
}

#####################################################################
# 函数名: start_spring_cloud_for_device
# 描述: 脚本主要业务入口
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function start_spring_cloud_for_device()
{
    CONF=${SERVICE_DIR}/device/conf
    LIB_DEVICE_DIR=${SERVICE_DIR}/device/lib
    LIB_JARS=`ls $LIB_DEVICE_DIR|grep .jar | grep -v avro-ipc-1.7.7-tests.jar \
    | grep -v avro-ipc-1.7.7.jar | grep -v spark-network-common_2.10-1.5.1.jar | \
    awk '{print "'$LIB_DEVICE_DIR'/"$0}'|tr "\n" ":"`   ## jar包位置以及第三方依赖jar包，绝对路径

    LIB_JARS=${LIB_JARS}${OBJECT_JARS}
    if [ ! -d $LOG_DIR ]; then
        mkdir $LOG_DIR;
    fi
    java -classpath $CONF:$LIB_JARS com.hzgc.service.device.DeviceApplication  | tee -a  ${LOG_FILE}
}

#####################################################################
# 函数名: start_spring_cloud_for_clustering
# 描述: 脚本主要业务入口
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function start_spring_cloud_for_clustering()
{
    CONF=${SERVICE_DIR}/clustering/conf
    LIB_CLUSTERING_DIR=${SERVICE_DIR}/clustering/lib
    LIB_JARS=`ls $LIB_CLUSTERING_DIR|grep .jar | grep -v avro-ipc-1.7.7-tests.jar \
    | grep -v avro-ipc-1.7.7.jar | grep -v spark-network-common_2.10-1.5.1.jar | \
    awk '{print "'$LIB_CLUSTERING_DIR'/"$0}'|tr "\n" ":"`   ## jar包位置以及第三方依赖jar包，绝对路径

    LIB_JARS=${LIB_JARS}${OBJECT_JARS}
    if [ ! -d $LOG_DIR ]; then
        mkdir $LOG_DIR;
    fi
    java -classpath $CONF:$LIB_JARS com.hzgc.service.clustering.ClusteringApplication  | tee -a  ${LOG_FILE}
}

#####################################################################
# 函数名: start_spring_cloud_for_address
# 描述: 脚本主要业务入口
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function start_spring_cloud_for_address()
{
    CONF=${SERVICE_DIR}/address/conf
    LIB_ADDRESS_DIR=${SERVICE_DIR}/address/lib
    LIB_JARS=`ls $LIB_ADDRESS_DIR|grep .jar | grep -v avro-ipc-1.7.7-tests.jar \
    | grep -v avro-ipc-1.7.7.jar | grep -v spark-network-common_2.10-1.5.1.jar | \
    awk '{print "'$LIB_ADDRESS_DIR'/"$0}'|tr "\n" ":"`   ## jar包位置以及第三方依赖jar包，绝对路径

    LIB_JARS=${LIB_JARS}${OBJECT_JARS}
    if [ ! -d $LOG_DIR ]; then
        mkdir $LOG_DIR;
    fi
    java -classpath $CONF:$LIB_JARS com.hzgc.service.address.FtpApplication  | tee -a  ${LOG_FILE}
}

#####################################################################
# 函数名: start_all
# 描述: 启动所有spring cloud
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function start_all()
{
    start_spring_cloud_for_starepo
    start_spring_cloud_for_face
    start_spring_cloud_for_dynrepo
    start_spring_cloud_for_device
    start_spring_cloud_for_clustering
    start_spring_cloud_for_address
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
    if [ $# != 0 ]; then
        case $1 in
            [sS][tT][aA][rR][eE][pP][oO] )
                start_spring_cloud_for_starepo;;
            [fF][aA][cC][eE] )
                start_spring_cloud_for_face;;
            [dD][yY][nN][rR][eE][pP][oO] )
                start_spring_cloud_for_dynrepo;;
            [dD][eE][vV][iI][cC][eE] )
                start_spring_cloud_for_device;;
            [cC][lL][uU][sS][tT][eE][rR][iI][nN][gG] )
                start_spring_cloud_for_clustering;;
            [aA][dD][rR][eE][sS][sS] )
                start_spring_cloud_for_device;;
        esac
    else
        start_all

        check_spring_pid=$(ps -ef | grep start-check-spring-cloud.sh |grep -v grep | awk  '{print $2}' | uniq)
        if [ -n "${check_spring_pid}" ];then
            echo "check_spring_cloud is exit,nothing to do " | tee -a ${LOG_FILE}
        else
            echo "check_spring_cloud is not exit, just to start check_dubbo."   | tee -a ${LOG_FILE}
            nohup sh ${BIN_DIR}/start-check-spring-cloud.sh &
        fi
    fi
}
#---------------------------------------------------------------------#
#                              执行流程                                #
#---------------------------------------------------------------------#

## 打印时间
echo ""  | tee  -a  ${LOG_FILE}
echo ""  | tee  -a  ${LOG_FILE}
echo "==================================================="  | tee -a ${LOG_FILE}
echo "$(date "+%Y-%m-%d  %H:%M:%S")"                       | tee  -a  ${LOG_FILE}
echo "开始配置service中的conf文件"                       | tee  -a  ${LOG_FILE}
main

set +x