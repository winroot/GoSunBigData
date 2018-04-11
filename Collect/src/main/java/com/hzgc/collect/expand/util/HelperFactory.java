package com.hzgc.collect.expand.util;

import com.hzgc.common.ftp.properties.CollectProperHelper;
import com.hzgc.common.ftp.properties.FTPAddressProperHelper;
import com.hzgc.common.ftp.properties.RocketMQProperHelper;

public class HelperFactory {
	public static void regist() {
		new CollectProperHelper();
		new FTPAddressProperHelper();
		new KafkaProperHelper();
		new RocketMQProperHelper();
	}
}
