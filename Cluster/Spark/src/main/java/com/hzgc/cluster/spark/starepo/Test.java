package com.hzgc.cluster.spark.starepo;

import com.hzgc.cluster.spark.util.PropertiesUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Administrator on 2018-5-23.
 */
public class Test {
    public static void main(String[] args) {
//        String phoenixJDBCURL = PropertiesUtil.getProperties().getProperty("phoenix.jdbc.url");
//        try {
//            Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
//            Connection conn = DriverManager.getConnection("jdbc:phoenix:172.18.18.100:2181");
//            System.out.println(conn);
//        } catch (ClassNotFoundException | SQLException e) {
//            e.printStackTrace();
//        }
        System.out.println(StaticRepoUtil.getInstance());
    }
}
