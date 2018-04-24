package com.hzgc.common.service.table.column;

import org.apache.hadoop.hbase.util.Bytes;

/**
 * 聚类有关的HBase表
 */
public class ClusteringTable {

    public static final String TABLE_ClUSTERINGINFO = "clusteringInfo";              // clusteringInfo表
    public static final byte[] ClUSTERINGINFO_COLUMNFAMILY = Bytes.toBytes("c");  // clusteringInfo表列簇
    public static final byte[] ClUSTERINGINFO_COLUMN_YES = Bytes.toBytes("y");    // 聚类信息
    public static final byte[] ClUSTERINGINFO_COLUMN_NO = Bytes.toBytes("n");
    public static final String TABLE_DETAILINFO = "detailInfo";                      // detailInfo表
    public static final byte[] DETAILINFO_COLUMNFAMILY = Bytes.toBytes("c");      // detailInfo表列簇
    public static final byte[] DETAILINFO_COLUMN_DATA = Bytes.toBytes("i");       // 告警详细信息
}
