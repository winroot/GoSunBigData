package com.hzgc.service.visual.service;

import com.hzgc.common.attribute.bean.Attribute;
import com.hzgc.common.attribute.bean.AttributeValue;
import com.hzgc.common.attribute.service.AttributeService;
import com.hzgc.common.service.table.column.DynamicShowTable;
import com.hzgc.common.service.table.column.DynamicTable;
import com.hzgc.common.util.searchtype.SearchType;
import com.hzgc.service.visual.bean.AttributeCount;
import com.hzgc.service.visual.bean.CaptureCount;
import com.hzgc.service.visual.bean.TimeSlotNumber;
import com.hzgc.service.visual.bean.TotalAndTodayCount;
import com.hzgc.service.visual.dao.ElasticSearchDao;
import com.hzgc.service.visual.dao.EsSearchParam;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
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
@Slf4j
public class CaptureCountService {
    @Autowired
    private ElasticSearchDao elasticSearchDao;

    /**
     * 抓拍统计与今日抓拍统计
     * 查询es的动态库，返回总抓拍数量和今日抓拍数量
     *
     * @param ipcId 设备ID：ipcId
     * @return 返回总抓拍数量和今日抓拍数量
     */
    public TotalAndTodayCount dynamicNumberService(List<String> ipcId) {
        SearchResponse[] responsesArray = elasticSearchDao.dynamicNumberService(ipcId);
        SearchResponse searchResponse0 = responsesArray[0];
        SearchHits searchHits0 = searchResponse0.getHits();
        int totalNumber = (int) searchHits0.getTotalHits();
        SearchResponse searchResponse1 = responsesArray[1];
        SearchHits searchHits1 = searchResponse1.getHits();
        int todaytotalNumber = (int) searchHits1.getTotalHits();
        TotalAndTodayCount count = new TotalAndTodayCount();
        count.setTotalNumber(totalNumber);
        count.setTodayTotalNumber(todaytotalNumber);
        return count;
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
    public TimeSlotNumber timeSoltNumber(List<String> ipcids, String startTime, String endTime) {
        TimeSlotNumber slotNumber = new TimeSlotNumber();
        List<String> times;
        if (startTime != null && endTime != null && !startTime.equals("") && !endTime.equals("")) {
            times = getHourTime(startTime, endTime);
            SearchResponse searchResponse = elasticSearchDao.timeSoltNumber(ipcids, startTime, endTime);
            SearchHits searchHits = searchResponse.getHits();
            SearchHit[] hits = searchHits.getHits();
            if (times.size() > 0) {
                for (String time : times) {
                    int count = 0;
                    for (SearchHit hit : hits) {
                        String actTime = (String) hit.getSource().get(DynamicShowTable.TIME);
                        int actCount = (int) hit.getSource().get(DynamicShowTable.COUNT);
                        if (Objects.equals(actTime, time)) {
                            count += actCount;
                        }
                    }
                    slotNumber.getTimeSlotNumber().put(time, count);
                }
            }
        }
        return slotNumber;
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
    public CaptureCount captureCountQuery(String startTime, String endTime, String ipcId) {
        //CaptureCount是一个封装类，用于封装返回的结果。
        CaptureCount result = new CaptureCount();
        //所有判断结束后
        SearchResponse searchResponse = elasticSearchDao.captureCountQuery(startTime, endTime, ipcId);

        SearchHits hits = searchResponse.getHits(); //返回结果包含的文档放在数组hits中
        long totalresultcount = hits.getTotalHits(); //符合qb条件的结果数量

        //返回结果包含的文档放在数组hits中
        SearchHit[] searchHits = hits.hits();
        //若不存在符合条件的查询结果
        if (totalresultcount == 0) {
            log.error("The result count is 0! Last capture time does not exist!");
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
    public Long getCaptureCount(String startTime, String endTime, List<String> ipcId) {
        SearchResponse searchResponse = elasticSearchDao.getCaptureCount(startTime, endTime, ipcId);
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
    public List<AttributeCount> captureAttributeQuery(String startTime, String endTime, List<String> ipcIdList, SearchType type) {
        List<AttributeCount> attributeCountList = new ArrayList<>();
        if (type == SearchType.PERSON) {
            AttributeService attributeService = new AttributeService();
            if (ipcIdList != null && ipcIdList.size() > 0) {
                for (String ipcId : ipcIdList) {
                    AttributeCount attributeCount = new AttributeCount();
                    attributeCount.setIPCId(ipcId);
                    CaptureCount captureCount = captureCountQuery(startTime, endTime, ipcId);
                    long count = captureCount.getTotalresultcount();
                    attributeCount.setCaptureCount(count);
                    List<Attribute> attributeList = attributeService.getAttribute(type);
                    for (Attribute attribute : attributeList) {
                        List<AttributeValue> values = attribute.getValues();
                        for (AttributeValue attributeValue : values) {
                            SearchResponse searchResponse = elasticSearchDao
                                    .captureAttributeQuery(startTime, endTime, ipcId, attribute, attributeValue.getValue());
                            SearchHits hits = searchResponse.getHits();
                            long totalHits = hits.getTotalHits();
                            attributeValue.setCount(totalHits);
                        }
                    }
                    attributeCount.setAttributes(attributeList);
                    attributeCountList.add(attributeCount);
                }
            } else {
                log.error("ipcIdList is null.");
            }
        } else if (type == SearchType.CAR) {
            log.error("No vehicle queries are currently supported");
        } else {
            log.error("method AttributeService.captureAttributeQuery SearchType is error");
        }
        return attributeCountList;
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
        DateFormat df = new SimpleDateFormat(EsSearchParam.TIMEFORMAT_YMDHMS);
        try {
            start.setTime(df.parse(startTime));
            Long startTimeL = start.getTimeInMillis();
            Calendar end = Calendar.getInstance();
            end.setTime(df.parse(endTime));
            Long endTimeL = end.getTimeInMillis();
            Long onehour = EsSearchParam.LONG_OBNEHOUR;
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
}