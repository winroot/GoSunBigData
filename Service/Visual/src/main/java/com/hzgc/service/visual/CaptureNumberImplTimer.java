package com.hzgc.service.visual;

import com.hzgc.common.es.ElasticSearchHelper;
import com.hzgc.common.hbase.HBaseHelper;
import com.hzgc.common.table.dynrepo.DynamicTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CaptureNumberImplTimer {

    public static void main(String[] args) {
        indexTable();
    }

    private static TransportClient esClient;

    public CaptureNumberImplTimer(@Value("${es.cluster.name}") String clusterName,
                                  @Value("${es.hosts}") String esHost,
                                  @Value("${es.cluster.port}") String esPort) {
        esClient = ElasticSearchHelper.getEsClient(clusterName, esHost, Integer.parseInt(esPort));
    }

    /**
     * 将查出来的数据插入到一个新建的dynamicshow表中
     */
    private static void indexTable() {
        List<String> lists = findIpcId();
        long nowTime = System.currentTimeMillis();
        long lessOneHour = nowTime - 1000 * 60 * 60;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTimeStr = format.format(nowTime);
        String lessOneHourStr = format.format(lessOneHour);
        String endTime = nowTimeStr.split(":")[0] + ":00:00";
        String startTime = lessOneHourStr.split(":")[0] + ":00:00";
        String index = DynamicTable.DYNAMIC_INDEX;
        String type = DynamicTable.PERSON_INDEX_TYPE;
        if (lists != null && lists.size() > 0) {
            for (String list : lists) {
                //查询动态库中数据
                BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
                boolQueryBuilder.must(QueryBuilders.matchPhraseQuery(DynamicTable.IPCID, list));
                boolQueryBuilder.must(QueryBuilders.rangeQuery(DynamicTable.TIMESTAMP).gte(startTime).lte(endTime));
                SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(index)
                        .setTypes(type)
                        .setQuery(boolQueryBuilder);
                SearchResponse searchResponse = searchRequestBuilder.get();
                SearchHits searchHits = searchResponse.getHits();
                int number = (int) searchHits.getTotalHits();
                //将数据插入新表中
                Map<String, Object> map = new HashMap<>();
                map.put("ipcid", list);
                map.put("time", startTime);
                map.put("count", number);
                IndexResponse indexResponse = esClient
                        .prepareIndex("dynamicshow", "person")
                        .setSource(map)
                        .get();
                System.out.println(indexResponse.getVersion());
            }
        }
    }

    /**
     * 查询当前有多少设备
     *
     * @return 返回设备列表
     */
    private static List<String> findIpcId() {
        List<String> list = new ArrayList<>();
        Table table = HBaseHelper.getTable("device");
        Scan scan = new Scan();
        try {
            ResultScanner rs = table.getScanner(scan);
            for (Result r : rs) {
                byte[] rowkey = r.getRow();
                String rowkeyStr = new String(rowkey);
                list.add(rowkeyStr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
