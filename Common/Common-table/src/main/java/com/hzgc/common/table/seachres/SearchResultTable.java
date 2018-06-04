package com.hzgc.common.table.seachres;

import java.io.Serializable;

public class SearchResultTable implements Serializable {
    //searchRes表
    public static final String TABLE_SEARCHRES = "searchRes";
    //searchRes表列簇
    public static final byte[] SEARCHRES_COLUMNFAMILY = "i".getBytes();
    //查询信息
    public static final byte[] SEARCHRES_COLUMN_SEARCHMESSAGE = "m".getBytes();
    //查询时间
    public static final byte[] SEARCHRES_COLUM_SEARCHTIME = "time".getBytes();
    //搜索原图
    public static final byte[] SEARCHRES_COLUM_PICTURE = "pic".getBytes();

    //以图搜图查询历史记录
    public static final byte[] STAREPO_COLUMN_SEARCHMESSAGE = "sm".getBytes();
    //静态库搜索原图
    public static final byte[] STAREPO_COLUMN_PICTURE = "spic".getBytes();
    //搜索记录导出重点人员
    public static final byte[] STAREPO_COLUMN_FILE = "word".getBytes();
}
