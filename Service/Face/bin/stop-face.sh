################################################################################
##opyright:   HZGOSUN Tech. Co, BigData
## Filename:    springCloud stop face
## Description:  停止face服务
## Author:      yansen
## Created:     2018-05-18
################################################################################
set -x


cd `dirname $0`
BIN_DIR=`pwd`                                        ##bin 目录
cd ..
TEST_DIR=`pwd`					     ##test目录
cd lib
#SERVER_PORT=7777                                    ##face服务端口号
FACE_JAR_NAME=`ls ${LIB_DIR}| grep ^face-[0-9].[0-9].[0-9].jar$`                   ##获取运行jar包名
FACE_JAR_PID=`jps | grep ${FACE_JAR_NAME} | awk '{print $1}'`                       ##根据服务名称获取服务pid
cd ..
echo `pwd`
cd ${BIN_DIR}
echo `pwd`
#FACE_SERVER_PID=`lsof -i:${SERVER_PORT} | sed -n '2p' | awk '{print $2}'`          ##根据服务端口号获取服务pid




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
  	##杀掉进程
	kill -9 ${FACE_JAR_PID}
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
