package com.hzgc.service.dynrepo.service;

import com.hzgc.common.service.table.column.DynamicTable;
import com.hzgc.common.util.json.JSONUtil;
import com.hzgc.common.util.uuid.UuidUtil;
import com.hzgc.service.dynrepo.bean.*;
import com.hzgc.service.dynrepo.dao.HBaseDao;
import com.hzgc.service.dynrepo.dao.SparkJDBCDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.hzgc.service.dynrepo.service.CaptureServiceHelper.parseResultNotOnePerson;
import static com.hzgc.service.dynrepo.service.CaptureServiceHelper.parseResultOnePerson;

@Service
@Slf4j
public class CaptureSearchService {
    @Autowired
    private SparkJDBCDao sparkJDBCDao;
    @Autowired
    private HBaseDao hBaseDao;

    public SearchResult searchPicture(SearchOption option) throws SQLException {
        SearchResult searchResult = null;
        ResultSet resultSet;
        long start = System.currentTimeMillis();
        if (option == null) {
            log.error("Start search picture, but search option is null");
            return new SearchResult();
        }
        if (option.getImages() == null && option.getImages().size() < 1) {
            log.error("Start search picture, but images is null");
            return new SearchResult();
        }
        if (option.getThreshold() < 0.0) {
            log.error("Start search picture, but threshold is null");
        }

        log.info("Start search picture, search option is:" + JSONUtil.toJson(option));
        String searchId = UuidUtil.setUuid();
        log.info("Start search picture, generate search id and search id is:[" + searchId + "]");
        resultSet = sparkJDBCDao.searchPicture(option);
        log.info("Start search picture, execute query total time is:" + (System.currentTimeMillis() - start));
        if (resultSet != null) {
            if (option.isOnePerson() || option.getImages().size() == 1) {
                searchResult = parseResultOnePerson(resultSet, option, searchId);
            } else {
                searchResult = parseResultNotOnePerson(resultSet, option, searchId);
            }
            searchResult.setSearchType(DynamicTable.PERSON_TYPE);
            if (searchResult.getResults().size() > 0) {
                boolean flag = hBaseDao.insertSearchRes(searchResult);
                if (flag) {
                    log.info("The search history of: [" + searchId + "] saved successful");
                } else {
                    log.error("The search history of: [" + searchId + "] saved failure");
                }
                for (SingleResult singleResult : searchResult.getResults()) {
                    singleResult.setPictures(CaptureServiceHelper.pageSplit(singleResult.getPictures(),
                            option.getOffset(),
                            option.getCount()));
                }
            }
        } else {
            log.info("Query result set is null");
        }
        return searchResult;
    }

    /**
     * 历史搜索记录查询
     *
     * @param resultOption 历史结果查询参数对象
     * @return SearchResult对象
     */
    public SearchResult getSearchResult(SearchResultOption resultOption) {
        SearchResult searchResult = null;
        if (resultOption.getSearchID() != null && !"".equals(resultOption.getSearchID())) {
            searchResult = hBaseDao.getSearchRes(resultOption.getSearchID());
            log.info("Start query searchResult, SearchResultOption is " + JSONUtil.toJson(resultOption));
            if (searchResult != null) {
                switch (searchResult.getSearchType()) {
                    case DynamicTable.PERSON_TYPE:
                        if (resultOption.getSortParam() != null && resultOption.getSortParam().size() > 0) {
                            CaptureServiceHelper.sortByParamsAndPageSplit(searchResult, resultOption);
                        } else {
                            for (SingleResult singleResult : searchResult.getResults()) {
                                CaptureServiceHelper.pageSplit(singleResult.getPictures(), resultOption);
                            }
                        }
                        if (resultOption.getSingleResultOptions() != null
                                && resultOption.getSingleResultOptions().size() > 0) {
                            List<SingleResult> singleList = searchResult.getResults();
                            List<SingleResult> tempList = new ArrayList<>();
                            for (SingleResult singleResult : singleList) {
                                boolean isContanis = false;
                                for (SingleResultOption singleResultOption : resultOption.getSingleResultOptions()) {
                                    if (Objects.equals(singleResult.getId(), singleResultOption.getId())) {
                                        isContanis = true;
                                    }
                                }
                                if (!isContanis) {
                                    tempList.add(singleResult);
                                }
                            }
                            singleList.removeAll(tempList);
                        }
                        break;
                    case DynamicTable.CAR_TYPE:
                        log.error("No vehicle queries are currently supported");
                        break;
                    default:
                        for (SingleResult singleResult : searchResult.getResults()) {
                            CaptureServiceHelper.pageSplit(singleResult.getPictures(), resultOption);
                        }
                }
            } else {
                log.error("Get query history failure, SearchResultOption is " + resultOption);
            }

        } else {
            log.info("SearchId is null");
        }
        return searchResult;
    }

}
