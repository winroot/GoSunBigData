package com.hzgc.common.table.clustering;

/**
 * 聚类有关的HBase表
 */
public class ClusteringTable {

    public static final String TABLE_ClUSTERINGINFO = "clusteringInfo";              // clusteringInfo表
    public static final byte[] ClUSTERINGINFO_COLUMNFAMILY = "c".getBytes();  // clusteringInfo表列簇
    public static final byte[] ClUSTERINGINFO_COLUMN_YES = "y".getBytes();    // 聚类信息
    public static final byte[] ClUSTERINGINFO_COLUMN_NO = "n".getBytes();
    public static final String TABLE_DETAILINFO = "detailInfo";                      // detailInfo表
    public static final byte[] DETAILINFO_COLUMNFAMILY = "c".getBytes();      // detailInfo表列簇
    public static final byte[] DETAILINFO_COLUMN_DATA = "i".getBytes();       // 告警详细信息
}
