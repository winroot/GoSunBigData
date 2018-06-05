package com.hzgc.common.table.device;

import java.io.Serializable;

public class DeviceTable implements Serializable {

    public final static String TABLE_DEVICE = "deviceTest";
    public final static byte[] CF_DEVICE = "device".getBytes();
    public final static byte[] PLAT_ID = "p".getBytes();
    public final static byte[] NOTES = "n".getBytes();
    public final static byte[] WARN = "w".getBytes();
    public final static byte[] OFFLINERK = "offlineWarnRowKey".getBytes();
    public final static byte[] OFFLINECOL = "objTypes".getBytes();
    public final static Integer IDENTIFY = 100;
    public final static Integer ADDED = 101;
    public final static Integer OFFLINE = 102;

}