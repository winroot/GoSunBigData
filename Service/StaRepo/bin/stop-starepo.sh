################################################################################
##opyright:   HZGOSUN Tech. Co, BigData
## Filename:    springCloud stop starepo
## Description:  停止starepo服务
## Author:      chenke
## Created:     2018-05-19
################################################################################
#set -x

cd `dirname $0`
BIN_DIR=`pwd`    ##bin目录地址
cd ..
STAREPO_DIR=`pwd`    ##STAREPO目录地址
LIB_DIR=${STAREPO_DIR}/lib        ##lib目录地址
CONF_DIR=${STAREPO_DIR}/conf      ##conf目录地址
STAREPO_JAR_NAME=`ls ${LIB_DIR} | grep ^starepo-[0-9].[0-9].[0-9].jar$`
STAREPO_PID=`jps | grep ${STAREPO_JAR_NAME} | awk '{print $1}'`


#---------------------------------------------------------------------#
#                              定义函数                                #
#---------------------------------------------------------------------#



#####################################################################
# 函数名:stop_spring_cloud
# 描述: 停止spring cloud
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function stop_springCloud()
{
    if [ -n "${STAREPO_PID}" ];then
        echo "starepo service is exist, exit with 0, kill service now"
        kill -9 ${STAREPO_PID}
        echo "stop service successfully"
    else
        echo "starepo service is not start"
    fi
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
    stop_springCloud
}

main