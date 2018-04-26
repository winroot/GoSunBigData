package com.hzgc.service.dynrepo.service;

import com.hzgc.common.service.table.column.DynamicTable;
import com.hzgc.common.service.table.column.ObjectInfoTable;
import com.hzgc.common.service.connection.ElasticSearchHelper;
import com.hzgc.common.util.searchtype.SearchType;
import com.hzgc.service.dynrepo.attribute.Attribute;
import com.hzgc.service.dynrepo.attribute.AttributeCount;
import com.hzgc.service.dynrepo.attribute.AttributeValue;
import com.hzgc.service.dynrepo.object.CaptureCount;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 这个方法是为了大数据可视化而指定的，继承于CaptureCountService，主要包含三个方法：
 * 1、dynaicNumberService：查询es的动态库，返回总抓拍数量和今日抓拍数量
 * 2、staticNumberService：查询es的静态库，返回每个平台下（对应platformId），每个对象库（对应pkey）下的人员的数量
 * 3、timeSoltNumber：根据入参ipcid的list、startTime和endTime去es查询到相应的值
 */
@Service
public class CaptureCountServiceImpl implements CaptureCountService {

    private static Logger LOG = Logger.getLogger(CapturePictureSearchServiceImpl.class);

    /**
     * 抓拍统计与今日抓拍统计
     * 查询es的动态库，返回总抓拍数量和今日抓拍数量
     *
     * @param ipcId 设备ID：ipcId
     * @return 返回总抓拍数量和今日抓拍数量
     */
    @Override
    public synchronized Map<String, Integer> dynaicNumberService(List<String> ipcId) {
        String index = DynamicTable.DYNAMIC_INDEX;
        String type = DynamicTable.PERSON_INDEX_TYPE;
        Map<String, Integer> map = new HashMap<>();
        BoolQueryBuilder totolBQ = QueryBuilders.boolQuery();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long a = System.currentTimeMillis();
        String endTime = format.format(a);
        String startTime = endTime.substring(0, endTime.indexOf(" ")) + " 00:00:00";
        totolBQ.must(QueryBuilders.rangeQuery(DynamicTable.TIMESTAMP).gte(startTime).lte(endTime));
        BoolQueryBuilder ipcBQ = QueryBuilders.boolQuery();
        if (ipcId != null) {
            for (String ipcid : ipcId) {
                ipcBQ.should(QueryBuilders.matchPhraseQuery(DynamicTable.IPCID, ipcid));
            }
            totolBQ.must(ipcBQ);
        }
        TransportClient client = ElasticSearchHelper.getEsClient();
        SearchResponse searchResponse1 = client.prepareSearch(index)
                .setTypes(type)
                .setSize(1)
                .get();
        SearchHits searchHits1 = searchResponse1.getHits();
        int totolNumber = (int) searchHits1.getTotalHits();
        SearchResponse searchResponse2 = client.prepareSearch(index)
                .setTypes(type)
                .setQuery(totolBQ)
                .setSize(1)
                .get();
        SearchHits searchHits2 = searchResponse2.getHits();
        int todayTotolNumber = (int) searchHits2.getTotalHits();
        map.put(totolNum, totolNumber);
        map.put(todyTotolNumber, todayTotolNumber);
        return map;
    }

    /**
     * 单平台下对象库人员统计
     * 查询es的静态库，返回每个平台下（对应platformId），每个对象库（对应pkey）下的人员的数量
     *
     * @param platformId 平台ID
     * @return 返回每个平台下（对应platformId），每个对象库（对应pkey）下的人员的数量
     */
    @Override
    public synchronized Map<String, Integer> staticNumberService(String platformId) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        String index = ObjectInfoTable.TABLE_NAME;
        String type = ObjectInfoTable.PERSON_COLF;
        Map<String, Integer> map = new HashMap<>();
        if (platformId != null) {
            boolQueryBuilder.must(QueryBuilders.termsQuery(ObjectInfoTable.PLATFORMID, platformId));
        }
        SearchRequestBuilder searchRequestBuilder = ElasticSearchHelper.getEsClient()
                .prepareSearch(index)
                .setTypes(type)
                .setQuery(boolQueryBuilder);
        TermsAggregationBuilder tamAgg = AggregationBuilders.terms("pkey_count").field("pkey");
        searchRequestBuilder.addAggregation(tamAgg);
        SearchResponse response = searchRequestBuilder.execute().actionGet();
        Terms pkey_count = response.getAggregations().get("pkey_count");
        for (Terms.Bucket bk : pkey_count.getBuckets()) {
            String pkey = (String) bk.getKey();
            int pkeyNumber = (int) bk.getDocCount();
            map.put(pkey, pkeyNumber);
        }
        return map;
    }

    /**
     * 多设备每小时抓拍统计
     * 根据入参ipcid的list、startTime和endTime去es查询到相应的值
     *
     * @param ipcids    设备ID：ipcid
     * @param startTime 搜索的开始时间
     * @param endTime   搜索的结束时间
     * @return 返回某段时间内，这些ipcid的抓拍的总数量
     */
    @Override
    public synchronized Map<String, Integer> timeSoltNumber(List<String> ipcids, String startTime, String endTime) {
        List<String> times = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();
        BoolQueryBuilder totolQuery = QueryBuilders.boolQuery();
        if (ipcids != null && ipcids.size() > 0) {
            for (String ipcid : ipcids) {
                totolQuery.should(QueryBuilders.matchPhraseQuery("ipcid", ipcid));
            }
        }
        BoolQueryBuilder timeQuery = QueryBuilders.boolQuery();
        if (startTime != null && endTime != null && !startTime.equals("") && !endTime.equals("")) {
            times = getHourTime(startTime, endTime);
            timeQuery.must(QueryBuilders.rangeQuery("time").gte(startTime).lte(endTime));
        }
        timeQuery.must(totolQuery);
        SearchResponse searchResponse = ElasticSearchHelper.getEsClient()
                .prepareSearch("dynamicshow")
                .setTypes("person")
                .setQuery(timeQuery)
                .setSize(100000000)
                .get();
        SearchHits searchHits = searchResponse.getHits();
        SearchHit[] hits = searchHits.getHits();
        if (times != null && times.size() > 0) {
            for (String time : times) {
                int count = 0;
                for (SearchHit hit : hits) {
                    String actTime = (String) hit.getSource().get("time");
                    int actCount = (int) hit.getSource().get("count");
                    if (Objects.equals(actTime, time)) {
                        count += actCount;
                    }
                }
                map.put(time, count);
            }
        }
        return map;
    }

    /**
     * 通过入参确定起始和截止的时间，返回这段时间内的每一个小时的String
     *
     * @param startTime 开始时间
     * @param endTime   截止时间
     * @return 返回这段时间内的每一个小时的String
     */
    private List<String> getHourTime(String startTime, String endTime) {
        List<String> timeList = new ArrayList<>();
        Calendar start = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            start.setTime(df.parse(startTime));
            Long startTimeL = start.getTimeInMillis();
            Calendar end = Calendar.getInstance();
            end.setTime(df.parse(endTime));
            Long endTimeL = end.getTimeInMillis();
            Long onehour = 1000 * 60 * 60L;
            Long time = startTimeL;
            while (time <= endTimeL) {
                Date everyTime = new Date(time);
                String timee = df.format(everyTime);
                timeList.add(timee);
                time += onehour;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeList;
    }

    /**
     * 单设备抓拍统计（马燊偲）
     * 查询指定时间段内，指定设备抓拍的图片数量、该设备最后一次抓拍时间
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param ipcId     设备ID
     * @return CaptureCount 查询结果对象。对象内封装了：该时间段内该设备抓拍张数，该时间段内该设备最后一次抓拍时间。
     */
    @Override
    public CaptureCount captureCountQuery(String startTime, String endTime, String ipcId) {

        //CaptureCount是一个封装类，用于封装返回的结果。
        CaptureCount result = new CaptureCount();

        // 最终封装成的boolQueryBuilder 对象。
        BoolQueryBuilder totalBQ = QueryBuilders.boolQuery();
        //totalBQ = ;

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
        SearchResponse searchResponse = ElasticSearchHelper.getEsClient() //启动Es Java客户端
                .prepareSearch(DynamicTable.DYNAMIC_INDEX) //指定要查询的索引名称
                .setTypes(DynamicTable.PERSON_INDEX_TYPE) //指定要查询的类型名称
                .setQuery(totalBQ) //根据查询条件qb设置查询
                .addSort(DynamicTable.TIMESTAMP, SortOrder.DESC) //以时间字段降序排序
                .get();

        SearchHits hits = searchResponse.getHits(); //返回结果包含的文档放在数组hits中
        long totalresultcount = hits.getTotalHits(); //符合qb条件的结果数量

        //返回结果包含的文档放在数组hits中
        SearchHit[] searchHits = hits.hits();
        //若不存在符合条件的查询结果
        if (totalresultcount == 0) {
            LOG.error("The result count is 0! Last capture time does not exist!");
            result.setTotalresultcount(totalresultcount);
            result.setLastcapturetime("None");
        } else {
            /*
              获取该时间段内设备最后一次抓拍时间：
              返回结果包含的文档放在数组hits中，由于结果按照降序排列，
              因此hits数组里的第一个值代表了该设备最后一次抓拍的具体信息
              例如{"s":"XXXX","t":"2017-09-20 15:55:06","sj":"1555"}
              将该信息以Map形式读取，再获取到key="t“的值，即最后一次抓拍时间。
             */

            //获取最后一次抓拍时间
            String lastcapturetime = (String) searchHits[0].getSourceAsMap().get(DynamicTable.TIMESTAMP);

            /*
              返回值为：设备抓拍张数、设备最后一次抓拍时间。
             */
            result.setTotalresultcount(totalresultcount);
            result.setLastcapturetime(lastcapturetime);
        }
        return result;
    }

    /**
     * 多设备抓拍统计（陈柯）
     * 查询指定时间段内，指定的多个设备抓拍的图片数量
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param ipcId     多个设备id
     * @return 图片数量以long值表示
     */
    @Override
    public Long getCaptureCount(String startTime, String endTime, List<String> ipcId) {
        BoolQueryBuilder totolBQ = QueryBuilders.boolQuery();
        // 开始时间和结束时间存在的时候的处理
        if (startTime != null && endTime != null && !startTime.equals("") && !endTime.equals("")) {
            totolBQ.must(QueryBuilders.rangeQuery(DynamicTable.TIMESTAMP).gte(startTime).lte(endTime));
        }
        BoolQueryBuilder ipcIdBQ = QueryBuilders.boolQuery();
        if (ipcId != null && ipcId.size() > 0) {
            for (String ipcid : ipcId) {
                ipcIdBQ.should(QueryBuilders.matchPhraseQuery(DynamicTable.IPCID, ipcid).analyzer("standard"));
            }
        }
        totolBQ.must(ipcIdBQ);
        SearchResponse searchResponse = ElasticSearchHelper.getEsClient()
                .prepareSearch(DynamicTable.DYNAMIC_INDEX)
                .setTypes(DynamicTable.PERSON_INDEX_TYPE)
                .setQuery(totolBQ)
                .get();
        SearchHits searchHits = searchResponse.getHits();
        return searchHits.getTotalHits();
    }

    /**
     * 抓拍属性统计 (刘思阳)
     * 查询指定时间段内，单个或某组设备中某种属性在抓拍图片中的数量
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param ipcIdList 单个或某组设备ID
     * @param type      统计类型
     * @return 单个或某组设备中某种属性在抓拍图片中的数量
     */
    @Override
    public List<AttributeCount> captureAttributeQuery(String startTime, String endTime, List<String> ipcIdList, SearchType type) {
        List<AttributeCount> attributeCountList = new ArrayList<>();

        if (type == SearchType.PERSON) {
            CapturePictureSearchServiceImpl capturePictureSearchService = new CapturePictureSearchServiceImpl();
            CaptureCountServiceImpl captureCountService = new CaptureCountServiceImpl();
            if (ipcIdList != null && ipcIdList.size() > 0) {
                for (String ipcId : ipcIdList) {
                    AttributeCount attributeCount = new AttributeCount();
                    attributeCount.setIPCId(ipcId);
                    CaptureCount captureCount = captureCountService.captureCountQuery(startTime, endTime, ipcId);
                    long count = captureCount.getTotalresultcount();
                    attributeCount.setCaptureCount(count);

                    List<Attribute> attributeList = capturePictureSearchService.getAttribute(type);
                    for (Attribute attribute : attributeList) {
                        List<AttributeValue> values = attribute.getValues();
                        for (AttributeValue attributeValue : values) {
                            BoolQueryBuilder FilterIpcId = QueryBuilders.boolQuery();
                            FilterIpcId.must(QueryBuilders.matchPhraseQuery(DynamicTable.IPCID, ipcId));
                            FilterIpcId.must(QueryBuilders.rangeQuery(DynamicTable.TIMESTAMP).gt(startTime).lt(endTime));
                            FilterIpcId.must(QueryBuilders.matchPhraseQuery(attribute.getIdentify().toLowerCase(), attributeValue.getValue()));
                            SearchResponse searchResponse = ElasticSearchHelper.getEsClient()
                                    .prepareSearch(DynamicTable.DYNAMIC_INDEX)
                                    .setTypes(DynamicTable.PERSON_INDEX_TYPE)
                                    .setQuery(FilterIpcId).get();
                            SearchHits hits = searchResponse.getHits();
                            long totalHits = hits.getTotalHits();
                            attributeValue.setCount(totalHits);
                        }
                    }
                    attributeCount.setAttributes(attributeList);
                    attributeCountList.add(attributeCount);
                }
            } else {
                LOG.error("ipcIdList is null.");
            }
        } else if (type == SearchType.CAR) {
            LOG.error("No vehicle queries are currently supported");
        } else {
            LOG.error("method CapturePictureSearchServiceImpl.captureAttributeQuery SearchType is error");
        }
        return attributeCountList;
    }
}
