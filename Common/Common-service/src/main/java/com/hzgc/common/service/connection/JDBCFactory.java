package com.hzgc.common.service.connection;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * JDBC工具类 乔凯峰（内）
 */
public class JDBCFactory {
    private static Logger LOG = Logger.getLogger(JDBCFactory.class);

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
            conn = DriverManager.getConnection(JDBCProperties.getHiveJDBCURL());
        } catch (SQLException e) {
            if (e.getMessage().contains("Unable to read HiveServer2 uri from ZooKeeper")) {
                LOG.error("Please start Spark JBDC Service !");
            } else {
                e.printStackTrace();
            }
        }
        LOG.info("get jdbc connection time is:" + (System.currentTimeMillis() - start));
        return conn;
    }
}
