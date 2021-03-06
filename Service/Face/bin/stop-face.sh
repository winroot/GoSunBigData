################################################################################
##opyright:   HZGOSUN Tech. Co, BigData
## Filename:    springCloud stop face
## Description:  停止face服务
## Author:      yansen
## Created:     2018-05-18
################################################################################
#set -x


cd `dirname $0`
BIN_DIR=`pwd`                                        ##bin 目录
cd ..
FACE_DIR=`pwd`					     ##face目录
LIB_DIR=${FACE_DIR}/lib              ##lib目录地址
FACE_JAR_NAME=`ls ${LIB_DIR}| grep ^face-[0-9].[0-9].[0-9].jar$`                   ##获取运行jar包名
FACE_JAR_PID=`jps | grep ${FACE_JAR_NAME} | awk '{print $1}'`                       ##根据服务名称获取服务pid


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
    if [ -n "${FACE_JAR_PID}" ];then
       echo "Face service is exist,exit with 0, kill service now!!"
  	   ##杀掉进程
	   kill -9 ${FACE_JAR_PID}
	   echo "stop service successfully!!"
	else
	   echo "Face service is not start!!"
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