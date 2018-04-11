package com.hzgc.collect.ftp.util;

import com.hzgc.common.util.file.ResourceFileUtil;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class LoggerConfig {
    private static Logger LOG = Logger.getLogger(LoggerConfig.class);

    static {
        PropertyConfigurator.configureAndWatch(
                ResourceFileUtil.loadResourceFile("log4j.properties").getAbsolutePath(), 10000);
        LOG.info("Dynamic log configuration is successful! Log configuration file refresh time:" + 10000 + "ms");
    }
}
