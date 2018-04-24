#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    expectInstall.sh
## Description: 安装 expect 工具，安装后可以用expect命令减少人与linux之间的交互
##              实现自动化的脚本
##              关联脚本：./tool/expect.xml
## Version:     1.0
## Author:      lidiliang
## Editor:      mashencai
## Created:     2017-10-23
################################################################################

#set -x

cd `dirname $0`
## 脚本所在目录
BIN_DIR=`pwd`
cd ..
## 安装包根目录
ROOT_HOME=`pwd`
## 工具类脚本目录
TOOL_DIR=${ROOT_HOME}/tool
## 配置文件目录
CONF_DIR=${ROOT_HOME}/conf
## 安装日记目录
LOG_DIR=${ROOT_HOME}/logs
## 安装日记目录
LOG_FILE=${LOG_DIR}/expectInstall.log
## expect rpm 软件目录
EXPECT_RPM_DIR=${ROOT_HOME}/component/basic_suports/expectRpm
## 基础工具安装路径
INSTALL_HOME_BASIC=$(grep System_SuportDir ${CONF_DIR}/cluster_conf.properties|cut -d '=' -f2)
## expect rpm 软件最终目录
EXPECT_RPM_INSTALL_HOME=${INSTALL_HOME_BASIC}/expectRpm
## expect的安装节点，集群所有主机名，放入数组中
CLUSTER_HOSTNAME_LISTS=$(grep Cluster_HostName ${CONF_DIR}/cluster_conf.properties|cut -d '=' -f2)
CLUSTER_HOSTNAME_ARRY=(${CLUSTER_HOSTNAME_LISTS//;/ })
## 系统root 用密码
export PASSWORD=$(grep SSH_Password ${CONF_DIR}/cluster_conf.properties|cut -d '=' -f2)


if [ ! -d $LOG_DIR ];then
    mkdir -p $LOG_DIR;
fi

echo ""  | tee  -a  $LOG_FILE
echo ""  | tee  -a  $LOG_FILE
echo "==================================================="  | tee -a $LOG_FILE
echo "$(date "+%Y-%m-%d  %H:%M:%S")"                       | tee  -a  $LOG_FILE

for name in ${CLUSTER_HOSTNAME_ARRY[@]}
do
    echo ""  | tee -a $LOG_FILE
    echo "**********************************************" | tee -a $LOG_FILE
    echo "intall expect in  ${name}...... "  | tee -a $LOG_FILE
	echo "**********************************************" | tee -a $LOG_FILE
    ant -f ${TOOL_DIR}/expect.xml -Dhost=${name} -Dpassword=${PASSWORD} -Dexpect_rpm_install_home=${EXPECT_RPM_INSTALL_HOME} ssh-mkdir
    ant -f ${TOOL_DIR}/expect.xml -Dhost=${name} -Dpassword=${PASSWORD} -Dexpect_rpm_install_home=${EXPECT_RPM_INSTALL_HOME} -Dexpect_rpm_dir=${EXPECT_RPM_DIR} scp-tar
    if [ $? == 0 ];then
        echo "scp expect to the ${EXPECT_RPM_INSTALL_HOME} done !!!"  | tee -a $LOG_FILE
    else 
        echo "scp expect to the ${EXPECT_RPM_INSTALL_HOME} failed !!!"  | tee -a $LOG_FILE
    fi
    ant -f ${TOOL_DIR}/expect.xml -Dhost=${name} -Dpassword=${PASSWORD} -Dexpect_rpm_install_home=${EXPECT_RPM_INSTALL_HOME} -Dinstall_home_basic=${INSTALL_HOME_BASIC} ssh-install 
    echo ""  | tee -a $LOG_FILE

done




set +x
