package com.hzgc.service.dynrepo.service;

import com.hzgc.common.table.dynrepo.DynamicTable;
import com.hzgc.common.util.json.JSONUtil;
import com.hzgc.service.dynrepo.bean.*;
import com.hzgc.service.dynrepo.dao.ElasticSearchDao;
import com.hzgc.service.dynrepo.dao.EsSearchParam;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CaptureHistoryService {
    @Autowired
    @SuppressWarnings("unused")
    private ElasticSearchDao elasticSearchDao;
    @Autowired
    @SuppressWarnings("unused")
    private Environment environment;
    @Autowired
    @SuppressWarnings("unused")
    private CaptureServiceHelper captureServiceHelper;

    public List<SearchResult> getCaptureHistory(SearchOption option) {
        if (option == null ||
                (option.getSort() != null && option.getSort().size() > 0)) {
            log.warn("Start query capture history, search option is null");
            return new ArrayList<>();
        }
        log.info("Start query capture history, search option is:" + JSONUtil.toJson(option));
        String sortParam = EsSearchParam.DESC;
        List<SortParam> sortParams = option.getSort()
                .stream().map(param -> SortParam.values()[param]).collect(Collectors.toList());
        for (SortParam s : sortParams) {
            if (s.name().equals(SortParam.TIMEDESC.toString())) {
                sortParam = EsSearchParam.DESC;
            } else if (s.name().equals(SortParam.SIMDASC.toString())) {
                sortParam = EsSearchParam.ASC;
            }
        }
        log.debug("Sort param is " + sortParam);
        if (option.getDeviceIds() != null &&
                option.getDeviceIds().size() > 0 &&
                sortParams.get(0).name().equals(SortParam.IPC.toString())) {
            log.debug("The current query needs to be grouped by ipcid");
            return getCaptureHistory(option, sortParam);
        } else if (option.getDeviceIds() != null && option.getDeviceIds().size() > 0 &&
                !sortParams.get(0).name().equals(SortParam.IPC.toString())) {
            log.debug("The current query don't needs to be grouped by ipcid");
            return getCaptureHistory(option, option.getDeviceIds(), sortParam);
        } else {
            log.debug("The current query is default");
            return getDefaultCaptureHistory(option, sortParam);
        }
    }

    private List<SearchResult> getDefaultCaptureHistory(SearchOption option, String sortParam) {
        List<SearchResult> resultList = new ArrayList<>();
        SearchResult result = new SearchResult();
        List<SingleCaptureResult> results = new ArrayList<>();
        SingleCaptureResult singleResult = new SingleCaptureResult();
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
                String burl = captureServiceHelper.surlToBurl(surl);
                String ipcid = (String) hit.getSource().get(DynamicTable.IPCID);
                String timestamp = (String) hit.getSource().get(DynamicTable.TIMESTAMP);
                capturePicture.setSurl(captureServiceHelper.getFtpUrl(surl));
                capturePicture.setBurl(captureServiceHelper.getFtpUrl(burl));
                capturePicture.setDeviceId(ipcid);
                capturePicture.setTime(timestamp);
                persons.add(capturePicture);
            }
        }
        singleResult.setTotal(totallCount);
        singleResult.setPictures(persons);
        results.add(singleResult);
        result.setSingleResults(results);
        resultList.add(result);
        return resultList;
    }

    private List<SearchResult> getCaptureHistory(SearchOption option, String sortParam) {
        List<SearchResult> resultList = new ArrayList<>();
        for (String ipcId : option.getDeviceIds()) {
            SearchResult result = new SearchResult();
            List<SingleCaptureResult> results = new ArrayList<>();
            SingleCaptureResult singleResult = new SingleCaptureResult();
            List<CapturedPicture> capturedPictureList = new ArrayList<>();
            List<GroupByIpc> picturesByIpc = new ArrayList<>();
            GroupByIpc groupByIpc = new GroupByIpc();

            SearchResponse searchResponse = elasticSearchDao.getCaptureHistory(option, ipcId, sortParam);
            SearchHits searchHits = searchResponse.getHits();

            SearchHit[] hits = searchHits.getHits();
            CapturedPicture capturePicture;
            groupByIpc.setDeviceId(ipcId);
            picturesByIpc.add(groupByIpc);
            if (hits.length > 0) {
                for (SearchHit hit : hits) {
                    capturePicture = new CapturedPicture();
                    String surl = hit.getId();
                    String burl = captureServiceHelper.surlToBurl(surl);
                    String ipc = (String) hit.getSource().get(DynamicTable.IPCID);
                    String timestamp = (String) hit.getSource().get(DynamicTable.TIMESTAMP);
                    capturePicture.setSurl(captureServiceHelper.getFtpUrl(surl));
                    capturePicture.setBurl(captureServiceHelper.getFtpUrl(burl));
                    capturePicture.setDeviceId(ipc);
                    capturePicture.setTime(timestamp);
                    if (ipcId.equals(ipc)) {
                        capturedPictureList.add(capturePicture);
                    }
                }
            } else {
                capturePicture = new CapturedPicture();
                capturedPictureList.add(capturePicture);
            }
            captureServiceHelper.addDeviceName(capturedPictureList);
            singleResult.setTotal((int) searchHits.getTotalHits());
            singleResult.setDevicePictures(picturesByIpc);
            singleResult.setPictures(capturedPictureList);
            results.add(singleResult);
            result.setSingleResults(results);
            resultList.add(result);
        }
        return resultList;
    }

    private List<SearchResult> getCaptureHistory(SearchOption option, List<String> deviceIds, String sortParam) {
        List<SearchResult> resultList = new ArrayList<>();
        SearchResult result = new SearchResult();
        List<SingleCaptureResult> results = new ArrayList<>();
        SingleCaptureResult singleResult = new SingleCaptureResult();
        List<CapturedPicture> captureList = new ArrayList<>();

        SearchResponse searchResponse = elasticSearchDao.getCaptureHistory(option, deviceIds, sortParam);
        SearchHits searchHits = searchResponse.getHits();
        SearchHit[] hits = searchHits.getHits();
        CapturedPicture capturePicture;
        if (hits.length > 0) {
            for (SearchHit hit : hits) {
                capturePicture = new CapturedPicture();
                String surl = hit.getId();
                String burl = captureServiceHelper.surlToBurl(surl);
                String ipc = (String) hit.getSource().get(DynamicTable.IPCID);
                String timestamp = (String) hit.getSource().get(DynamicTable.TIMESTAMP);
                capturePicture.setSurl(captureServiceHelper.getFtpUrl(surl));
                capturePicture.setBurl(captureServiceHelper.getFtpUrl(burl));
                capturePicture.setDeviceId(ipc);
                capturePicture.setTime(timestamp);
            }
        }
        captureServiceHelper.addDeviceName(captureList);
        singleResult.setTotal((int) searchHits.getTotalHits());
        singleResult.setPictures(captureList);
        results.add(singleResult);
        result.setSingleResults(results);
        resultList.add(result);
        return resultList;
    }
}