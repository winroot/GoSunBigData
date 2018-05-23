package com.hzgc.cluster.spark.spark.starepo;

import com.hzgc.cluster.spark.spark.util.PropertiesUtil;

import java.sql.*;

class PhoenixJDBCUtil {

    private static Connection conn;

    private PhoenixJDBCUtil() {
    }

    static Connection getPhoenixJdbcConn() {
        if (conn == null) {
            initConnection();
        }
        return conn;
    }

    private static void initConnection() {
        String phoenixJDBCURL = PropertiesUtil.getProperties().getProperty("phoenix.jdbc.url");
        try {
            Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
            conn = DriverManager.getConnection(phoenixJDBCURL);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}