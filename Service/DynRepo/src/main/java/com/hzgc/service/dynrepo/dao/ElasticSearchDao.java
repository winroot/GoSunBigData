package com.hzgc.service.dynrepo.dao;

import com.hzgc.common.service.connection.ElasticSearchHelper;
import com.hzgc.common.service.table.column.DynamicShowTable;
import com.hzgc.common.service.table.column.DynamicTable;
import com.hzgc.common.util.searchtype.SearchType;
import com.hzgc.service.dynrepo.attribute.Attribute;
import com.hzgc.service.dynrepo.attribute.AttributeCount;
import com.hzgc.service.dynrepo.attribute.AttributeValue;
import com.hzgc.service.dynrepo.bean.*;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Repository
public class ElasticSearchDao {
    private static Logger LOG = Logger.getLogger(ElasticSearchDao.class);
    private TransportClient esClient;


    public ElasticSearchDao(@Value("${es.cluster.name}") String clusterName,
                            @Value("${es.hosts}") String esHost,
                            @Value("${es.cluster.port}") String esPort) {
        this.esClient = ElasticSearchHelper.getEsClient(clusterName, esHost, Integer.parseInt(esPort));
    }


    public SearchResponse getCaptureHistory(SearchOption option, String sortParam) {
        BoolQueryBuilder queryBuilder = createBoolQueryBuilder(option);
        SearchRequestBuilder requestBuilder = createSearchRequestBuilder()
                .setQuery(queryBuilder)
                .setFrom(option.getOffset())
                .setSize(option.getCount())
                .addSort(DynamicTable.TIMESTAMP, SortOrder.fromString(sortParam));
        return requestBuilder.get();
    }

    public SearchResponse getCaptureHistory(SearchOption option, List<String> ipcList, String sortParam) {
        BoolQueryBuilder queryBuilder = createBoolQueryBuilder(option);
        setDeviceIdList(queryBuilder, ipcList);
        SearchRequestBuilder requestBuilder = createSearchRequestBuilder()
                .setQuery(queryBuilder)
                .setFrom(option.getOffset())
                .setSize(option.getCount())
                .addSort(DynamicTable.TIMESTAMP, SortOrder.fromString(sortParam));
        return requestBuilder.get();
    }

    public SearchResponse getCaptureHistory(SearchOption option, String ipc, String sortParam) {
        BoolQueryBuilder queryBuilder = createBoolQueryBuilder(option);
        setDeviceId(queryBuilder, ipc);
        SearchRequestBuilder requestBuilder = createSearchRequestBuilder()
                .setQuery(queryBuilder)
                .setFrom(option.getOffset())
                .setSize(option.getCount())
                .addSort(DynamicTable.TIMESTAMP, SortOrder.fromString(sortParam));
        return requestBuilder.get();
    }

    private SearchRequestBuilder createSearchRequestBuilder() {
        return esClient.prepareSearch(DynamicTable.DYNAMIC_INDEX)
                .setTypes(DynamicTable.PERSON_INDEX_TYPE);
    }

    private BoolQueryBuilder createBoolQueryBuilder(SearchOption option) {
        // 最终封装成的boolQueryBuilder 对象。
        BoolQueryBuilder totalBQ = QueryBuilders.boolQuery();
        //筛选人脸属性
        if (option.getAttributes() != null && option.getAttributes().size() > 0) {
            setAttribute(totalBQ, option.getAttributes());
        }

        // 开始时间和结束时间存在的时候的处理
        if (option.getStartDate() != null && option.getEndDate() != null &&
                !option.getStartDate().equals("") && !option.getEndDate().equals("")) {
            setStartEndTime(totalBQ, option.getStartDate(), option.getEndDate());
        }

        if (option.getIntervals() != null && option.getIntervals().size() > 0) {
            setTimeInterval(totalBQ, option.getIntervals());
        }
        return totalBQ;
    }

    private void setTimeInterval(BoolQueryBuilder totalBQ, List<TimeInterval> timeIntervals) {
        //临时存储对象
        TimeInterval temp;
        //时间段的BoolQueryBuilder
        BoolQueryBuilder timeInQB = QueryBuilders.boolQuery();
        for (TimeInterval timeInterval1 : timeIntervals) {
            temp = timeInterval1;
            int start_sj = temp.getStart();
            String start_ts = String.valueOf(start_sj * 100 / 60 + start_sj % 60);
            int end_sj = temp.getEnd();
            String end_ts = String.valueOf(end_sj * 100 / 60 + end_sj % 60);
            timeInQB.should(QueryBuilders.rangeQuery(DynamicTable.TIMESLOT).gte(start_ts).lte(end_ts));
            totalBQ.must(timeInQB);
        }
    }

    private void setStartEndTime(BoolQueryBuilder totalBQ, String startTime, String endTime) {
        totalBQ.must(QueryBuilders.rangeQuery(DynamicTable.TIMESTAMP).gte(startTime).lte(endTime));
    }

    private void setDeviceIdList(BoolQueryBuilder totalBQ, List<String> deviceId) {
        // 设备ID 的的boolQueryBuilder
        BoolQueryBuilder devicdIdBQ = QueryBuilders.boolQuery();
        for (Object t : deviceId) {
            devicdIdBQ.should(QueryBuilders.matchPhraseQuery(DynamicTable.IPCID, t).analyzer(EsSearchParam.STANDARD));
        }
        totalBQ.must(devicdIdBQ);
    }

    private void setDeviceId(BoolQueryBuilder totalBQ, String ipc) {
        BoolQueryBuilder deviceIdBQ = QueryBuilders.boolQuery();
        deviceIdBQ.should(QueryBuilders.matchPhraseQuery(DynamicTable.IPCID, ipc).analyzer(EsSearchParam.STANDARD));
        totalBQ.must(deviceIdBQ);
    }

    private void setAttribute(BoolQueryBuilder totalBQ, List<Attribute> attributes) {
        //人脸属性
        for (Attribute attribute : attributes) {
            String identify = attribute.getIdentify().toLowerCase();
            String logic = String.valueOf(attribute.getLogistic());
            List<AttributeValue> attributeValues = attribute.getValues();
            for (AttributeValue attributeValue : attributeValues) {
                int attr = attributeValue.getValue();
                if (attr != 0) {
                    if (logic.equals(EsSearchParam.OR)) {
                        totalBQ.should(QueryBuilders.matchQuery(identify, attr).analyzer(EsSearchParam.STANDARD));
                    } else {
                        totalBQ.must(QueryBuilders.matchQuery(identify, attr).analyzer(EsSearchParam.STANDARD));
                    }
                }
            }
        }
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
