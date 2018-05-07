#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    config-spring-cloud
## Description: 启动 spring cloud
## Author:      wujiaqi
## Created:     2018-5-5
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
LOG_FILE=$LOG_DIR/config-spring-cloud.log

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
# 函数名: config_spring_cloud_starepo
# 描述: 分发并配置每个节点下的service/starepo/conf/starepoApplication.properties
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function config_spring_cloud_starepo()
{

    echo ""  | tee -a $LOG_FILE
    echo "**********************************************" | tee -a $LOG_FILE
    echo "" | tee -a $LOG_FILE
    echo "配置starepoApplication.properties......"  | tee  -a  $LOG_FILE

    PROPERTIES_DIR=${SERVICE_DIR}/starepo/conf/starepoApplication.properties

    # 删除原本starepoApplication.properties内容（从第一行开始的行）
    if [ -f "${PROPERTIES_DIR}" ]; then
        sed -i '1,$d' ${PROPERTIES_DIR}
    fi

    DEFAULTZONE=${grep "eureka.client.serviceUrl.defaultZone" ${CONF_DIR}}
    echo ${DEFAULTZONE} >> ${PROPERTIES_DIR}
    STAREPO_SERVER_PORT=${grep "starepo.server.port" ${CONF_DIR}}
    echo ${STAREPO_SERVER_PORT} >> ${PROPERTIES_DIR}
    STAREPO_APPLICATION_NAME=${grep "starepo.spring.application.name" ${CONF_DIR}}
    echo ${STAREPO_APPLICATION_NAME} >> ${PROPERTIES_DIR}

    echo "配置starepoApplication.properties完毕......"  | tee  -a  $LOG_FILE

}

#####################################################################
 # 函数名: config_spring_cloud_face
 # 描述: 分发并配置每个节点下的service/face/conf/faceApplication.properties
 # 参数: N/A
 # 返回值: N/A
 # 其他: N/A
 #####################################################################
 function config_spring_cloud_face()
 {

     echo ""  | tee -a $LOG_FILE
     echo "**********************************************" | tee -a $LOG_FILE
     echo "" | tee -a $LOG_FILE
     echo "配置faceApplication.properties......"  | tee  -a  $LOG_FILE

     PROPERTIES_DIR=${SERVICE_DIR}/face/conf/faceApplication.properties

     # 删除原本faceApplication.properties内容（从第一行开始的行）
     if [ -f "${PROPERTIES_DIR}" ]; then
         sed -i '1,$d' ${PROPERTIES_DIR}
     fi

     DEFAULTZONE=${grep "eureka.client.serviceUrl.defaultZone" ${CONF_DIR}}
     echo ${DEFAULTZONE} >> ${PROPERTIES_DIR}
     FACE_SERVER_PORT=${grep "face.server.port" ${CONF_DIR}}
     echo ${FACE_SERVER_PORT} >> ${PROPERTIES_DIR}
     FACE_APPLICATION_NAME=${grep "face.spring.application.name" ${CONF_DIR}}
     echo ${FACE_APPLICATION_NAME} >> ${PROPERTIES_DIR}

     echo "配置faceApplication.properties完毕......"  | tee  -a  $LOG_FILE
 }

 #####################################################################
  # 函数名: config_spring_cloud_dynrepo
  # 描述: 分发并配置每个节点下的service/dynrepo/conf/faceApplication.properties
  # 参数: N/A
  # 返回值: N/A
  # 其他: N/A
  #####################################################################
  function config_spring_cloud_dynrepo()
  {

      echo ""  | tee -a $LOG_FILE
      echo "**********************************************" | tee -a $LOG_FILE
      echo "" | tee -a $LOG_FILE
      echo "配置dynrepoApplication.properties......"  | tee  -a  $LOG_FILE

      PROPERTIES_DIR=${SERVICE_DIR}/dynrepo/conf/dynrepoApplication.properties

      # 删除原本dynrepoApplication.properties内容（从第一行开始的行）
      if [ -f "${PROPERTIES_DIR}" ]; then
          sed -i '1,$d' ${PROPERTIES_DIR}
      fi

      DEFAULTZONE=${grep "eureka.client.serviceUrl.defaultZone" ${CONF_DIR}}
      echo ${DEFAULTZONE} >> ${PROPERTIES_DIR}
      DYNREPO_SERVER_PORT=${grep "dynrepo.server.port" ${CONF_DIR}}
      echo ${DYNREPO_SERVER_PORT} >> ${PROPERTIES_DIR}
      DYNREPO_APPLICATION_NAME=${grep "dynrepo.spring.application.name" ${CONF_DIR}}
      echo ${DYNREPO_APPLICATION_NAME} >> ${PROPERTIES_DIR}

      echo "配置dynrepoApplication.properties完毕......"  | tee  -a  $LOG_FILE
  }

 #####################################################################
  # 函数名: config_spring_cloud_device
  # 描述: 分发并配置每个节点下的service/device/conf/deviceApplication.properties
  # 参数: N/A
  # 返回值: N/A
  # 其他: N/A
  #####################################################################
  function config_spring_cloud_device()
  {

      echo ""  | tee -a $LOG_FILE
      echo "**********************************************" | tee -a $LOG_FILE
      echo "" | tee -a $LOG_FILE
      echo "配置deviceApplication.properties......"  | tee  -a  $LOG_FILE

      PROPERTIES_DIR=${SERVICE_DIR}/device/conf/deviceApplication.properties

      # 删除原本deviceApplication.properties内容（从第一行开始的行）
      if [ -f "${PROPERTIES_DIR}" ]; then
          sed -i '1,$d' ${PROPERTIES_DIR}
      fi

      DEFAULTZONE=${grep "eureka.client.serviceUrl.defaultZone" ${CONF_DIR}}
      echo ${DEFAULTZONE} >> ${PROPERTIES_DIR}
      DEVICE_SERVER_PORT=${grep "device.server.port" ${CONF_DIR}}
      echo ${DEVICE_SERVER_PORT} >> ${PROPERTIES_DIR}
      DEVICE_APPLICATION_NAME=${grep "device.spring.application.name" ${CONF_DIR}}
      echo ${DEVICE_APPLICATION_NAME} >> ${PROPERTIES_DIR}

      echo "配置deviceApplication.properties完毕......"  | tee  -a  $LOG_FILE
  }

   #####################################################################
    # 函数名: config_spring_cloud_clustering
    # 描述: 分发并配置每个节点下的service/clustering/conf/clusteringApplication.properties
    # 参数: N/A
    # 返回值: N/A
    # 其他: N/A
    #####################################################################
    function config_spring_cloud_clustering()
    {

        echo ""  | tee -a $LOG_FILE
        echo "**********************************************" | tee -a $LOG_FILE
        echo "" | tee -a $LOG_FILE
        echo "配置clusteringApplication.properties......"  | tee  -a  $LOG_FILE

        PROPERTIES_DIR=${SERVICE_DIR}/clustering/conf/clusteringApplication.properties

        # 删除原本clusteringApplication.properties内容（从第一行开始的行）
        if [ -f "${PROPERTIES_DIR}" ]; then
            sed -i '1,$d' ${PROPERTIES_DIR}
        fi

        DEFAULTZONE=${grep "eureka.client.serviceUrl.defaultZone" ${CONF_DIR}}
        echo ${DEFAULTZONE} >> ${PROPERTIES_DIR}
        CLUSTERING_SERVER_PORT=${grep "clustering.server.port" ${CONF_DIR}}
        echo ${CLUSTERING_SERVER_PORT} >> ${PROPERTIES_DIR}
        CLUSTERING_APPLICATION_NAME=${grep "clustering.spring.application.name" ${CONF_DIR}}
        echo ${CLUSTERING_APPLICATION_NAME} >> ${PROPERTIES_DIR}

        echo "配置clusteringApplication.properties完毕......"  | tee  -a  $LOG_FILE
    }

   #####################################################################
    # 函数名: config_spring_cloud_address
    # 描述: 分发并配置每个节点下的service/address/conf/addressApplication.properties
    # 参数: N/A
    # 返回值: N/A
    # 其他: N/A
    #####################################################################
    function config_spring_cloud_address()
    {

        echo ""  | tee -a $LOG_FILE
        echo "**********************************************" | tee -a $LOG_FILE
        echo "" | tee -a $LOG_FILE
        echo "配置addressApplication.properties......"  | tee  -a  $LOG_FILE

        PROPERTIES_DIR=${SERVICE_DIR}/address/conf/addressApplication.properties

        # 删除原本addressApplication.properties内容（从第一行开始的行）
        if [ -f "${PROPERTIES_DIR}" ]; then
            sed -i '1,$d' ${PROPERTIES_DIR}
        fi

        DEFAULTZONE=${grep "eureka.client.serviceUrl.defaultZone" ${CONF_DIR}}
        echo ${DEFAULTZONE} >> ${PROPERTIES_DIR}
        ADDRESS_SERVER_PORT=${grep "address.server.port" ${CONF_DIR}}
        echo ${ADDRESS_SERVER_PORT} >> ${PROPERTIES_DIR}
        ADDRESS_APPLICATION_NAME=${grep "address.spring.application.name" ${CONF_DIR}}
        echo ${ADDRESS_APPLICATION_NAME} >> ${PROPERTIES_DIR}

        echo "配置addressApplication.properties完毕......"  | tee  -a  $LOG_FILE
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
    config_spring_cloud_starepo
    config_spring_cloud_face
    config_spring_cloud_dynrepo
    config_spring_cloud_device
    config_spring_cloud_clustering
    config_spring_cloud_address
}


#---------------------------------------------------------------------#
#                              执行流程                                #
#---------------------------------------------------------------------#

## 打印时间
echo ""  | tee  -a  $LOG_FILE
echo ""  | tee  -a  $LOG_FILE
echo "==================================================="  | tee -a $LOG_FILE
echo "$(date "+%Y-%m-%d  %H:%M:%S")"                       | tee  -a  $LOG_FILE
echo "开始配置service中的spring cloud conf文件"                       | tee  -a  $LOG_FILE
main

set +x
