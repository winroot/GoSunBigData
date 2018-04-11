package com.hzgc.collect.expand.util.properties;

import com.hzgc.common.ftp.FTPAddressProperHelper;

public class HelperFactory {
	public static void regist() {
		new ClusterOverFtpProperHelper();
		new FTPAddressProperHelper();
		new KafkaProperHelper();
		new RocketMQProperHelper();
	}
}
