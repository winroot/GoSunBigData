package com.hzgc.common.service.connection;

import com.hzgc.common.util.file.ResourceFileUtil;
import com.hzgc.common.util.empty.IsEmpty;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class HBaseHelper implements Serializable {

    private static Logger LOG = Logger.getLogger(HBaseHelper.class);

    private static Configuration innerHBaseConf = null;
    private static Connection innerHBaseConnection = null;

    public HBaseHelper() {
        initHBaseConfiguration();
        LOG.info("init configuration is successful");
        initHBaseConnection();
        LOG.info("init connection is successful");
    }

    /**
     * 内部方法，用来初始化HBaseConfiguration
     */
    private static void initHBaseConfiguration() {
        try {
            innerHBaseConf = HBaseConfiguration.create();
            innerHBaseConf.addResource(ResourceFileUtil.loadResourceInputStream("hbase-site.xml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeInnerHbaseConn() {
        if (innerHBaseConnection != null) {
            try {
                innerHBaseConnection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取HBaseConfiguration
     *
     * @return 返回HBaseConfiguration对象
     */
    private static Configuration getHBaseConfiguration() {
        if (null == innerHBaseConf) {
            initHBaseConfiguration();
        }
        return innerHBaseConf;
    }

    /**
     * 内部方法，用来初始化HBaseConnection
     */
    private static void initHBaseConnection() {
        try {
            innerHBaseConnection = ConnectionFactory.createConnection(HBaseHelper.getHBaseConfiguration());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取HBase连接
     *
     * @return 返回HBaseConnection对象
     */
    public static Connection getHBaseConnection() {
        if (null == innerHBaseConnection || innerHBaseConnection.isClosed()) {
            LOG.info("The HBaseConnection is null or closed, recreate connection");
            initHBaseConnection();
        }
        return innerHBaseConnection;
    }

    /**
     * 获取表对象
     *
     * @param tableName 表名称
     * @return 表对象
     */
    public static Table getTable(String tableName) {
        if (IsEmpty.strIsRight(tableName)) {
            try {
                return HBaseHelper.getHBaseConnection().getTable(TableName.valueOf(tableName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 关闭connection连接
     */
    public static void closeConn() {
        if (null != innerHBaseConnection && !innerHBaseConnection.isClosed()) {
            try {
                innerHBaseConnection.close();
                LOG.info("HBaseConnection close successfull");
            } catch (IOException e) {
                LOG.info("HBaseConnection close failed ");
                e.printStackTrace();
            }
        }
    }

    public static void closeTable(Table table) {
        if (null != table) {
            try {
                table.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
