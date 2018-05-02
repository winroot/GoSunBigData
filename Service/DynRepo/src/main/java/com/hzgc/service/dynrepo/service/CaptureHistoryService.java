package com.hzgc.service.dynrepo.service;

import com.hzgc.common.service.table.column.DynamicTable;
import com.hzgc.common.service.connection.ElasticSearchHelper;
import com.hzgc.common.util.json.JSONUtil;
import com.hzgc.service.dynrepo.bean.*;
import com.hzgc.service.dynrepo.dao.ElasticSearchDao;
import com.hzgc.service.dynrepo.dao.EsSearchParam;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.hzgc.service.dynrepo.service.CaptureServiceHelper.getFtpUrl;
import static com.hzgc.service.dynrepo.service.CaptureServiceHelper.surlToBurl;

@Service
public class CaptureHistoryService {
    private static Logger LOG = Logger.getLogger(CaptureHistoryService.class);
    @Autowired
    private ElasticSearchDao elasticSearchDao;
    @Autowired
    private Environment environment;

    public List<SearchResult> getCaptureHistory(SearchOption option) {
        if (option == null || option.getSearchType() == null ||
                (option.getSortParams() != null && option.getSortParams().size() > 0)) {
            LOG.warn("Start query capture history, search option is null");
            return new ArrayList<>();
        }
        LOG.info("Start query capture history, search option is:" + JSONUtil.toJson(option));
        String sortParam = EsSearchParam.DESC;
        for (SortParam s : option.getSortParams()) {
            if (s.name().equals(SortParam.TIMEDESC.toString())) {
                sortParam = EsSearchParam.DESC;
            } else if (s.name().equals(SortParam.SIMDASC.toString())) {
                sortParam = EsSearchParam.ASC;
            }
        }
        LOG.debug("Sort param is " + sortParam);
        if (option.getDeviceIds() != null &&
                option.getDeviceIds().size() > 0 &&
                option.getSortParams().get(0).name().equals(SortParam.IPC.toString())) {
            LOG.debug("The current query needs to be grouped by ipcid");
            return getCaptureHistory(option, sortParam);
        } else if (option.getDeviceIds() != null && option.getDeviceIds().size() > 0 &&
                !option.getSortParams().get(0).name().equals(SortParam.IPC.toString())) {
            LOG.debug("The current query don't needs to be grouped by ipcid");
            return getCaptureHistory(option, option.getDeviceIds(), sortParam);
        } else {
            LOG.debug("The current query is default");
            return getDefaultCaptureHistory(option, sortParam);
        }
    }

    private List<SearchResult> getDefaultCaptureHistory(SearchOption option, String sortParam) {
        List<SearchResult> resultList = new ArrayList<>();
        SearchResult result = new SearchResult();
        List<SingleResult> results = new ArrayList<>();
        SingleResult singleResult = new SingleResult();
        SearchResponse searchResponse = elasticSearchDao.getCaptureHistory(option, sortParam);
        SearchHits searchHits = searchResponse.getHits();
        SearchHit[] hits = searchHits.getHits();
        int totallCount = (int) searchHits.getTotalHits();
        List<CapturedPicture> persons = new ArrayList<>();
        CapturedPicture capturePicture;
        if (hits.length > 0) {
            for (SearchHit hit : hits) {
                capturePicture = new CapturedPicture();
                String surl = hit.getId();
                String burl = surlToBurl(surl);
                String ipcid = (String) hit.getSource().get(DynamicTable.IPCID);
                String timestamp = (String) hit.getSource().get(DynamicTable.TIMESTAMP);
                capturePicture.setSurl(getFtpUrl(surl));
                capturePicture.setBurl(getFtpUrl(burl));
                capturePicture.setIpcId(ipcid);
                capturePicture.setTimeStamp(timestamp);
                persons.add(capturePicture);
            }
        }
        singleResult.setTotal(totallCount);
        singleResult.setPictures(persons);
        results.add(singleResult);
        result.setResults(results);
        resultList.add(result);
        return resultList;
    }

    private List<SearchResult> getCaptureHistory(SearchOption option, String sortParam) {
        List<SearchResult> resultList = new ArrayList<>();
        for (String ipcId : option.getDeviceIds()) {
            SearchResult result = new SearchResult();
            List<SingleResult> results = new ArrayList<>();
            SingleResult singleResult = new SingleResult();
            List<CapturedPicture> capturedPictureList = new ArrayList<>();
            List<GroupByIpc> picturesByIpc = new ArrayList<>();
            GroupByIpc groupByIpc = new GroupByIpc();

            SearchResponse searchResponse = elasticSearchDao.getCaptureHistory(option, ipcId, sortParam);
            SearchHits searchHits = searchResponse.getHits();

            SearchHit[] hits = searchHits.getHits();
            CapturedPicture capturePicture;
            groupByIpc.setIpc(ipcId);
            picturesByIpc.add(groupByIpc);
            if (hits.length > 0) {
                for (SearchHit hit : hits) {
                    capturePicture = new CapturedPicture();
                    String surl = hit.getId();
                    String burl = surlToBurl(surl);
                    String ipc = (String) hit.getSource().get(DynamicTable.IPCID);
                    String timestamp = (String) hit.getSource().get(DynamicTable.TIMESTAMP);
                    capturePicture.setSurl(getFtpUrl(surl));
                    capturePicture.setBurl(getFtpUrl(burl));
                    capturePicture.setIpcId(ipc);
                    capturePicture.setTimeStamp(timestamp);
                    if (ipcId.equals(ipc)) {
                        capturedPictureList.add(capturePicture);
                    }
                }
            } else {
                capturePicture = new CapturedPicture();
                capturedPictureList.add(capturePicture);
            }
            singleResult.setTotal((int) searchHits.getTotalHits());
            singleResult.setPicturesByIpc(picturesByIpc);
            singleResult.setPictures(capturedPictureList);
            results.add(singleResult);
            result.setResults(results);
            resultList.add(result);
        }
        return resultList;
    }

    private List<SearchResult> getCaptureHistory(SearchOption option, List<String> deviceIds, String sortParam) {
        List<SearchResult> resultList = new ArrayList<>();
        SearchResult result = new SearchResult();
        List<SingleResult> results = new ArrayList<>();
        SingleResult singleResult = new SingleResult();
        List<CapturedPicture> captureList = new ArrayList<>();

        SearchResponse searchResponse = elasticSearchDao.getCaptureHistory(option, deviceIds, sortParam);
        SearchHits searchHits = searchResponse.getHits();
        SearchHit[] hits = searchHits.getHits();
        CapturedPicture capturePicture;
        if (hits.length > 0) {
            for (SearchHit hit : hits) {
                capturePicture = new CapturedPicture();
                String surl = hit.getId();
                String burl = surlToBurl(surl);
                String ipc = (String) hit.getSource().get(DynamicTable.IPCID);
                String timestamp = (String) hit.getSource().get(DynamicTable.TIMESTAMP);
                capturePicture.setSurl(getFtpUrl(surl));
                capturePicture.setBurl(getFtpUrl(burl));
                capturePicture.setIpcId(ipc);
                capturePicture.setTimeStamp(timestamp);
            }
        }
        singleResult.setTotal((int) searchHits.getTotalHits());
        singleResult.setPictures(captureList);
        results.add(singleResult);
        result.setResults(results);
        resultList.add(result);
        return resultList;
    }
}