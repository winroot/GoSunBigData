#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    config-spring-cloud
## Description: 配置 spring cloud
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
CONF_FILE=${OBJECT_DIR}/common/conf/project-conf.properties
OBJECT_JARS=`ls ${OBJECT_LIB_DIR} | grep .jar | awk '{print "'${OBJECT_LIB_DIR}'/"$0}'|tr "\n" ":"`

if [ ! -d $LOG_DIR ]; then
    mkdir $LOG_DIR;
fi

#---------------------------------------------------------------------#
#                              定义函数                                #
#---------------------------------------------------------------------#

#####################################################################
# 函数名: config_spring_cloud_starepo
# 描述: 配置service/starepo/conf/starepoApplication.properties
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

    DEFAULTZONE=${grep "eureka_client_serviceUrl_defaultZone" ${CONF_FILE} | cut -d '=' -f2}
    echo "eureka.client.serviceUrl.defaultZone=${DEFAULTZONE}" >> ${PROPERTIES_DIR}

    # STAREPO_SERVER_PORT=${grep "starepo.server.port" ${CONF_FILE} | cut -d '=' -f2}
    echo "server.port=7776" >> ${PROPERTIES_DIR}

    # STAREPO_APPLICATION_NAME=${grep "starepo.spring.application.name" ${CONF_FILE} | cut -d '=' -f2}
    echo "spring.application.name=staRepo" >> ${PROPERTIES_DIR}

    value1=${grep "phoenix_enable" ${CONF_FILE} | cut -d '=' -f2}
    echo "phoenix.enable=${value1}" >> ${PROPERTIES_DIR}

    value2=${grep "phoenix_url" ${CONF_FILE} | cut -d '=' -f2}
    echo "phoenix.url=${value2}" >> ${PROPERTIES_DIR}

    value3=${grep "phoenix_type" ${CONF_FILE} | cut -d '=' -f2}
    echo "phoenix.type=${value3}" >> ${PROPERTIES_DIR}

    value4=${grep "phoenix_driver-class-name" ${CONF_FILE} | cut -d '=' -f2}
    echo "phoenix.driver-class-name=${value4}" >> ${PROPERTIES_DIR}

    value5=${grep "phoenix_default-auto-commit" ${CONF_FILE} | cut -d '=' -f2}
    echo "phoenix.default-auto-commit=${value5}" >> ${PROPERTIES_DIR}

    echo "table.name=objectinfo" >> ${PROPERTIES_DIR}
    echo "table.colfams=person" >> ${PROPERTIES_DIR}
    echo "table.maxversion=1" >> ${PROPERTIES_DIR}

    echo "配置starepoApplication.properties完毕......"  | tee  -a  $LOG_FILE

}

#####################################################################
 # 函数名: config_spring_cloud_face
 # 描述: 配置service/face/conf/faceApplication.properties
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

     DEFAULTZONE=${grep "eureka_client_serviceUrl_defaultZone" ${CONF_FILE} | cut -d '=' -f2}
     echo "eureka.client.serviceUrl.defaultZone=${DEFAULTZONE}" >> ${PROPERTIES_DIR}
     # FACE_SERVER_PORT=${grep "face.server.port" ${CONF_FILE} | cut -d '=' -f2}
     echo "server.port=7775" >> ${PROPERTIES_DIR}
     # FACE_APPLICATION_NAME=${grep "face.spring.application.name" ${CONF_FILE} | cut -d '=' -f2}
     echo "spring.application.name=face" >> ${PROPERTIES_DIR}

     echo "配置faceApplication.properties完毕......"  | tee  -a  $LOG_FILE
 }

 #####################################################################
  # 函数名: config_spring_cloud_dynrepo
  # 描述: 配置service/dynrepo/conf/faceApplication.properties
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

      DEFAULTZONE=${grep "eureka_client_serviceUrl_defaultZone" ${CONF_FILE} | cut -d '=' -f2}
      echo "eureka.client.serviceUrl.defaultZone=${DEFAULTZONE}" >> ${PROPERTIES_DIR}
      # DYNREPO_SERVER_PORT=${grep "dynrepo.server.port" ${CONF_FILE} | cut -d '=' -f2}
      echo "server.port=7774" >> ${PROPERTIES_DIR}
      # DYNREPO_APPLICATION_NAME=${grep "dynrepo.spring.application.name" ${CONF_FILE} | cut -d '=' -f2}
      echo "spring.application.name=dynRepo" >> ${PROPERTIES_DIR}

      value1=${grep "es_cluster_name" ${CONF_FILE} | cut -d '=' -f2}
      echo "es.cluster.name=${value1}" >> ${PROPERTIES_DIR}

      value2=${grep "es_hosts" ${CONF_FILE} | cut -d '=' -f2}
      echo "es.hosts=${value2}" >> ${PROPERTIES_DIR}

      value3=${grep "es_cluster_port" ${CONF_FILE} | cut -d '=' -f2}
      echo "es.cluster.port=${value3}" >> ${PROPERTIES_DIR}

      value4=${grep "hive_jdbc_driver" ${CONF_FILE} | cut -d '=' -f2}
      echo "hive.jdbc.driver=${value3}" >> ${PROPERTIES_DIR}

      value5=${grep "hive_jdbc_url" ${CONF_FILE} | cut -d '=' -f2}
      echo "hive.jdbc.url=${value3}" >> ${PROPERTIES_DIR}
      echo "配置dynrepoApplication.properties完毕......"  | tee  -a  $LOG_FILE
  }

 #####################################################################
  # 函数名: config_spring_cloud_device
  # 描述: 配置service/device/conf/deviceApplication.properties
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

      DEFAULTZONE=${grep "eureka_client_serviceUrl_defaultZone" ${CONF_FILE} | cut -d '=' -f2}
      echo "eureka.client.serviceUrl.defaultZone=${DEFAULTZONE}" >> ${PROPERTIES_DIR}
      # DEVICE_SERVER_PORT=${grep "device.server.port" ${CONF_FILE} | cut -d '=' -f2}
      echo "server.port=7773" >> ${PROPERTIES_DIR}
      # DEVICE_APPLICATION_NAME=${grep "device.spring.application.name" ${CONF_FILE} | cut -d '=' -f2}
      echo "spring.application.name=device" >> ${PROPERTIES_DIR}

      echo "配置deviceApplication.properties完毕......"  | tee  -a  $LOG_FILE
  }

   #####################################################################
    # 函数名: config_spring_cloud_clustering
    # 描述: 配置service/clustering/conf/clusteringApplication.properties
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

        DEFAULTZONE=${grep "eureka_client_serviceUrl_defaultZone" ${CONF_FILE} | cut -d '=' -f2}
        echo "eureka.client.serviceUrl.defaultZone=${DEFAULTZONE}" >> ${PROPERTIES_DIR}
        # CLUSTERING_SERVER_PORT=${grep "clustering.server.port" ${CONF_FILE} | cut -d '=' -f2}
        echo "server.port=7772" >> ${PROPERTIES_DIR}
        # CLUSTERING_APPLICATION_NAME=${grep "clustering.spring.application.name" ${CONF_FILE} | cut -d '=' -f2}
        echo "spring.application.name=clustering" >> ${PROPERTIES_DIR}

        value1=${grep "es_hosts" ${CONF_FILE} | cut -d '=' -f2}
        echo "es.hosts=${value1}" >> ${PROPERTIES_DIR}

        value2=${grep "es_cluster_port" ${CONF_FILE} | cut -d '=' -f2}
        echo "es.cluster.port=${value2}" >> ${PROPERTIES_DIR}

        value3=${grep "es_cluster_name" ${CONF_FILE} | cut -d '=' -f2}
        echo "es.cluster.name=${value3}" >> ${PROPERTIES_DIR}

        echo "配置clusteringApplication.properties完毕......"  | tee  -a  $LOG_FILE
    }

   #####################################################################
    # 函数名: config_spring_cloud_address
    # 描述: 配置service/address/conf/addressApplication.properties
    # 参数: N/A
    # 返回值: N/A
    # 其他: N/A
    #####################################################################
    function config_spring_cloud_address()
    {

        echo ""  | tee -a $LOG_FILE
        echo "**********************************************" | tee -a $LOG_FILE
        echo "" | tee -a $LOG_FILE
        echo "配置ftpApplication.properties......"  | tee  -a  $LOG_FILE

        PROPERTIES_DIR=${SERVICE_DIR}/address/conf/ftpApplication.properties

        # 删除原本ftpApplication.properties内容（从第一行开始的行）
        if [ -f "${PROPERTIES_DIR}" ]; then
            sed -i '1,$d' ${PROPERTIES_DIR}
        fi

        DEFAULTZONE=${grep "eureka_client_serviceUrl_defaultZone" ${CONF_FILE} | cut -d '=' -f2}
        echo "eureka.client.serviceUrl.defaultZone=${DEFAULTZONE}" >> ${PROPERTIES_DIR}
        # ADDRESS_SERVER_PORT=${grep "address.server.port" ${CONF_FILE} | cut -d '=' -f2}
        echo "server.port=7770" >> ${PROPERTIES_DIR}
        # ADDRESS_APPLICATION_NAME=${grep "address.spring.application.name" ${CONF_FILE} | cut -d '=' -f2}
        echo "spring.application.name=ftp" >> ${PROPERTIES_DIR}

        value1=${grep "ftp_proxy_ip" ${CONF_FILE} | cut -d '=' -f2}
        echo "ip=${value1}" >> ${PROPERTIES_DIR}

        value2=${grep "ftp_proxy_hostname" ${CONF_FILE} | cut -d '=' -f2}
        echo "hostname=${value2}" >> ${PROPERTIES_DIR}

        value3=${grep "ftp_proxy_port" ${CONF_FILE} | cut -d '=' -f2}
        echo "ftp.proxy.port=${value3}" >> ${PROPERTIES_DIR}

        # value4=${grep "ftp.username" ${CONF_FILE} | cut -d '=' -f2}
        echo "ftp.username=admin" >> ${PROPERTIES_DIR}

        # value5=${grep "ftp.password" ${CONF_FILE} | cut -d '=' -f2}
        echo "ftp.password=123456" >> ${PROPERTIES_DIR}

        # value6=${grep "ftp.pathRule" ${CONF_FILE} | cut -d '=' -f2}
        echo "ftp.pathRule=%f/%Y/%m/%d/%H" >> ${PROPERTIES_DIR}

        value7=${grep "ftp_hostname_mapping" ${CONF_FILE} | cut -d '=' -f2}
        echo "ftp.hostname.mapping=${value7}" >> ${PROPERTIES_DIR}

        value8=${grep "zk_session_timeout" ${CONF_FILE} | cut -d '=' -f2}
        echo "zk.session.timeout=${value8}" >> ${PROPERTIES_DIR}

        value9=${grep "zk_address" ${CONF_FILE} | cut -d '=' -f2}
        echo "zk.address=${value9}" >> ${PROPERTIES_DIR}

        value10=${grep "zk_path_subscribe" ${CONF_FILE} | cut -d '=' -f2}
        echo "zk.path.subscribe=${value10}" >> ${PROPERTIES_DIR}

        value11=${grep "zk_watcher" ${CONF_FILE} | cut -d '=' -f2}
        echo "zk.watcher=${value11}" >> ${PROPERTIES_DIR}

        echo "配置ftpApplication.properties完毕......"  | tee  -a  $LOG_FILE
    }

   #####################################################################
    # 函数名: config_spring_cloud_visual
    # 描述: 配置service/visual/conf/visualApplication.properties
    # 参数: N/A
    # 返回值: N/A
    # 其他: N/A
    #####################################################################
    function config_spring_cloud_visual()
    {

        echo ""  | tee -a $LOG_FILE
        echo "**********************************************" | tee -a $LOG_FILE
        echo "" | tee -a $LOG_FILE
        echo "配置visualApplication.properties......"  | tee  -a  $LOG_FILE

        PROPERTIES_DIR=${SERVICE_DIR}/visual/conf/visualApplication.properties

        # 删除原本visualApplication.properties内容（从第一行开始的行）
        if [ -f "${PROPERTIES_DIR}" ]; then
            sed -i '1,$d' ${PROPERTIES_DIR}
        fi

        DEFAULTZONE=${grep "eureka_client_serviceUrl_defaultZone" ${CONF_FILE} | cut -d '=' -f2}
        echo "eureka.client.serviceUrl.defaultZone=${DEFAULTZONE}" >> ${PROPERTIES_DIR}
        # VISUAL_SERVER_PORT=${grep "visual.server.port" ${CONF_FILE} | cut -d '=' -f2}
        echo "server.port=7778" >> ${PROPERTIES_DIR}
        # VISUAL_APPLICATION_NAME=${grep "visual.spring.application.name" ${CONF_FILE} | cut -d '=' -f2}
        echo "spring.application.name=visual" >> ${PROPERTIES_DIR}

        value1=${grep "es_cluster_name" ${CONF_FILE} | cut -d '=' -f2}
        echo "es.cluster.name=${value1}" >> ${PROPERTIES_DIR}

        value2=${grep "es_hosts" ${CONF_FILE} | cut -d '=' -f2}
        echo "es.hosts=${value2}" >> ${PROPERTIES_DIR}

        value3=${grep "es_cluster_port" ${CONF_FILE} | cut -d '=' -f2}
        echo "es.cluster.port=${value3}" >> ${PROPERTIES_DIR}

        echo "配置visualApplication.properties完毕......"  | tee  -a  $LOG_FILE
    }


#####################################################################
# 函数名: config
# 描述: 脚本主要业务入口
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################

function config()
{
     case $class in
         [sS][tT][aA][rR][eE][pP][oO] )
             config_spring_cloud_starepo;;
         [fF][aA][cC][eE] )
             config_spring_cloud_face;;
         [dD][yY][nN][rR][eE][pP][oO] )
             config_spring_cloud_dynrepo;;
         [dD][eE][vV][iI][cC][eE] )
             config_spring_cloud_device;;
         [cC][lL][uU][sS][tT][eE][rR][iI][nN][gG] )
             config_spring_cloud_clustering;;
         [aA][dD][dD][rR][eE][sS][sS] )
             config_spring_cloud_address;;
         [vV][iI][sS][uU][aA][lL] )
             config_spring_cloud_visual;;
     esac
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
    SPRING_CLASS=$(grep spring_cloud_service_classes ${CONF_FILE}|cut -d '=' -f2)
    spring_arr=(${SPRING_CLASS//;/ })
    for spring_class in ${spring_arr[@]}
    do
        echo "停止${spring_class}................."  | tee  -a  $LOG_FILE
        class=${spring_class}
        config
    done
    stop_check_spring_cloud
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
