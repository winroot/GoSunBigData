#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    springCloud start address
## Description: 启动 face服务提取特征
## Author:      yansen
## Created:     2018-05-19
################################################################################
#set -x



cd `dirname $0`

BIN_DIR=`pwd`               ##bin目录地址
cd ..                    
cd lib
LIB_DIR=`pwd`               ##lib目录地址
ADDRESS_JAR_NAME=`ls | grep ^address-[0-9].[0-9].[0-9].jar$`    ##获取jar包名称
ADDRESS_JAR=${LIB_DIR}/${ADDRESS_JAR_NAME}       ##获取jar包的全路径
echo `pwd`
cd ..
echo `pwd`




#---------------------------------------------------------------------#
#                          springcloud配置参数                        #
#---------------------------------------------------------------------#
EUREKA_IP=172.18.18.201     ##注册中心的ip地址
SERVER_IP=172.18.18.104     ##服务的ip地址
EUREKA_PORT=9000	        ##服务注册中心端口

#---------------------------------------------------------------------#
#                          ftp ip hostname 配置参数                        #
#---------------------------------------------------------------------#
FTP_PROXY_IP=172.18.18.163
FTP_PROXY_HOSTNAME=s120
FTP_HOSTNAME_MAPPING=s120:172.18.18.163;s103:172.18.18.103;s105:172.18.18.105

#---------------------------------------------------------------------#
#                          Ftp Subscription 相关配置                        #
#---------------------------------------------------------------------#
ZK_ADDRESS=172.18.18.163:2181


#---------------------------------------------------------------------#
#                              定义函数                               #
#---------------------------------------------------------------------#

#####################################################################
# 函数名: start_spring_cloud
# 描述: 启动 springCloud face服务
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function start_springCloud_address()
{

   nohup java -jar ${ADDRESS_JAR} --spring.profiles.active=pro  --eureka.ip=${EUREKA_IP} --eureka.port=${EUREKA_PORT} --server.ip=${SERVER_IP}  --ftp.proxy.ip=${FTP_PROXY_IP} --ftp.proxy.hostname=${FTP_PROXY_HOSTNAME} --ftp.hostname.mapping=${FTP_HOSTNAME_MAPPING} --zk.address=${ZK_ADDRESS}  2&>1 &

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
    start_springCloud_address
}




main
