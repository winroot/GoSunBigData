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
}
