package com.hzgc.common.service.connection;

import org.apache.log4j.Logger;

import java.sql.*;

public class PhoenixJDBCFactory {
    private Logger LOG = Logger.getLogger(PhoenixJDBCFactory.class);
    private static Connection conn;

    private PhoenixJDBCFactory() {}
    public static Connection getPhoenixJdbcConn() {
        if (conn == null) {
            initConnection();
        }
        return conn;
    }


    private static void initConnection() {
        String phoenixJDBCURL = JDBCProperties.getPhoenixJDBCURL();
        try {
            Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
            conn = DriverManager.getConnection(phoenixJDBCURL);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeConnection(Connection conn, Statement pstm) {
        closeConnection(conn, pstm, null);
    }

    public static void closeConnection(Connection conn, Statement pstm, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (pstm != null) {
                pstm.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
