package com.hzgc.service.dynrepo.dao;

import com.hzgc.common.hbase.HBaseHelper;
import com.hzgc.common.table.dynrepo.DynamicTable;
import com.hzgc.common.table.seachres.SearchResultTable;
import com.hzgc.common.util.object.ObjectUtil;
import com.hzgc.service.dynrepo.bean.SearchCollection;
import com.hzgc.service.dynrepo.bean.SearchHisotry;
import com.hzgc.service.dynrepo.bean.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Repository
@Slf4j
public class HBaseDao {
    public HBaseDao() {
        HBaseHelper.getHBaseConnection();
    }

    /**
     * 存储查询结果
     *
     * @param collection 查询集合
     * @return 返回是否插入成功
     */
    public boolean insertSearchRes(SearchCollection collection) {
        Table searchRes = HBaseHelper.getTable(SearchResultTable.TABLE_SEARCHRES);
        List<Put> putList = new ArrayList<>();
        long start = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat(EsSearchParam.TIMEFORMAT_YMDHMS);
        String searchTime = dateFormat.format(new Date());
        try {
            Put put = new Put(Bytes.toBytes(collection.getSearchResult().getSearchId()));
            put.setDurability(Durability.SYNC_WAL);
            byte[] searchMessage = ObjectUtil.objectToByte(collection);
            put.addColumn(SearchResultTable.SEARCHRES_COLUMNFAMILY,
                    SearchResultTable.SEARCHRES_COLUMN_SEARCHMESSAGE, searchMessage);
            put.addColumn(SearchResultTable.SEARCHRES_COLUMNFAMILY,
                    SearchResultTable.SEARCHRES_COLUM_SEARCHTIME, Bytes.toBytes(searchTime));
            putList.add(put);
            for (int i = 0; i < collection.getSearchOption().getImages().size(); i++) {
                put.addColumn(SearchResultTable.SEARCHRES_COLUMNFAMILY,
                        Bytes.toBytes(i+ ""), collection.getSearchOption().getImages().get(i).getImageData());
                Put putimage = new Put(Bytes.toBytes(collection.getSearchOption().getImages().get(i).getImageID()));
                putimage.addColumn(SearchResultTable.SEARCHRES_COLUMNFAMILY,
                        SearchResultTable.SEARCHRES_COLUM_PICTURE,
                        collection.getSearchOption().getImages().get(i).getImageData());
                putList.add(putimage);
            }
            if (searchRes != null) {
                searchRes.put(putList);
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
        SearchCollection searchCollection;
        SearchResult searchResult;
        Table searchResTable = HBaseHelper.getTable(SearchResultTable.TABLE_SEARCHRES);
        Get get = new Get(Bytes.toBytes(searchId));
        get.addColumn(SearchResultTable.SEARCHRES_COLUMNFAMILY,
                SearchResultTable.SEARCHRES_COLUMN_SEARCHMESSAGE);
        try {
            if (searchResTable != null) {
                result = searchResTable.get(get);
            }
            if (result != null) {
                byte[] searchMessage =
                        result.getValue(SearchResultTable.SEARCHRES_COLUMNFAMILY,
                                SearchResultTable.SEARCHRES_COLUMN_SEARCHMESSAGE);
                searchCollection = ((SearchCollection) ObjectUtil.byteToObject(searchMessage));
                searchResult = searchCollection.getSearchResult();
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

    /**
     * 获取搜索原图
     *
     * @param image_name 原图ID
     * @return 图片二进制
     */
    public byte[] getSearchPicture(String image_name) {
        Table table = HBaseHelper.getTable(image_name);
        Get get = new Get(Bytes.toBytes(image_name));
        get.addColumn(SearchResultTable.SEARCHRES_COLUMNFAMILY, SearchResultTable.SEARCHRES_COLUM_PICTURE);
        try {
            Result picBin = table.get(get);
            return picBin.getValue(SearchResultTable.SEARCHRES_COLUMNFAMILY, SearchResultTable.SEARCHRES_COLUM_PICTURE);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            HBaseHelper.closeTable(table);
        }
        return null;
    }

    /**
     * 查询搜索记录
     *
     * @param start_time 历史记录起始时间
     * @param end_time   历史记录结束时间
     * @param sort       排序参数
     * @param start      起始位置
     * @param limit      返回条数
     * @return 返回查询结果
     */
    public List<SearchHisotry> getSearchHistory(String start_time, String end_time, String sort, int start, int limit) {
        FilterList filterList = new FilterList();
        Filter stimeFilter = new SingleColumnValueFilter(SearchResultTable.SEARCHRES_COLUMNFAMILY,
                SearchResultTable.SEARCHRES_COLUM_SEARCHTIME,
                CompareFilter.CompareOp.GREATER_OR_EQUAL,
                Bytes.toBytes(start_time));
        Filter etimeFilter = new SingleColumnValueFilter(SearchResultTable.SEARCHRES_COLUMNFAMILY,
                SearchResultTable.SEARCHRES_COLUM_SEARCHTIME,
                CompareFilter.CompareOp.LESS_OR_EQUAL,
                Bytes.toBytes(end_time));
        filterList.addFilter(stimeFilter);
        filterList.addFilter(etimeFilter);
        Scan scan = new Scan();
        scan.setFilter(filterList);
        scan.addColumn(SearchResultTable.SEARCHRES_COLUMNFAMILY, SearchResultTable.SEARCHRES_COLUM_SEARCHTIME);
        scan.addColumn(SearchResultTable.SEARCHRES_COLUMNFAMILY, SearchResultTable.SEARCHRES_COLUMN_SEARCHMESSAGE);
        Table table = HBaseHelper.getTable(SearchResultTable.TABLE_SEARCHRES);
        List<SearchHisotry> searchHisotryList = new ArrayList<>();
        try {
            if (table != null) {
                ResultScanner results = table.getScanner(scan);
                for (Result r : results) {
                    SearchHisotry searchHisotry = new SearchHisotry();
                    for (Cell cell : r.listCells()) {
                        String qualifier = new String(CellUtil.cloneQualifier(cell));
                        if (qualifier.equals(new String(SearchResultTable.SEARCHRES_COLUM_SEARCHTIME))) {
                            searchHisotry.setSearchTime(new String(CellUtil.cloneValue(cell)));
                        } else if (qualifier.equals(new String(SearchResultTable.SEARCHRES_COLUMN_SEARCHMESSAGE))) {
                            SearchCollection collection =
                                    (SearchCollection) ObjectUtil.byteToObject(CellUtil.cloneValue(cell));
                            searchHisotry.setAttributes(collection.getSearchOption().getAttributes());
                            searchHisotry.setDeviceIds(collection.getSearchOption().getDeviceIpcs());
                            searchHisotry.setSearchId(new String(CellUtil.cloneRow(cell)));
                            searchHisotry.setSimilarity(collection.getSearchOption().getSimilarity() + "");
                            searchHisotry.setStartTime(collection.getSearchOption().getStartTime());
                            searchHisotry.setEndTime(collection.getSearchOption().getEndTime());
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            HBaseHelper.closeTable(table);
        }
        if (searchHisotryList.size() > 1) {
            searchHisotryList.sort((o1, o2) -> {
                if (o1 == null || o2 == null) {
                    return 0;
                }
                if (o1.getSearchTime() == null || o2.getSearchTime() == null) {
                    return 0;
                }
                return o2.getSearchTime().compareTo(o1.getSearchTime());
            });
        }
        return searchHisotryList;
    }
}
