package com.hzgc.service.dynrepo.dao;

import com.hzgc.common.hbase.HBaseHelper;
import com.hzgc.common.table.dynrepo.DynamicTable;
import com.hzgc.common.util.object.ObjectUtil;
import com.hzgc.service.dynrepo.bean.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
@Slf4j
public class HBaseDao {
    public HBaseDao() {
        HBaseHelper.getHBaseConnection();
    }

    /**
     * 存储查询结果
     *
     * @param result 查询结果
     * @return 返回是否插入成功
     */
    public boolean insertSearchRes(SearchResult result) {
        Table searchRes = HBaseHelper.getTable(DynamicTable.TABLE_SEARCHRES);
        long start = System.currentTimeMillis();
        try {
            Put put = new Put(Bytes.toBytes(result.getSearchId()));
            put.setDurability(Durability.SYNC_WAL);
            byte[] searchMessage = ObjectUtil.objectToByte(result);
            put.addColumn(DynamicTable.SEARCHRES_COLUMNFAMILY, DynamicTable.SEARCHRES_COLUMN_SEARCHMESSAGE, searchMessage);
            if (searchRes != null) {
                searchRes.put(put);
                log.info("SaveResult time is:" + (System.currentTimeMillis() - start));
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Insert data by searchId from table_searchRes failed! used method DynamicPhotoServiceHelper.insertSearchRes.");
        } finally {
            HBaseHelper.closeTable(searchRes);
        }
        return false;
    }

    /**
     * 通过查询ID获取之前的查询结果
     *
     * @param searchId 查询ID
     * @return 返回查询结果对象
     */
    public SearchResult getSearchRes(String searchId) {
        Result result = null;
        SearchResult searchResult;
        Table searchResTable = HBaseHelper.getTable(DynamicTable.TABLE_SEARCHRES);
        Get get = new Get(Bytes.toBytes(searchId));
        try {
            if (searchResTable != null) {
                result = searchResTable.get(get);
            }
            if (result != null) {
                byte[] searchMessage = result.getValue(DynamicTable.SEARCHRES_COLUMNFAMILY, DynamicTable.SEARCHRES_COLUMN_SEARCHMESSAGE);
                searchResult = ((SearchResult) ObjectUtil.byteToObject(searchMessage));
                if (searchResult != null) {
                    searchResult.setSearchId(searchId);
                }
                return searchResult;
            } else {
                log.info("Get searchResult null from table_searchRes, search id is:" + searchId);
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.info("No result get by searchId[" + searchId + "]");
        } finally {
            HBaseHelper.closeTable(searchResTable);
        }
        return null;
    }

}
