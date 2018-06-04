package com.hzgc.service.visual.dao;

import com.hzgc.common.attribute.bean.Attribute;
import com.hzgc.common.es.ElasticSearchHelper;
import com.hzgc.common.table.dynrepo.DynamicShowTable;
import com.hzgc.common.table.dynrepo.DynamicTable;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.SumAggregationBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

@Repository
public class ElasticSearchDao {

    private TransportClient esClient;

    public ElasticSearchDao(@Value("${es.cluster.name}") String clusterName,
                            @Value("${es.hosts}") String esHost,
                            @Value("${es.cluster.port}") String esPort) {
        this.esClient = ElasticSearchHelper.getEsClient(clusterName, esHost, Integer.parseInt(esPort));
    }

    public synchronized SearchResponse[] dynamicNumberService(List<String> ipcId) {
        String index = DynamicTable.DYNAMIC_INDEX;
        String type = DynamicTable.PERSON_INDEX_TYPE;
        SearchResponse[] responsesArray = new SearchResponse[2];
        // 统计所有抓拍总数
        SearchResponse responseV1 = esClient.prepareSearch(index).setTypes(type)
                .setQuery(QueryBuilders.matchAllQuery()).setSize(1).get();

        // 查询今天抓拍的数量
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String endTime = format.format(System.currentTimeMillis());
        SearchResponse responseV2 = esClient.prepareSearch(index).setQuery(QueryBuilders
                .matchPhraseQuery(DynamicTable.DATE,
                        endTime.substring(0, endTime.indexOf(" "))))
                .setSize(1)
                .get();
        responsesArray[0] = responseV1;
        responsesArray[1] = responseV2;
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
        SearchRequestBuilder sbuilder = esClient.prepareSearch(DynamicShowTable.INDEX).setTypes(DynamicShowTable.TYPE).setQuery(timeQuery);
        TermsAggregationBuilder timeagg = AggregationBuilders.terms("times").field("time").size(1000000);
        SumAggregationBuilder countagg = AggregationBuilders.sum("count_count").field("count");
        timeagg.subAggregation(countagg);
        sbuilder.addAggregation(timeagg);
        return sbuilder.execute().actionGet();
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
