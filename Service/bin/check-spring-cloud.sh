#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    check-spring-cloud.sh
## Description: 大数据spring cloud 守护脚本
## Author:      wujiaqi
## Created:     2018-01-08
################################################################################
#set -x  ## 用于调试用，不用的时候可以注释掉

#crontab 里面不会读取jdk环境变量的值
source /etc/profile

#set -x



#---------------------------------------------------------------------#
#                              定义变量                               #
#---------------------------------------------------------------------#
cd `dirname $0`
BIN_DIR=`pwd`                               ### bin 目录

cd ..
SERVICE_DIR=`pwd`                           ### service 目录
CONF_SERVICE_DIR=$SERVICE_DIR/conf          ### service 配置文件
LIB_STAREPO_DIR=${SERVICE_DIR}/starepo/lib  ### starepo lib
LOG_DIR=${SERVICE_DIR}/logs                 ### LOG 目录
CHECK_LOG_FILE=$LOG_DIR/check-spring-cloud.log

cd ..
OBJECT_DIR=`pwd`                            ### RealTimeFaceCompare 目录
OBJECT_LIB_DIR=${OBJECT_DIR}/lib            ### lib
OBJECT_JARS=`ls ${OBJECT_LIB_DIR} | grep .jar | awk '{print "'${OBJECT_LIB_DIR}'/"$0}'|tr "\n" ":"`

if [ ! -d $LOG_DIR ]; then
    mkdir $LOG_DIR;
fi

#####################################################################
# 函数名: check
# 描述: 脚本主要业务入口
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################

function check()
{
     case $class in
         [sS][tT][aA][rR][eE][pP][oO] )
             sh ${SERVICE_DIR}/starepo/bin/check-spring-cloud-starepo.sh;;
         [fF][aA][cC][eE] )
             sh ${SERVICE_DIR}/face/bin/check-spring-cloud-face.sh;;
         [dD][yY][nN][rR][eE][pP][oO] )
             sh ${SERVICE_DIR}/dynrepo/bin/check-spring-cloud-dynrepo.sh;;
         [dD][eE][vV][iI][cC][eE] )
             sh ${SERVICE_DIR}/device/bin/check-spring-cloud-device.sh;;
         [cC][lL][uU][sS][tT][eE][rR][iI][nN][gG] )
             sh ${SERVICE_DIR}/clustering/bin/check-spring-cloud-clustering.sh;;
         [aA][dD][dD][rR][eE][sS][sS] )
             sh ${SERVICE_DIR}/address/bin/check-spring-cloud-address.sh;;
         [vV][iI][sS][uU][aA][lL] )
             sh ${SERVICE_DIR}/visual/bin/check-spring-cloud-address.sh;;
     esac
}
#####################################################################
# 函数名: check_all
# 描述: 检查所有spring cloud
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function check_all()
{

    SPRING_CLASS=$(grep spring_cloud_service_classes ${CONF_FILE}|cut -d '=' -f2)
    spring_arr=(${SPRING_CLASS//;/ })
    for spring_class in ${spring_arr[@]}
    do
        echo "检查${spring_class}................."  | tee  -a  $CHECK_LOG_FILE
        class=${spring_class}
        check
    done
}

#####################################################################
# 函数名: main
# 描述: 模块功能main 入口，即程序入口, 用来监听整个大数据服务的情况。
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function main()
{
   while true
   do
       check_all
       sleep 5m
   done
}


# 主程序入口
main
