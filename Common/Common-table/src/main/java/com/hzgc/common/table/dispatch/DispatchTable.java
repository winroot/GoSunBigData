package com.hzgc.common.table.dispatch;

import java.io.Serializable;

public class DispatchTable implements Serializable {

    public final static String TABLE_DEVICE = "device";
    public final static byte[] CF_DEVICE = "device".getBytes();
    public final static byte[] WARN = "w".getBytes();
    public final static byte[] OFFLINERK = "offlineWarnRowKey".getBytes();
    public final static byte[] OFFLINECOL = "objTypes".getBytes();
    public final static byte[] COLUMN_RULE = "dispatchObj".getBytes();
    public final static byte[] RULE_ID = "ruleId".getBytes();
    public final static String IDENTIFY = "100";
    public final static String ADDED = "101";
    public final static String OFFLINE = "102";

}