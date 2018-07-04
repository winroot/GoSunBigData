package com.hzgc.service.alarm.dao;

import com.hzgc.common.es.ElasticSearchHelper;
import com.hzgc.common.facealarm.table.AlarmTable;
import com.hzgc.service.alarm.bean.AlarmBean;
import com.hzgc.service.alarm.bean.AlarmData;
import com.hzgc.service.alarm.bean.UserAlarmMessage;
import com.hzgc.service.util.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class AlarmDao {

    @Autowired
    private AlarmDao alarmDao;

    private TransportClient esClient;
    public AlarmDao(@Value("${es.cluster.name}") String clusterName,
                    @Value("${es.hosts}") String esHost,
                    @Value("${es.cluster.port}") String esPort) {
        this.esClient = ElasticSearchHelper.getEsClient(clusterName, esHost, Integer.parseInt(esPort));
    }

    private SearchResponse searchResponse = null;
    public ResponseResult<List<UserAlarmMessage>> alarmSearch(AlarmBean alarmBean) {
        //离线告警
        if (null != alarmBean.getAlarm_type() && AlarmTable.OFFLINE.equals(alarmBean.getAlarm_type())){
            BoolQueryBuilder timeBuilder = QueryBuilders.boolQuery();
            timeBuilder.must(QueryBuilders.rangeQuery("alarm_time").gte(alarmBean.getAlarm_start_time())
                    .lte(alarmBean.getAlarm_end_time()));
            SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(AlarmTable.INDEX)
                    .setTypes(AlarmTable.OFF_TYPE)
                    .setQuery(timeBuilder)
                    .addSort("alarm_time",SortOrder.DESC);
            //参数判断
            alarmDao.paramChoose(searchRequestBuilder,alarmBean);
            SearchHits hits = searchResponse.getHits();
            long rowNum = hits.totalHits;
            SearchHit[] hits2 = hits.getHits();
            List<UserAlarmMessage> offList = new ArrayList<>();
            //参数封装
            alarmDao.off_param_package(hits2,offList);
            return ResponseResult.init(offList,rowNum);
        }
        //新增告警
        if (null != alarmBean.getAlarm_type() && AlarmTable.ADDED.equals(alarmBean.getAlarm_type())){
            BoolQueryBuilder timeBuilder = QueryBuilders.boolQuery();
            timeBuilder.must(QueryBuilders.rangeQuery("alarm_time").gte(alarmBean.getAlarm_start_time())
                    .lte(alarmBean.getAlarm_end_time()));
            timeBuilder.must(QueryBuilders.matchQuery("alarm_type",AlarmTable.ADDED));
                    SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(AlarmTable.INDEX)
                    .setTypes(AlarmTable.REC_TYPE)
                    .setQuery(timeBuilder)
                    .addSort("alarm_time",SortOrder.DESC);
            //参数判断
            alarmDao.paramChoose(searchRequestBuilder,alarmBean);
            SearchHits hits = searchResponse.getHits();
            long rowNum = hits.totalHits;
            SearchHit[] hits2 = hits.getHits();
            List<UserAlarmMessage> addList = new ArrayList<>();
            //参数封装
            alarmDao.add_param_package(hits2,addList);
            return ResponseResult.init(addList,rowNum);
        }
        //识别告警
        if (null != alarmBean.getAlarm_type() && AlarmTable.IDENTIFY.equals(alarmBean.getAlarm_type())){
            BoolQueryBuilder builder = QueryBuilders.boolQuery();
            builder.must(QueryBuilders.rangeQuery("alarm_time").gte(alarmBean.getAlarm_start_time())
                    .lte(alarmBean.getAlarm_end_time()));
            if (alarmBean.getSimilarity() > 0.0){
                builder.must(QueryBuilders.rangeQuery("similarity").gte(alarmBean.getSimilarity()));
            }
            builder.must(QueryBuilders.matchQuery("alarm_type",AlarmTable.IDENTIFY));
            SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(AlarmTable.INDEX)
                    .setTypes(AlarmTable.REC_TYPE)
                    .setQuery(builder)
                    .addSort("alarm_time",SortOrder.DESC);
            //参数判断
            alarmDao.paramChoose(searchRequestBuilder,alarmBean);
            SearchHits hits = searchResponse.getHits();
            long rowNum = hits.totalHits;
            SearchHit[] hits2 = hits.getHits();
            List<UserAlarmMessage> idenList = new ArrayList<>();
            //参数封装
            alarmDao.iden_param_package(hits2,idenList);
            return ResponseResult.init(idenList,rowNum);
        }
        //告警不详,查询全部
        if (null != alarmBean.getAlarm_type() && AlarmTable.UNKNOWN.equals(alarmBean.getAlarm_type())) {
            BoolQueryBuilder timeBuilder = QueryBuilders.boolQuery();
            timeBuilder.must(QueryBuilders.rangeQuery("alarm_time").gte(alarmBean.getAlarm_start_time())
                    .lte(alarmBean.getAlarm_end_time()));
            if (alarmBean.getSimilarity() > 0.0) {
                timeBuilder.must(QueryBuilders.rangeQuery("similarity").gte(alarmBean.getSimilarity()));
            }
            SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(AlarmTable.INDEX)
                    .setTypes(AlarmTable.OFF_TYPE)
                    .setQuery(timeBuilder)
                    .addSort("alarm_time", SortOrder.DESC);
            //离线参数判断
            alarmDao.paramChoose(searchRequestBuilder, alarmBean);
            SearchHits hits = searchResponse.getHits();
            SearchHit[] hits2 = hits.getHits();
            List <UserAlarmMessage> list = new ArrayList <>();
            //离线参数封装
            alarmDao.off_param_package(hits2, list);
            SearchRequestBuilder searchRequestBuilder1 = esClient.prepareSearch(AlarmTable.INDEX)
                    .setTypes(AlarmTable.REC_TYPE)
                    .setQuery(timeBuilder)
                    .addSort("alarm_time", SortOrder.DESC);
            //识别参数判断
            alarmDao.paramChoose(searchRequestBuilder1, alarmBean);
            SearchHits hits3 = searchResponse.getHits();
            SearchHit[] hits4 = hits3.getHits();
            //识别参数封装
            alarmDao.iden_param_package(hits4, list);
            SearchRequestBuilder searchRequestBuilder2 = esClient.prepareSearch(AlarmTable.INDEX)
                    .setTypes(AlarmTable.REC_TYPE)
                    .setQuery(QueryBuilders.matchQuery("alarm_type", AlarmTable.ADDED))
                    .setQuery(timeBuilder)
                    .addSort("alarm_time", SortOrder.DESC);
            //新增参数判断
            alarmDao.paramChoose(searchRequestBuilder2, alarmBean);
            SearchHits hits5 = searchResponse.getHits();
            SearchHit[] hits6 = hits3.getHits();
            //新增参数封装
            alarmDao.add_param_package(hits6, list);
            return ResponseResult.init(list, hits.totalHits + hits3.totalHits + hits5.totalHits);
        }
        return ResponseResult.init(new ArrayList <>(),0L);
    }

    //参数判断
    private void paramChoose(SearchRequestBuilder searchRequestBuilder,AlarmBean alarmBean){
        //设备ID和对象类型都传
        if (null != alarmBean.getObject_type() && null != alarmBean.getDevice_id()){
            searchResponse = searchRequestBuilder
                    .setQuery(QueryBuilders.queryStringQuery("ipc_id:" + alarmBean.getDevice_id() + " AND " +
                        "object_type:" + alarmBean.getObject_type()))
                    .setFrom(alarmBean.getStart())
                    .setSize(alarmBean.getLimit())
                    .get();
        }
        //只传入了对象类型，没有传入设备ID
        if (null != alarmBean.getObject_type()){
            searchResponse = searchRequestBuilder
                    .setQuery(QueryBuilders.matchQuery("object_type",alarmBean.getObject_type()))
                    .setFrom(alarmBean.getStart())
                    .setSize(alarmBean.getLimit())
                    .get();
        }
        //只传入设备ID，没有传入对象类型
        if (null != alarmBean.getDevice_id()){
            searchResponse = searchRequestBuilder
                    .setQuery(QueryBuilders.matchQuery("ipc_id",alarmBean.getDevice_id()))
                    .setFrom(alarmBean.getStart())
                    .setSize(alarmBean.getLimit())
                    .get();
        }
        //设别ID和对象类型都没有传
        searchResponse = searchRequestBuilder
                .setFrom(alarmBean.getStart())
                .setSize(alarmBean.getLimit())
                .get();
    }

    //离线告警参数封装
    private void off_param_package(SearchHit[] hits2,List<UserAlarmMessage> offList){
        for (SearchHit searchHit : hits2) {
            UserAlarmMessage userAlarmMessage = new UserAlarmMessage();
            AlarmData alarmData = new AlarmData();
            String staticId = (String) searchHit.getSource().get("static_id");
            String lastAppearanceTime = (String) searchHit.getSource().get("last_appearance_time");
            String alarm_type = String.valueOf(searchHit.getSource().get("alarm_type")) ;
            String occurTime = (String) searchHit.getSource().get("alarm_time");
            alarmData.setStaticId(staticId);
            alarmData.setLastAppearanceTime(lastAppearanceTime);
            userAlarmMessage.setAlarmData(alarmData);
            userAlarmMessage.setOccurTime(occurTime);
            userAlarmMessage.setAlarmType(alarm_type);
            offList.add(userAlarmMessage);
        }
    }

    //新增告警参数封装
    private void add_param_package(SearchHit[] hits2,List<UserAlarmMessage> addList){
        for (SearchHit searchHit : hits2) {
            AlarmData alarmData = new AlarmData();
            UserAlarmMessage userAlarmMessage = new UserAlarmMessage();
            String bigPictureUrl = (String) searchHit.getSource().get("big_picture_url");
            String smallPictureUrl = (String) searchHit.getSource().get("small_picture_url");
            String deviceName = (String) searchHit.getSource().get("host_name");
            String deviceId = (String) searchHit.getSource().get("ipc_id");
            String occurTime = (String) searchHit.getSource().get("alarm_time");
            String alarmType = String.valueOf(searchHit.getSource().get("alarm_type"));
            alarmData.setBigPictureURL(bigPictureUrl);
            alarmData.setSmallPictureURL(smallPictureUrl);
            userAlarmMessage.setAlarmData(alarmData);
            userAlarmMessage.setDeviceName(deviceName);
            userAlarmMessage.setDeviceID(deviceId);
            userAlarmMessage.setAlarmType(alarmType);
            userAlarmMessage.setOccurTime(occurTime);
            addList.add(userAlarmMessage);
        }
    }

    //识别告警参数封装
    private void iden_param_package(SearchHit[] hits2,List<UserAlarmMessage> idenList){
        for (SearchHit searchHit : hits2){
            UserAlarmMessage userAlarmMessage = new UserAlarmMessage();
            AlarmData alarmData = new AlarmData();
            String bigPictureUrl = (String) searchHit.getSource().get("big_picture_url");
            String smallPictureUrl = (String) searchHit.getSource().get("small_picture_url");
            String deviceName = (String) searchHit.getSource().get("host_name");
            String deviceId = (String) searchHit.getSource().get("ipc_id");
            String occurTime = (String) searchHit.getSource().get("alarm_time");
            String staticId = (String) searchHit.getSource().get("static_id");
            String similarity = String.valueOf(searchHit.getSource().get("similarity"));
            String objType = (String) searchHit.getSource().get("object_type");
            String alarmType = String.valueOf(searchHit.getSource().get("alarm_type"));
            alarmData.setBigPictureURL(bigPictureUrl);
            alarmData.setSmallPictureURL(smallPictureUrl);
            alarmData.setStaticId(staticId);
            alarmData.setObjType(objType);
            alarmData.setSimilarity(similarity);
            userAlarmMessage.setAlarmData(alarmData);
            userAlarmMessage.setAlarmType(alarmType);
            userAlarmMessage.setDeviceID(deviceId);
            userAlarmMessage.setOccurTime(occurTime);
            userAlarmMessage.setDeviceName(deviceName);
            idenList.add(userAlarmMessage);
        }
    }
}
