#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    expand_rocketmq.sh
## Description: rocketmq扩展安装
## Version:     2.4
## Author:      yinhang
## Created:     2018-07-14
################################################################################
## set -x  ## 用于调试用，不用的时候可以注释掉
#set -e
#---------------------------------------------------------------------#
#                              定义变量                                #
#---------------------------------------------------------------------#
cd `dirname $0`
## 脚本所在目录
BIN_DIR=`pwd`
cd ../..
## ClusterBuildScripts 目录
CLUSTER_BUILD_SCRIPTS_DIR=`pwd`
## expand conf 配置文件目录
CONF_DIR=${CLUSTER_BUILD_SCRIPTS_DIR}/expand/conf
## 安装日志目录
LOG_DIR=${CLUSTER_BUILD_SCRIPTS_DIR}/logs
## 安装日记目录
LOG_FILE=${LOG_DIR}/expand_rocketmq.log
## 最终安装的根目录，所有bigdata 相关的根目录
INSTALL_HOME=$(grep Install_HomeDir ${CONF_DIR}/expand_conf.properties|cut -d '=' -f2)
## ROCKETMQ_INSTALL_HOME rocketmq 安装目录
ROCKETMQ_INSTALL_HOME=${INSTALL_HOME}/RocketMQ
## ROCKETMQ_HOME  rocketmq 根目录
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
ROCKETMQ_LOG=$(grep Cluster_LOGSDir ${CLUSTER_BUILD_SCRIPTS_DIR}/conf/cluster_conf.properties|cut -d '=' -f2)
ROCKETMQ_LOG_PATH=${ROCKETMQ_LOG}/rocketmq
ROCKETMQ_HOSTNAME_ARRY=(${ROCKETMQ_HOSTNAME_LISTS//;/ })
## 集群新增节点主机名，放入数组中
CLUSTER_HOST=$(grep Node_HostName ${CONF_DIR}/expand_conf.properties | cut -d '=' -f2)
echo "读取的新增集群节点IP为："${CLUSTER_HOST} | tee -a $LOG_FILE
HOSTNAMES=(${CLUSTER_HOST//;/ })

## NameServer 节点IP
NameServer_Host=$(grep RocketMQ_Namesrv ${CLUSTER_BUILD_SCRIPTS_DIR}/conf/cluster_conf.properties|cut -d '=' -f2)
Broker_Hosts=$(grep RocketMQ_Broker ${CLUSTER_BUILD_SCRIPTS_DIR}/conf/cluster_conf.properties|cut -d '=' -f2)
Broker_Hostarr=(${Broker_Hosts//;/ })
NameServer_IP=$(cat /etc/hosts|grep "$NameServer_Host" | awk '{print $1}')


echo "-------------------------------------" | tee  -a  $LOG_FILE
echo " 准备进行 rocketmq 扩展安装操作 ing~" | tee  -a  $LOG_FILE
echo "-------------------------------------" | tee  -a  $LOG_FILE

Host_Arr=(${Broker_Hostarr[*]} ${NameServer_Host})

if [ ! -d ${LOG_ROCKETMQ_INSTALL_HOMEDIR} ];then
    mkdir -p ${LOG_ROCKETMQ_INSTALL_HOMEDIR};
fi
if [ ! -d ${LOG_DIR} ];then
    mkdir -p ${LOG_DIR};
fi
if [ ! -d ${ROCKETMQ_LOG} ];then
    mkdir -p ${ROCKETMQ_LOG};
fi

echo "" | tee  -a  $LOG_FILE
echo "" | tee  -a  $LOG_FILE
echo "===================================================" | tee -a $LOG_FILE
echo "$(date "+%Y-%m-%d  %H:%M:%S")" | tee -a $LOG_FILE

#####################################################################
# 函数名:rocketmq_distribution
# 描述: 分发新增节点 rocketmq
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function rocketmq_distribution ()
{
for insName in ${HOSTNAMES[@]}
do
    echo ""  | tee  -a  $LOG_FILE
    echo "************************************************"
    echo "准备将ROCKETMQ分发到节点$insName："  | tee -a $LOG_FILE
    echo "rocketmq 分发中,请稍候......"  | tee -a $LOG_FILE
    ssh ${insName} "rm -rf ${ROCKETMQ_HOME}/conf/2m-noslave/*.properties"
    ssh root@${insName} "mkdir -p ${ROCKETMQ_LOG_PATH};chmod -R 777 ${ROCKETMQ_LOG_PATH}"
    scp -r ${ROCKETMQ_INSTALL_HOME} root@${insName}:${INSTALL_HOME} > /dev/null
    echo "rocketmq 分发完毕......"  | tee -a $LOG_FILE
done
}

#####################################################################
# 函数名:etc_profile
# 描述: 判断 /etc/profile 文件是否存在export NAMESRV_ADDR=172.18.18.108:9876这一行，若存在则替换，若不存在则追加
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function etc_profile ()
{
for insName in ${HOSTNAMES[@]}
do
    namesrv_exists=$(ssh root@${insName} 'grep "export NAMESRV_ADDR=" /etc/profile')
    if [ "${namesrv_exists}" != "" ];then
        ssh root@${insName} "sed -i 's#^export NAMESRV_ADDR=.*#export NAMESRV_ADDR="${NameServer_IP}:9876"#g' /etc/profile"
    else
        ssh root@${insName} "echo export NAMESRV_ADDR="${NameServer_IP}:9876"  >> /etc/profile; echo "">> /etc/profile"
    fi
done
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
for insName in ${HOSTNAMES[@]}
do
        echo "************************************************"
        echo "准备修改${hostname}节点下的broker配置文件：" | tee -a $LOG_FILE
        ## 修改 $hostname 节点下的 broker 配置文件名字
        ssh root@${insName} "mv ${ROCKETMQ_HOME}/conf/2m-noslave/*.properties ${ROCKETMQ_HOME}/conf/2m-noslave/broker-${insName}.properties"
        ## 修改 broker 配置文件中的 brokername
        ssh root@${insName} "sed -i 's#^brokerName=.*#brokerName="broker-${insName}"#g' ${ROCKETMQ_HOME}/conf/2m-noslave/broker-${insName}.properties"

        ## 修改 storePathRootDir
        ssh root@${insName} "sed -i 's#^storePathRootDir=.*#storePathRootDir=${ROCKETMQ_STORE}#g' ${ROCKETMQ_HOME}/conf/2m-noslave/broker-${insName}.properties"
        flag1=$?

        ## 修改 storePathCommitLog
        ssh root@${insName} "sed -i 's#^storePathCommitLog=.*#storePathCommitLog=${ROCKETMQ_COMMITLOG}#g' ${ROCKETMQ_HOME}/conf/2m-noslave/broker-${insName}.properties"
        flag2=$?

        ## 修改storePathConsumeQueue
        ssh root@${insName} "sed -i 's#^storePathConsumeQueue=.*#storePathConsumeQueue=${ROCKETMQ_CONSUMEQUE}#g' ${ROCKETMQ_HOME}/conf/2m-noslave/broker-${insName}.properties"
        flag3=$?

		## 修改storePathIndex
        ssh root@${insName} "sed -i 's#^storePathIndex=.*#storePathIndex=${ROCKETMQ_INDEX}#g' ${ROCKETMQ_HOME}/conf/2m-noslave/broker-${insName}.properties"
        flag4=$?

		## 修改storeCheckpoint
        ssh root@${insName} "sed -i 's#^storeCheckpoint=.*#storeCheckpoint=${ROCKETMQ_CHECKPOINT}#g' ${ROCKETMQ_HOME}/conf/2m-noslave/broker-${insName}.properties"
        flag5=$?

		## 修改abortFile
        ssh root@${insName} "sed -i 's#^abortFile=.*#abortFile=${ROCKETMQ_ABORT}#g' ${ROCKETMQ_HOME}/conf/2m-noslave/broker-${insName}.properties"
        flag6=$?

        ssh root@${insName} "sed -i 's#\${user.home}/logs#${ROCKETMQ_LOG}\/rocketmq#g' ${ROCKETMQ_HOME}/conf/*.xml"
        flag7=$?

        ## 添加brokerIP1
		ssh root@${insName} "sed -i 's#brokerIP1=.*#brokerIP1=${insName}#g' ${ROCKETMQ_HOME}/conf/2m-noslave/broker-${insName}.properties"
		flag8=$?

		if [[ ($flag1 == 0) && ($flag2 == 0) && ($flag3 == 0) && ($flag4 == 0) && ($flag5 == 0) && ($flag6 == 0) && ($flag7 == 0) && ($flag8 == 0) ]];then
            echo " 配置brokerproperties完成." | tee -a $LOG_FILE
        else
            echo "配置brokerproperties失败." | tee -a $LOG_FILE
        fi
    echo "修改${insName}节点下的broker配置文件完成" | tee -a $LOG_FILE
done
}
# 函数名: main
# 描述: 脚本主要业务入口
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function main ()
{
rocketmq_distribution
etc_profile
noslave_broker
}

#---------------------------------------------------------------------#
#                              执行流程                                #
#---------------------------------------------------------------------#
## 打印时间
echo "" | tee -a $LOG_FILE
echo "$(date "+%Y-%m-%d  %H:%M:%S")" | tee  -a $LOG_FILE
main

echo "-------------------------------------" | tee  -a  $LOG_FILE
echo " rocketmq 扩展安装操作完成 zzZ~" | tee  -a  $LOG_FILE
echo "-------------------------------------" | tee  -a  $LOG_FILE
