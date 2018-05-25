package com.hzgc.service.dynrepo.service;

import com.hzgc.common.util.json.JSONUtil;
import com.hzgc.common.util.uuid.UuidUtil;
import com.hzgc.service.dynrepo.bean.*;
import com.hzgc.service.dynrepo.dao.HBaseDao;
import com.hzgc.service.dynrepo.dao.SparkJDBCDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class CaptureSearchService {
    @Autowired
    @SuppressWarnings("unused")
    private SparkJDBCDao sparkJDBCDao;
    @Autowired
    @SuppressWarnings("unused")
    private HBaseDao hBaseDao;
    @Autowired
    @SuppressWarnings("unused")
    private CaptureServiceHelper captureServiceHelper;

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
        if (option.getSimilarity() < 0.0) {
            log.error("Start search picture, but threshold is null");
        }
        if (option.getDeviceIds() != null && option.getDeviceIds().size() > 0) {
            captureServiceHelper.capturOptionConver(option);
        }

        log.info("Start search picture, search option is:" + JSONUtil.toJson(option));
        String searchId = UuidUtil.getUuid();
        log.info("Start search picture, generate search id and search id is:[" + searchId + "]");
        SearchCallBack searchCallBack = sparkJDBCDao.searchPicture(option);
        resultSet = searchCallBack.getResultSet();
        log.info("Start search picture, execute query total time is:" + (System.currentTimeMillis() - start));
        if (resultSet != null) {
            if (option.isSinglePerson() || option.getImages().size() == 1) {
                searchResult = captureServiceHelper.parseResultOnePerson(resultSet, option, searchId);
            } else {
                searchResult = captureServiceHelper.parseResultNotOnePerson(resultSet, option, searchId);
            }
            if (searchResult.getSingleResults().size() > 0) {
                SearchCollection collection = new SearchCollection();
                collection.setSearchOption(option);
                collection.setSearchResult(searchResult);
                boolean flag = hBaseDao.insertSearchRes(collection);
                if (flag) {
                    log.info("The search history of: [" + searchId + "] saved successful");
                } else {
                    log.error("The search history of: [" + searchId + "] saved failure");
                }
                for (SingleSearchResult singleResult : searchResult.getSingleResults()) {
                    singleResult.setPictures(captureServiceHelper.pageSplit(singleResult.getPictures(),
                            option.getStart(),
                            option.getLimit()));
                }
            }
        } else {
            log.info("Query result set is null");
        }
        sparkJDBCDao.closeConnection(searchCallBack.getConnection(), searchCallBack.getStatement());
        System.out.println(JSONUtil.toJson(searchResult));
        return searchResult;
    }

    /**
     * 获取搜索原图
     *
     * @param image_name 原图ID
     * @return 图片二进制
     */
    public byte[] getSearchPicture(String image_name) {
        if (!StringUtils.isBlank(image_name)) {
            return hBaseDao.getSearchPicture(image_name);
        }
        return null;
    }

    /**
     * 查询搜索记录
     *
     * @param start_time 历史记录起始时间
     * @param end_time   历史记录结束时间
     * @param sort       排序参数
     * @param start      起始位置
     * @param limit      返回条数
     * @return 返回查询结果
     */
    public List<SearchHisotry> getSearchHistory(String start_time, String end_time, String sort, int start, int limit) {
        if (!StringUtils.isBlank(start_time) && !StringUtils.isBlank(end_time) && !StringUtils.isBlank(sort)) {
            List<SearchHisotry> searchHisotryList = hBaseDao.getSearchHistory(start_time, end_time, sort, start, limit);
            if (start >= 0 && searchHisotryList.size() > (start + limit - 1) && limit > 0) {
                return searchHisotryList.subList(start, limit);
            } else {
                return new ArrayList<>();
            }
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * 历史搜索记录查询
     *
     * @param resultOption 历史结果查询参数对象
     * @return SearchResult对象
     */
    public SearchResult getSearchResult(SearchResultOption resultOption) {
        SearchResult searchResult = null;
        if (resultOption.getSearchId() != null && !"".equals(resultOption.getSearchId())) {
            searchResult = hBaseDao.getSearchRes(resultOption.getSearchId());
            log.info("Start query searchResult, SearchResultOption is " + JSONUtil.toJson(resultOption));
            if (searchResult != null) {
                if (resultOption.getSortParam() != null && resultOption.getSortParam().size() > 0) {
                    captureServiceHelper.sortByParamsAndPageSplit(searchResult, resultOption);
                } else {
                    for (SingleSearchResult singleSearchResult : searchResult.getSingleResults()) {
                        captureServiceHelper.pageSplit(singleSearchResult.getPictures(), resultOption);
                    }
                }
                if (resultOption.getSingleResultOptions() != null
                        && resultOption.getSingleResultOptions().size() > 0) {
                    List<SingleSearchResult> singleList = searchResult.getSingleResults();
                    List<SingleSearchResult> tempList = new ArrayList<>();
                    for (SingleSearchResult singleResult : singleList) {
                        boolean isContanis = false;
                        for (SingleResultOption singleResultOption : resultOption.getSingleResultOptions()) {
                            if (Objects.equals(((SingleSearchResult)singleResult).getSearchId(), singleResultOption.getSearchId())) {
                                isContanis = true;
                            }
                        }
                        if (!isContanis) {
                            tempList.add(singleResult);
                        }
                    }
                    singleList.removeAll(tempList);
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
