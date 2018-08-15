#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    miniaturizationYarnConfigure.sh
## Description: 修改小型化hadoop集群yarn配置
## Version:     2.4
## Author:      yinhang
## Created:     2018-06-30
################################################################################
#set -x  ## 用于调试用，不用的时候可以注释掉
#set -e

#---------------------------------------------------------------------#
#                              定义变量                                #
#---------------------------------------------------------------------#
cd `dirname $0`
## 脚本所在目录
BIN_DIR=`pwd`
cd ../..
## ClusterBuildScripts目录
CLUSTER_BUILD_SCRIPTS_DIR=`pwd`
## log 日记目录
LOG_DIR=${CLUSTER_BUILD_SCRIPTS_DIR}/logs
##log日志文件
LOG_FILE=${LOG_DIR}/miniaturizationYarnConfigure.log
## yarn 该节点上YARN可使用的物理内存总量
YARN_MAX_MEN=50790
## yarn 单个任务可申请的最少物理内存量
YARN_ASINGLETASK_MIN_MEN=1024
## yarn 单个任务可申请的最多物理内存量
YARN_ASINGLETASK_MAX_MEN=8192
## 获取当前机器core数量
CORES=$(cat /proc/cpuinfo | grep "processor"| wc -l)
## cluster_conf.properties文件目录
CONF_DIR=${CLUSTER_BUILD_SCRIPTS_DIR}/conf
## 最终安装的根目录，所有bigdata 相关的根目录
INSTALL_HOME=$(grep Install_HomeDir ${CONF_DIR}/cluster_conf.properties | cut -d '=' -f2)
## yar-site.xml目录
YARN_SITE_XML_DIR=${INSTALL_HOME}/Hadoop/hadoop/etc/hadoop
## yar-site.xml文件路径
YARN_SITE_XML=${YARN_SITE_XML_DIR}/yarn-site.xml
## <value>
VALUE="<value>"
## </value>
VALUE_END="</value>"
## 获取yarn-site.xml 分发节点
CLUSTER_HOST=$(grep Cluster_HostName ${CONF_DIR}/cluster_conf.properties | cut -d '=' -f2)
HOSTNAMES=(${CLUSTER_HOST//;/ })
SERVICE_DIR=${CLUSTER_BUILD_SCRIPTS_DIR}/service
THRIFTSERVER_FILE=${SERVICE_DIR}/thriftServerStart.sh
#####################################################################
# 函数名:config_yarn_site_xml
# 描述: 配置yarn-site.xml
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function config_yarn_site_xml()
{
echo "" | tee -a $LOG_FILE
echo "****************************************************" | tee -a $LOG_FILE
cd ${YARN_SITE_XML_DIR}
echo "进入${YARN_SITE_XML_DIR}目录，准备配置yarn-site.xml" | tee -a $LOG_FILE
if [ -f "${YARN_SITE_XML}" ]; then
    ## 配置yarn可使用的物理内存总量
    grep -q "yarn.nodemanager.resource.memory-mb" ${YARN_SITE_XML}
    if [[ $? -eq 0 ]]; then
        num1=$[ $(cat yarn-site.xml | cat -n | grep  yarn.nodemanager.resource.memory-mb | awk '{print $1}') + 1 ]
        sed -i "${num1}c ${VALUE}${YARN_MAX_MEN}${VALUE_END}" ${YARN_SITE_XML}
    else echo "yarn.nodemanager.resource.memory-mb 配置失败" | tee -a $LOG_FILE
    fi
    ## 配置yarn单个任务可申请的最少物理内存量
    grep -q "yarn.scheduler.minimum-allocation-mb" ${YARN_SITE_XML}
    if [[ $? -eq 0 ]]; then
        num2=$[ $(cat yarn-site.xml | cat -n | grep  yarn.scheduler.minimum-allocation-mb | awk '{print $1}') + 1 ]
        sed -i "${num2}c ${VALUE}${YARN_ASINGLETASK_MIN_MEN}${VALUE_END}" ${YARN_SITE_XML}
    else echo "yarn.scheduler.minimum-allocation-mb 配置失败" | tee -a $LOG_FILE
    fi
    ## 配置yarn单个任务可申请的最多物理内存量
    grep -q "yarn.scheduler.maximum-allocation-mb" ${YARN_SITE_XML}
    if [[ $? -eq 0 ]]; then
        num3=$[ $(cat yarn-site.xml | cat -n | grep  yarn.scheduler.maximum-allocation-mb | awk '{print $1}') + 1 ]
        sed -i "${num3}c ${VALUE}${YARN_ASINGLETASK_MAX_MEN}${VALUE_END}" ${YARN_SITE_XML}
    else echo "yarn.scheduler.maximum-allocation-mb 配置失败" | tee -a $LOG_FILE
    fi
    ## 配置当前机器core数量
    grep -q "yarn.nodemanager.resource.cpu-vcores" ${YARN_SITE_XML}
    if [[ $? -eq 0 ]]; then
        num4=$[ $(cat yarn-site.xml | cat -n | grep  yarn.nodemanager.resource.cpu-vcores | awk '{print $1}') + 1 ]
        sed -i "${num4}c ${VALUE}${CORES}${VALUE_END}" ${YARN_SITE_XML}
    else echo "yarn.nodemanager.resource.cpu-vcores 配置失败" | tee -a $LOG_FILE
    fi
    fi
    echo "配置yarn-site.xml完成!!!!!!" | tee -a $LOG_FILE
}

#####################################################################
# 函数名:add_two_configuration
# 描述: 是否启动一个线程检查每个任务正使用的虚拟内存量，如果任务超出分配值，则直接将其杀掉，默认是true,改成false
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function add_two_configuration ()
{
cd ${YARN_SITE_XML_DIR}
grep -q "yarn.resourcemanager.pmem-check-enabled" ${YARN_SITE_XML}
if [[ ! $? -eq 0 ]]; then
    sed -i '$i\<property>' ${YARN_SITE_XML}
    sed -i '$i\<name>yarn.resourcemanager.pmem-check-enabled</name>' ${YARN_SITE_XML}
    sed -i '$i\ <value>false</value>' ${YARN_SITE_XML}
    sed -i '$i\</property>' ${YARN_SITE_XML}
fi
grep -q "yarn.resourcemanager.vmen-check-enabled" ${YARN_SITE_XML}
if [[ ! $? -eq 0 ]]; then
    sed -i '$i\<property>' ${YARN_SITE_XML}
    sed -i '$i\ <name>yarn.resourcemanager.vmen-check-enabled</name>' ${YARN_SITE_XML}
    sed -i '$i\ <value>false</value>' ${YARN_SITE_XML}
    sed -i '$i\</property>' ${YARN_SITE_XML}
fi
echo "配置add_two_configuration完成!!!!!!" | tee -a $LOG_FILE
}

#####################################################################
# 函数名: xync_yarn_config
# 描述: yarn 配置文件分发
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function distribution_yarn_config ()
{
cd ${YARN_SITE_XML_DIR}
echo ""  | tee -a $LOG_FILE
echo "**********************************************" | tee -a $LOG_FILE
echo "yarn 配置文件分发中，please waiting......"    | tee -a $LOG_FILE
for host in ${HOSTNAMES[@]};do
        scp ${YARN_SITE_XML} ${host}:${YARN_SITE_XML}
done
}

#####################################################################
# 函数名: thriftServer_config
# 描述: 修改thriftServer配置
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function thriftServer_config ()
{
cd ${SERVICE_DIR}
echo "开始配置 thriftServer_config " | tee -a $LOG_FILE
sed -i "s#DRIVER_MEN=\${1:-\"8g\"}#DRIVER_MEN=\${1:-\"2g\"} #g" ${THRIFTSERVER_FILE}
sed -i "s#EXECUTOR_MEN=\${2:-\"4g\"}#EXECUTOR_MEN=\${2:-\"2g\"} #g" ${THRIFTSERVER_FILE}
sed -i "s#DRIVER_CORES=\${3:-\"4\"}#DRIVER_CORES=\${3:-\"1\"} #g" ${THRIFTSERVER_FILE}
sed -i "s#EXECUTOR_CORES=\${4:-\"4\"}#EXECUTOR_CORES=\${4:-\"1\"} #g" ${THRIFTSERVER_FILE}
echo "thriftServer_config 配置完毕" | tee -a $LOG_FILE
}

#####################################################################
# 函数名: main
# 描述: 脚本主要业务入口
# 参数: N/A
# 返回值: N/A
# 其他: N/A
#####################################################################
function main ()
{
config_yarn_site_xml
add_two_configuration
distribution_yarn_config
thriftServer_config
}

#---------------------------------------------------------------------#
#                              执行流程                                #
#---------------------------------------------------------------------#
## 打印时间
echo "" | tee -a $LOG_FILE
echo "$(date "+%Y-%m-%d  %H:%M:%S")" | tee  -a  $LOG_FILE
main













