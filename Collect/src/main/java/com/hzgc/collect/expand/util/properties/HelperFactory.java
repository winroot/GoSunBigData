package com.hzgc.collect.expand.util.properties;

public class HelperFactory {
	public static void regist() {
		new ClusterOverFtpProperHelper();
		new FTPAddressProperHelper();
		new KafkaProperHelper();
		new RocketMQProperHelper();
	}
}
