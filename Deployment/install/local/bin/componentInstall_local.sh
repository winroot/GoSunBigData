#!/bin/bash
################################################################################
## Copyright:   HZGOSUN Tech. Co, BigData
## Filename:    componentInstall_local.sh
## Description: 安装环境的脚本.
## Version:     2.0
## Author:      yinhang
## Created:     2018-07-30
################################################################################
#set -x

cd `dirname $0`
## BIN目录，脚本所在的目录
BIN_DIR=`pwd`
cd ../install

## 安装 jdk
sh jdk_local.sh

## 安装 mysql
sh mysql_local.sh

## 安装 zookeeper
sh zookeeper_local.sh

## 安装 hbase
sh hbase_local.sh

## 安装 kafka
sh kafka_local.sh

## 安装 es
sh elasticSearch_local.sh

## 安装 rocketmq
sh rocketmq_local.sh

## 安装 spark
sh spark_local.sh

## 安装 scala
sh scala_local.sh

## 安装 azkaban
sh azkaban_local.sh

## 安装 phoenix
sh phoenix_local.sh

## 安装 kibana
sh kinbana_local.sh

