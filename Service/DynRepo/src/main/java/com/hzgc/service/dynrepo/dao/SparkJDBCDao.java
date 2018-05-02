package com.hzgc.service.dynrepo.dao;

import com.hzgc.service.dynrepo.bean.SearchOption;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class SparkJDBCDao {
    private static Logger LOG = Logger.getLogger(SparkJDBCDao.class);
    @Value("${hive.jdbc.driver}")
    private String hiveClass;
    @Value("${hive.jdbc.url}")
    private String hiveUrl;


    public ResultSet searchPicture(SearchOption option) throws SQLException {
        Connection connection = createConnection();
        Statement statement = connection.createStatement();
        closeConnection(connection, statement);
        return statement.executeQuery(parseByOption(option));
    }

    private void closeConnection(Connection connection, Statement statement) {
        try {
            if (statement != null && !statement.isClosed()) {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private String parseByOption(SearchOption option) throws SQLException {
        LOG.info("Query sql: " + ParseByOption.getFinalSQLwithOption(option, false));
        return ParseByOption.getFinalSQLwithOption(option, true);
    }

    private Connection createConnection() throws SQLException {
        Connection connection = null;
        long start = System.currentTimeMillis();
        try {
            Class.forName(hiveClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection(hiveUrl);
        } catch (SQLException e) {
            if (e.getMessage().contains("Unable to read HiveServer2 uri from ZooKeeper")) {
                LOG.error("Please start Spark thriftserver Service !");
            } else {
                e.printStackTrace();
            }
        }
        if (connection == null) {
            LOG.error("Create spark jdbc connection faild, current hive class is [" + hiveClass + "], hive url is [" + hiveUrl + "]");
            throw new SQLException();
        }
        LOG.info("Get jdbc connection time is:" + (System.currentTimeMillis() - start));
        return connection;
    }
}
