package com.hzgc.service.starepo.dao;

import com.hzgc.common.facestarepo.table.table.SearchResultTable;
import com.hzgc.common.hbase.HBaseHelper;
import com.hzgc.jni.PictureData;
import com.hzgc.common.util.json.JSONUtil;
import com.hzgc.common.util.object.ObjectUtil;
import com.hzgc.service.starepo.bean.export.ObjectSearchResult;
import com.hzgc.service.starepo.bean.param.GetObjectInfoParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 添加 or 查询：搜索原图、搜索结果图片、导出重点人员文档、搜索历史记录
 */
@Repository
@Slf4j
public class HBaseDao {

    private String tableIsNull = "Table : " + SearchResultTable.TABLE_SEARCHRES + "is null!";
    private String resultIsNull = "Get Result is null from " + SearchResultTable.TABLE_SEARCHRES + ", rowKey is: ";
    private String insertFailed = "Insert data from " + SearchResultTable.TABLE_SEARCHRES + " failed! rowKey: ";
    private String getFailed = "Get data from " + SearchResultTable.TABLE_SEARCHRES + " failed! rowKey: ";

    public HBaseDao() {
        HBaseHelper.getHBaseConnection();
    }

    /**
     * 保存重点人员Word
     */
    public void insert_word(String rowKey, byte[] bytes) {
        Table table = HBaseHelper.getTable(SearchResultTable.TABLE_SEARCHRES);
        if (table == null) {
            log.error(tableIsNull);
            return;
        }
        try {
            Put put = new Put(Bytes.toBytes(rowKey));
            if (bytes != null) {
                put.addColumn(SearchResultTable.SEARCHRES_COLUMNFAMILY, SearchResultTable.STAREPO_COLUMN_FILE, bytes);
                table.put(put);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.info(insertFailed + rowKey);
        } finally {
            HBaseHelper.closeTable(table);
        }
    }

    public byte[] get(String rowKey, byte[] column) {
        Table table = HBaseHelper.getTable(SearchResultTable.TABLE_SEARCHRES);
        if (table == null) {
            log.error(tableIsNull);
            return null;
        }
        Get get = new Get(Bytes.toBytes(rowKey));
        try {
            Result result = table.get(get);
            if (result != null) {
                return result.getValue(SearchResultTable.SEARCHRES_COLUMNFAMILY, column);
            } else {
                log.info(resultIsNull + rowKey);
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.info(getFailed + rowKey);
        } finally {
            HBaseHelper.closeTable(table);
        }
        return null;
    }

    /**
     * 导出重点人员Word
     */
    public <T> T get_word(String rowKey, Class<T> clz) {
        Table table = HBaseHelper.getTable(SearchResultTable.TABLE_SEARCHRES);
        if (table == null) {
            log.error(tableIsNull);
            return null;
        }
        Get get = new Get(Bytes.toBytes(rowKey));
        Result result = null;
        try {
            result = table.get(get);
            if (result != null) {
                byte[] bytes = result.getValue(SearchResultTable.SEARCHRES_COLUMNFAMILY, SearchResultTable.STAREPO_COLUMN_FILE);
                T data = ObjectUtil.byteToObject(bytes, clz);
                if (data != null) {
                    return data;
                }
            } else {
                log.info(resultIsNull + rowKey);
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.info(getFailed + rowKey);
        } finally {
            HBaseHelper.closeTable(table);
        }
        return null;
    }

    public void saveSearchRecord(GetObjectInfoParam param, ObjectSearchResult objectSearchResult) {
        Table table = HBaseHelper.getTable(SearchResultTable.TABLE_SEARCHRES);
        List<Put> putList = new ArrayList<>();
        Put resultPut = new Put(Bytes.toBytes(objectSearchResult.getSearchId()));
        resultPut.addColumn(SearchResultTable.SEARCHRES_COLUMNFAMILY,
                SearchResultTable.STAREPO_COLUMN_SEARCHMESSAGE,
                Bytes.toBytes(JSONUtil.toJson(objectSearchResult)));
        putList.add(resultPut);
        if (param.getPictureDataList() != null && param.getPictureDataList().size() > 0) {
            for (PictureData pictureData : param.getPictureDataList()) {
                Put imagePut = new Put(Bytes.toBytes(pictureData.getImageID()));
                imagePut.addColumn(SearchResultTable.SEARCHRES_COLUMNFAMILY,
                        SearchResultTable.STAREPO_COLUMN_PICTURE,
                        pictureData.getImageData());
                putList.add(imagePut);
            }
        }
        try {
            table.put(putList);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            HBaseHelper.closeTable(table);
        }
    }
}
