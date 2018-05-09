package com.hzgc.service.visual.dao;

import com.hzgc.common.attribute.bean.Attribute;
import com.hzgc.common.service.connection.ElasticSearchHelper;
import com.hzgc.common.service.table.column.DynamicShowTable;
import com.hzgc.common.service.table.column.DynamicTable;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.List;

@Repository
public class ElasticSearchDao {

    private static Logger LOG = Logger.getLogger(ElasticSearchDao.class);

    private TransportClient esClient;

    public ElasticSearchDao(@Value("${es.cluster.name}") String clusterName,
                            @Value("${es.hosts}") String esHost,
                            @Value("${es.cluster.port}") String esPort) {
        this.esClient = ElasticSearchHelper.getEsClient(clusterName, esHost, Integer.parseInt(esPort));
    }

    public synchronized SearchResponse[] dynamicNumberService(List<String> ipcId) {
        String index = DynamicTable.DYNAMIC_INDEX;
        String type = DynamicTable.PERSON_INDEX_TYPE;
        BoolQueryBuilder totalBQ = QueryBuilders.boolQuery();
        SearchResponse[] responsesArray = new SearchResponse[2];
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long a = System.currentTimeMillis();
        String endTime = format.format(a);
        String startTime = endTime.substring(0, endTime.indexOf(" ")) + " 00:00:00";
        totalBQ.must(QueryBuilders.rangeQuery(DynamicTable.TIMESTAMP).gte(startTime).lte(endTime));
        BoolQueryBuilder ipcBQ = QueryBuilders.boolQuery();
        if (ipcId != null) {
            for (String ipcid : ipcId) {
                ipcBQ.should(QueryBuilders.matchPhraseQuery(DynamicTable.IPCID, ipcid));
            }
            totalBQ.must(ipcBQ);
        }
        TransportClient client = ElasticSearchHelper.getEsClient();
        SearchResponse searchResponse0 = client.prepareSearch(index)
                .setTypes(type)
                .setSize(1)
                .get();
        responsesArray[0] = searchResponse0;
        SearchResponse searchResponse1 = client.prepareSearch(index)
                .setTypes(type)
                .setQuery(totalBQ)
                .setSize(1)
                .get();
        responsesArray[1] = searchResponse1;
        return responsesArray;
    }

    public synchronized SearchResponse timeSoltNumber(List<String> ipcids, String startTime, String endTime) {
        BoolQueryBuilder totolQuery = QueryBuilders.boolQuery();
        if (ipcids != null && ipcids.size() > 0) {
            for (String ipcid : ipcids) {
                totolQuery.should(QueryBuilders.matchPhraseQuery(DynamicTable.IPCID, ipcid));
            }
        }
        BoolQueryBuilder timeQuery = QueryBuilders.boolQuery();
        timeQuery.must(QueryBuilders.rangeQuery("time").gte(startTime).lte(endTime));
        timeQuery.must(totolQuery);
        return esClient.prepareSearch(DynamicShowTable.INDEX)
                .setTypes(DynamicShowTable.TYPE)
                .setQuery(timeQuery)
                .setSize(100000000)
                .get();
    }

    public SearchResponse captureCountQuery(String startTime, String endTime, String ipcId) {
        // 最终封装成的boolQueryBuilder 对象。
        BoolQueryBuilder totalBQ = QueryBuilders.boolQuery();
        // 设备ID 的的boolQueryBuilder
        BoolQueryBuilder ipcIdBQ = QueryBuilders.boolQuery();
        //设备ID存在时的查询条件
        if (ipcId != null && !ipcId.equals("")) {
            ipcIdBQ.must(QueryBuilders.matchPhraseQuery(DynamicTable.IPCID, ipcId)); //暂支持只传一个设备
            totalBQ.must(ipcIdBQ);
        }

        // 开始时间 的boolQueryBuilder
        BoolQueryBuilder startTimeBQ = QueryBuilders.boolQuery();
        //开始时间 存在时的查询条件
        if (null != startTime && !startTime.equals("")) {
            startTimeBQ.must(QueryBuilders.rangeQuery(DynamicTable.TIMESTAMP).gte(startTime));//gte: >= 大于或等于
            totalBQ.must(startTimeBQ);
        }
        // 结束时间 的boolQueryBuilder
        BoolQueryBuilder endTimeBQ = QueryBuilders.boolQuery();
        // 结束时间 存在时的查询条件
        if (null != endTime && !endTime.equals("")) {
            startTimeBQ.must(QueryBuilders.rangeQuery(DynamicTable.TIMESTAMP).lte(endTime)); //lte: <= 小于或等于
            totalBQ.must(endTimeBQ);
        }
        //所有判断结束后
        return esClient.prepareSearch(DynamicTable.DYNAMIC_INDEX) //指定要查询的索引名称
                .setTypes(DynamicTable.PERSON_INDEX_TYPE) //指定要查询的类型名称
                .setQuery(totalBQ) //根据查询条件qb设置查询
                .addSort(DynamicTable.TIMESTAMP, SortOrder.DESC) //以时间字段降序排序
                .get();
    }

    public SearchResponse getCaptureCount(String startTime, String endTime, List<String> ipcId) {
        BoolQueryBuilder totalBQ = QueryBuilders.boolQuery();
        // 开始时间和结束时间存在的时候的处理
        if (startTime != null && endTime != null && !startTime.equals("") && !endTime.equals("")) {
            totalBQ.must(QueryBuilders.rangeQuery(DynamicTable.TIMESTAMP).gte(startTime).lte(endTime));
        }
        BoolQueryBuilder ipcIdBQ = QueryBuilders.boolQuery();
        if (ipcId != null && ipcId.size() > 0) {
            for (String ipcid : ipcId) {
                ipcIdBQ.should(QueryBuilders.matchPhraseQuery(DynamicTable.IPCID, ipcid).analyzer("standard"));
            }
        }
        totalBQ.must(ipcIdBQ);
        return esClient.prepareSearch(DynamicTable.DYNAMIC_INDEX)
                .setTypes(DynamicTable.PERSON_INDEX_TYPE)
                .setQuery(totalBQ)
                .get();
    }

    public SearchResponse captureAttributeQuery(String startTime,
                                                String endTime,
                                                String ipcId,
                                                Attribute attribute,
                                                Integer attributeValue) {
        BoolQueryBuilder filterIpcId = QueryBuilders.boolQuery();
        filterIpcId.must(QueryBuilders.matchPhraseQuery(DynamicTable.IPCID, ipcId));
        filterIpcId.must(QueryBuilders.rangeQuery(DynamicTable.TIMESTAMP).gt(startTime).lt(endTime));
        filterIpcId.must(QueryBuilders.matchPhraseQuery(attribute.getIdentify().toLowerCase(), attributeValue));
        return esClient.prepareSearch(DynamicTable.DYNAMIC_INDEX)
                .setTypes(DynamicTable.PERSON_INDEX_TYPE)
                .setQuery(filterIpcId).get();
    }

}
