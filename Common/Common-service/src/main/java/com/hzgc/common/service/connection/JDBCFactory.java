package com.hzgc.common.service.connection;

import com.hzgc.common.util.file.ResourceFileUtil;
import com.hzgc.common.util.io.IOUtil;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * JDBC工具类 乔凯峰（内）
 */
public class JDBCFactory {
    private static Logger LOG = Logger.getLogger(JDBCFactory.class);
    private static Properties propertie = new Properties();

    /*
      加载数据源配置信息
     */
    static {
        FileInputStream fis = null;
        try {
            File resourceFile = ResourceFileUtil.loadResourceFile("common_service_jdbc.properties");
            if (resourceFile != null) {
                fis = new FileInputStream(resourceFile);
                propertie.load(new FileInputStream(resourceFile));
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.info("get jdbc.properties failure");
        } finally {
            IOUtil.closeStream(fis);
        }
    }

    /**
     * 获取数据库连接池连接
     *
     * @return 返回Connection对象
     */
    public static Connection getConnection() {
        long start = System.currentTimeMillis();
        Connection conn = null;
        try {
            Class.forName("org.apache.hive.jdbc.HiveDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            conn = DriverManager.getConnection(propertie.getProperty("url"));
        } catch (SQLException e) {
            if (e.getMessage().contains("Unable to read HiveServer2 uri from ZooKeeper")){
                LOG.error("Please start Spark JBDC Service !");
            }else {
                e.printStackTrace();
            }
        }
        LOG.info("get jdbc connection time is:" + (System.currentTimeMillis() - start));
        return conn;
    }
}
