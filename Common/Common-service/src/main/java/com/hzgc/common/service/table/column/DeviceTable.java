package com.hzgc.common.service.table.column;

import org.apache.hadoop.hbase.util.Bytes;

import java.io.Serializable;

public class DeviceTable implements Serializable {
    public final static String TABLE_DEVICE = "device";
    public final static byte[] CF_DEVICE = Bytes.toBytes("device");
    public final static byte[] PLAT_ID = Bytes.toBytes("p");
    public final static byte[] NOTES = Bytes.toBytes("n");
    public final static byte[] WARN = Bytes.toBytes("w");
    public final static byte[] OFFLINERK = Bytes.toBytes("offlineWarnRowKey");
    public final static byte[] OFFLINECOL = Bytes.toBytes("objTypes");
    public final static Integer IDENTIFY = 100;
    public final static Integer ADDED = 101;
    public final static Integer OFFLINE = 102;

}