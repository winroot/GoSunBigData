#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    envInstall_local.sh
## Description: 安装组件的脚本
## Version:     2.0
## Author:      yinhang
## Created:     2018-07-30
################################################################################
#set -x

cd `dirname $0`
## BIN目录，脚本所在的目录
BIN_DIR=`pwd`
cd ../env

## 安装sshpass
sh sshpassInstall_local.sh

## 安装dos2unix
sh dos2unixInstall_local.sh

## 安装expect
sh expectInstall_local.sh

## 配置免密登录
sh sshSilentLogin_local.sh

## 删除环境变量
sh delete_env_variable_local.sh

## 关闭防火墙
sh offIptables_local.sh

## 创建log目录
sh logconfig.sh

