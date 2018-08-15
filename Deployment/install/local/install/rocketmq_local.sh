#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    rocketmq_local.sh
## Description: rocketmq安装
## Version:     2.4
## Author:      yinhang
## Created:     2018-07-28
################################################################################
## set -x

#---------------------------------------------------------------------#
#                              定义变量                                #
#---------------------------------------------------------------------#
cd `dirname $0`
## 脚本所在目录
BIN_DIR=`pwd`
cd ../../..
## 安装包根目录
ROOT_HOME=`pwd`
## 配置文件目录
CONF_DIR=${ROOT_HOME}/conf
## 安装日志目录
LOG_DIR=${ROOT_HOME}/logs
## 安装日志目录
LOG_FILE=${LOG_DIR}/rocketMQInstall.log
## rocketmq 安装包目录
ROCKETMQ_SOURCE_DIR=${ROOT_HOME}/component/bigdata
## 最终安装的根目录，所有bigdata 相关的根目录：/opt/hzgc/bigdata
INSTALL_HOME=$(grep Install_HomeDir ${CONF_DIR}/cluster_conf.properties|cut -d '=' -f2)
## rocketmq 集群节点
ROCKETMQ_HOST=`hostname -i`
## rocketmq 安装目录
ROCKETMQ_INSTALL_HOME=${INSTALL_HOME}/RocketMQ
## rocketmq 根目录
ROCKETMQ_HOME=${ROCKETMQ_INSTALL_HOME}/rocketmq

##RocketMQ存储路径
ROCKETMQ_STORE=${ROCKETMQ_HOME}/store
##RocketMQ commitLog 存储路径
ROCKETMQ_COMMITLOG=${ROCKETMQ_STORE}/commitlog
##消费队列存储路径存储路径
ROCKETMQ_CONSUMEQUE=${ROCKETMQ_STORE}/consumequeue
##消息索引存储路径
ROCKETMQ_INDEX=${ROCKETMQ_STORE}/index
##checkpoint 文件存储路径
ROCKETMQ_CHECKPOINT=${ROCKETMQ_STORE}/checkpoint
##abort 文件存储路径
ROCKETMQ_ABORT=${ROCKETMQ_STORE}/abort

##rocketMq 日志目录
ROCKETMQ_LOG=$(grep Cluster_LOGSDir ${CONF_DIR}/cluster_conf.properties|cut -d '=' -f2)

if [ ! -d ${LOG_DIR} ];then
    mkdir -p ${LOG_DIR};
fi
if [ ! -d ${ROCKETMQ_LOG} ];then
    mkdir -p ${ROCKETMQ_LOG};
fi

## 首先检查本机上是否安装有 rocketmq 如果有，则删除本机的 rocketmq
if [ -e ${ROCKETMQ_HOME} ];then
    echo "删除原有 rocketmq"
    rm -rf ${ROCKETMQ_HOME}
fi
mkdir -p ${ROCKETMQ_INSTALL_HOME}
cp -r ${ROCKETMQ_SOURCE_DIR}/rocketmq ${ROCKETMQ_INSTALL_HOME}
chmod -R 755 ${ROCKETMQ_INSTALL_HOME}

#####################################################################
# 函数名:etc_profile
# 描述: 判断 /etc/profile 文件是否存在export NAMESRV_ADDR=172.18.18.186:9876这一行，若存在则替换，若不存在则追加
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function etc_profile ()
{
namesrv_exists=$(grep "export NAMESRV_ADDR=" /etc/profile)
if [ -n "${namesrv_exists}" ];then
    ## 存在就替换
    sed -i "s#export NAMESRV_ADDR=.*#export NAMESRV_ADDR=${ROCKETMQ_HOST}:9876#g" /etc/profile
else
    ## 不存在就添加
    echo export NAMESRV_ADDR="${ROCKETMQ_HOST}:9876"  >> /etc/profile
    echo "">> /etc/profile
fi
source /etc/profile
}

#####################################################################
# 函数名:2m_noslave_broker
# 描述: 修改 2m-noslave下面 broker文件
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function noslave_broker ()
{
echo "************************************************"
echo "准备修改${ROCKETMQ_HOST}节点下的broker配置文件：" | tee -a $LOG_FILE

## 修改 ${ROCKETMQ_HOST} 节点下的 broker 配置文件名字
mv ${ROCKETMQ_HOME}/conf/2m-noslave/*.properties ${ROCKETMQ_HOME}/conf/2m-noslave/broker-${ROCKETMQ_HOST}.properties

## 修改 broker 配置文件中的 brokername
sed -i "s#brokerName=.*#brokerName=broker-${ROCKETMQ_HOST}#g" ${ROCKETMQ_HOME}/conf/2m-noslave/broker-${ROCKETMQ_HOST}.properties

## 添加brokerIP1
sed -i "s#brokerIP1=.*#brokerIP1=${ROCKETMQ_HOST}#g" ${ROCKETMQ_HOME}/conf/2m-noslave/broker-${ROCKETMQ_HOST}.properties
flag1=$?

## 修改 storePathRootDir
sed -i "s#storePathRootDir=.*#storePathRootDir=${ROCKETMQ_STORE}#g" ${ROCKETMQ_HOME}/conf/2m-noslave/broker-${ROCKETMQ_HOST}.properties
flag2=$?

## 修改 storePathCommitLog
sed -i "s#storePathCommitLog=.*#storePathCommitLog=${ROCKETMQ_COMMITLOG}#g" ${ROCKETMQ_HOME}/conf/2m-noslave/broker-${ROCKETMQ_HOST}.properties
flag3=$?

## 修改storePathConsumeQueue
sed -i "s#storePathConsumeQueue=.*#storePathConsumeQueue=${ROCKETMQ_CONSUMEQUE}#g" ${ROCKETMQ_HOME}/conf/2m-noslave/broker-${ROCKETMQ_HOST}.properties
flag4=$?

## 修改storePathIndex
sed -i "s#storePathIndex=.*#storePathIndex=${ROCKETMQ_INDEX}#g" ${ROCKETMQ_HOME}/conf/2m-noslave/broker-${ROCKETMQ_HOST}.properties
flag5=$?

## 修改storeCheckpoint
sed -i "s#storeCheckpoint=.*#storeCheckpoint=${ROCKETMQ_CHECKPOINT}#g" ${ROCKETMQ_HOME}/conf/2m-noslave/broker-${ROCKETMQ_HOST}.properties
flag6=$?

## 修改abortFile
sed -i "s#abortFile=.*#abortFile=${ROCKETMQ_ABORT}#g" ${ROCKETMQ_HOME}/conf/2m-noslave/broker-${ROCKETMQ_HOST}.properties
flag7=$?

sed -i "s#\${user.home}/logs#${ROCKETMQ_LOG}\/rocketmq#g" ${ROCKETMQ_HOME}/conf/*.xml
flag8=$?

if [[ ($flag1 == 0) && ($flag2 == 0) && ($flag3 == 0) && ($flag4 == 0) && ($flag5 == 0) && ($flag6 == 0) && ($flag7 == 0) && ($flag8 == 0) ]];then
    echo "配置 brokerproperties 完成" | tee -a $LOG_FILE
else
    echo "配置 brokerproperties 失败" | tee -a $LOG_FILE
fi

echo "修改 ${ROCKETMQ_HOST} 节点下的 broker 配置文件完成" | tee -a $LOG_FILE
}

function rocketmq_webUI ()
{
## 将RocketMQ的UI地址写到指定文件中
echo ""  | tee -a $LOG_FILE
echo "**********************************************" | tee -a $LOG_FILE
echo "准备将RocketMQ的UI地址写到指定文件中............"    | tee -a $LOG_FILE
RocketMQWebUI_Dir=$(grep WebUI_Dir ${CONF_DIR}/cluster_conf.properties|cut -d '=' -f2)
RocketMQ_UI="http://${ROCKETMQ_HOST}:8083"
mkdir -p ${RocketMQWebUI_Dir}
VALUE=$(grep "RocketMQUI_Address=" ${RocketMQWebUI_Dir}/WebUI_Address)
if [ -n "${VALUE}" ]  ;then
    sed -i "s#^RocketMQUI_Add0ress=.*#RocketMQUI_Address=${RocketMQ_UI}#g" ${RocketMQWebUI_Dir}/WebUI_Address
else
    echo "##RocketMQ_WebUI" >> ${RocketMQWebUI_Dir}/WebUI_Address
    echo "RocketMQUI_Address=${RocketMQ_UI}" >> ${RocketMQWebUI_Dir}/WebUI_Address
fi
}
####################################################################
# 函数名: main
# 描述: 脚本主要业务入口
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function main ()
{
etc_profile
noslave_broker
rocketmq_webUI
}

#---------------------------------------------------------------------#
#                              执行流程                                #
#---------------------------------------------------------------------#
## 打印时间
echo "" | tee -a $LOG_FILE
echo "$(date "+%Y-%m-%d  %H:%M:%S")" | tee  -a $LOG_FILE
main