#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    ftpoverkafka
## Description: stop  ftp
## Author:      liushanbin
## Created:     2018-01-08
################################################################################

#set -x
cd `dirname $0`
## 脚本所在目录
BIN_DIR=`pwd`
cd ..
## ftp根目录
FTP_DIR=`pwd`
## log 日记目录
LOG_DIR=${FTP_DIR}/logs
##log日志文件
source /etc/profile
stop_ftp=1                                                      ## 判断ftp是否关闭成功 1->失败 0->成功 默认失败
stop_check_ftp=1


#####################################################################
# 函数名:stopftp 
# 描述: 停止ftp
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function stopftp ()
{
    ftp_pid=$(jps | grep FTP | awk '{print $1}')
    if [ -n "${ftp_pid}" ];then
        echo "ftp pid is ${ftp_pid}, stop now "
        kill -9 ${ftp_pid}
        sleep 1s
        ftp_pid=$(jps | grep FTP | awk '{print $1}')
        if [ -n "${ftp_pid}" ];then
            stop_ftp=1
            echo "stop ftp failure"
        else
            stop_ftp=0
            echo "stop ftp sucessfull"
        fi
    else 
        echo "ftp process is not exit"
        stop_ftp=0
    fi
}

#####################################################################
# 函数名: main
# 描述:  停止dubbo的入口函数
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function main()
{
    stopftp
}
main
