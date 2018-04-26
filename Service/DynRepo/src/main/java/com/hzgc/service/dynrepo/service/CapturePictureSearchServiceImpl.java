package com.hzgc.service.dynrepo.service;

import com.hzgc.common.service.table.column.DynamicTable;
import com.hzgc.common.util.uuid.UuidUtil;
import com.hzgc.common.util.searchtype.SearchType;
import com.hzgc.service.dynrepo.attribute.*;
import com.hzgc.common.service.connection.ElasticSearchHelper;
import com.hzgc.common.service.connection.HBaseHelper;
import com.hzgc.service.dynrepo.object.*;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 以图搜图接口实现类，内含四个方法（外）（彭聪）
 */
@Service
public class CapturePictureSearchServiceImpl implements CapturePictureSearchService {

    private static Logger LOG = Logger.getLogger(CapturePictureSearchServiceImpl.class);

    static {
        ElasticSearchHelper.getEsClient();
        HBaseHelper.getHBaseConnection();
    }

    /**
     * 以图搜图
     * 接收应用层传递的参数进行搜图，，如果大数据处理的时间过长
     * 则先返回searchId,finished=false,然后再开始计算；如果能够在秒级时间内计算完则计算完后再返回结果
     *
     * @param option 搜索选项
     * @return 搜索结果SearchResult对象
     */
    @Override
    public SearchResult search(SearchOption option) {
        long start = System.currentTimeMillis();
        SearchResult searchResult = null;
        RealTimeFaceCompareBySparkSQL realTimeFaceCompareBySparkSQL;
        if (null != option) {
            realTimeFaceCompareBySparkSQL = new RealTimeFaceCompareBySparkSQL();
            //搜索类型 是人还是车
            //设置查询Id
            String searchId = UuidUtil.setUuid();
            LOG.info("Generate current query id:[" + searchId + "]");
            //查询的对象库是人
            if (option.getImages() != null && option.getImages().size() > 0) {
                if (option.getThreshold() != 0.0) {
                    //根据上传的图片查询
                    searchResult = realTimeFaceCompareBySparkSQL.pictureSearchBySparkSQL(option, searchId);
                } else {
                    LOG.warn("The threshold is null");
                }
            } else {
                return null;
            }
        } else {
            LOG.error("Search parameter option is null");
        }
        LOG.info("Search total time is:" + (System.currentTimeMillis() - start));
        return searchResult;
    }

    /**
     * @param resultOption 历史结果查询参数对象
     * @return SearchResult对象
     */
    @Override
    public SearchResult getSearchResult(SearchResultOption resultOption) {
        SearchResult searchResult = null;
        if (resultOption.getSearchID() != null && !"".equals(resultOption.getSearchID())) {
            searchResult = DynamicPhotoServiceHelper.getSearchRes(resultOption.getSearchID());
            LOG.info("Start query history failure, SearchResultOption is " + resultOption);
            if (searchResult != null) {
                switch (searchResult.getSearchType()) {
                    case DynamicTable.PERSON_TYPE:
                        if (resultOption.getSortParam() != null && resultOption.getSortParam().size() > 0) {
                            DynamicPhotoServiceHelper.sortByParamsAndPageSplit(searchResult, resultOption);
                        } else {
                            for (SingleResult singleResult : searchResult.getResults()) {
                                DynamicPhotoServiceHelper.pageSplit(singleResult.getPictures(), resultOption);
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
                        LOG.error("No vehicle queries are currently supported");
                        break;
                    default:
                        for (SingleResult singleResult : searchResult.getResults()) {
                            DynamicPhotoServiceHelper.pageSplit(singleResult.getPictures(), resultOption);
                        }
                }
            } else {
                LOG.error("Get query history failure, SearchResultOption is " + resultOption);
            }

        } else {
            LOG.info("SearchId is null");
        }
        return searchResult;
    }

    /**
     * 人/车属性查询
     *
     * @param type 图片类型（人、车）
     * @return 属性对象列表
     */
    @Override
    public List<Attribute> getAttribute(SearchType type) {
        List<Attribute> attributeList = new ArrayList<>();
        if (type == SearchType.PERSON) {
            Attribute hairColor = new Attribute();
            hairColor.setIdentify(HairColor.class.getSimpleName());
            hairColor.setDesc("发色");
            hairColor.setLogistic(Logistic.OR);
            List<AttributeValue> hairColorValueList = new ArrayList<>();
            for (HairColor hc : HairColor.values()) {
                AttributeValue hairColorValue = new AttributeValue();
                hairColorValue.setValue(hc.ordinal());
                hairColorValue.setDesc(HairColor.getDesc(hc));
                hairColorValueList.add(hairColorValue);
            }
            hairColor.setValues(hairColorValueList);
            attributeList.add(hairColor);

            Attribute hairStyle = new Attribute();
            hairStyle.setIdentify(HairStyle.class.getSimpleName());
            hairStyle.setDesc("发型");
            hairStyle.setLogistic(Logistic.OR);
            List<AttributeValue> hairStyleValueList = new ArrayList<>();
            for (HairStyle hs : HairStyle.values()) {
                AttributeValue hairStyleValue = new AttributeValue();
                hairStyleValue.setValue(hs.ordinal());
                hairStyleValue.setDesc(HairStyle.getDesc(hs));
                hairStyleValueList.add(hairStyleValue);
            }
            hairStyle.setValues(hairStyleValueList);
            attributeList.add(hairStyle);

            Attribute gender = new Attribute();
            gender.setIdentify(Gender.class.getSimpleName());
            gender.setDesc("性别");
            gender.setLogistic(Logistic.OR);
            List<AttributeValue> genderValueList = new ArrayList<>();
            for (Gender gend : Gender.values()) {
                AttributeValue genderValue = new AttributeValue();
                genderValue.setValue(gend.ordinal());
                genderValue.setDesc(Gender.getDesc(gend));
                genderValueList.add(genderValue);
            }
            gender.setValues(genderValueList);
            attributeList.add(gender);

            Attribute hat = new Attribute();
            hat.setIdentify(Hat.class.getSimpleName());
            hat.setDesc("帽子");
            hat.setLogistic(Logistic.OR);
            List<AttributeValue> hatValueList = new ArrayList<>();
            for (Hat h : Hat.values()) {
                AttributeValue hatValue = new AttributeValue();
                hatValue.setValue(h.ordinal());
                hatValue.setDesc(Hat.getDesc(h));
                hatValueList.add(hatValue);
            }
            hat.setValues(hatValueList);
            attributeList.add(hat);

            Attribute tie = new Attribute();
            tie.setIdentify(Tie.class.getSimpleName());
            tie.setDesc("领带");
            tie.setLogistic(Logistic.OR);
            List<AttributeValue> tieValueList = new ArrayList<>();
            for (Tie t : Tie.values()) {
                AttributeValue tieValue = new AttributeValue();
                tieValue.setValue(t.ordinal());
                tieValue.setDesc(Tie.getDesc(t));
                tieValueList.add(tieValue);
            }
            tie.setValues(tieValueList);
            attributeList.add(tie);

            Attribute huzi = new Attribute();
            huzi.setIdentify(Huzi.class.getSimpleName());
            huzi.setDesc("胡子");
            huzi.setLogistic(Logistic.OR);
            List<AttributeValue> huziValueList = new ArrayList<>();
            for (Huzi hz : Huzi.values()) {
                AttributeValue huziValue = new AttributeValue();
                huziValue.setValue(hz.ordinal());
                huziValue.setDesc(Huzi.getDesc(hz));
                huziValueList.add(huziValue);
            }
            huzi.setValues(huziValueList);
            attributeList.add(huzi);

            Attribute eyeglasses = new Attribute();
            eyeglasses.setIdentify(Eyeglasses.class.getSimpleName());
            eyeglasses.setDesc("眼镜");
            eyeglasses.setLogistic(Logistic.OR);
            List<AttributeValue> eyeglassesValueList = new ArrayList<>();
            for (Eyeglasses eye : Eyeglasses.values()) {
                AttributeValue eyeglassesValue = new AttributeValue();
                eyeglassesValue.setValue(eye.ordinal());
                eyeglassesValue.setDesc(Eyeglasses.getDesc(eye));
                eyeglassesValueList.add(eyeglassesValue);
            }
            eyeglasses.setValues(eyeglassesValueList);
            attributeList.add(eyeglasses);

        } else if (type == SearchType.CAR) {
            return new ArrayList<>();
        } else {
            LOG.error("method CapturePictureSearchServiceImpl.getAttribute SearchType is error.");
        }
        return attributeList;
    }

    /**
     * 抓拍历史记录查询（陈柯）
     * 根据条件筛选抓拍图片，并返回图片对象
     *
     * @param option option中包含count、时间段、时间戳、人脸属性等值，根据这些值去筛选
     *               符合条件的图片对象并返回
     * @return List<SearchResult>符合条件的图片对象以list形式返回
     */
    @Override
    public List<SearchResult> getCaptureHistory(SearchOption option) {
        CaptureHistory captureHistory = new CaptureHistory();
        option.setSearchType(SearchType.PERSON);
        List<SortParam> sortParams = option.getSortParams();
        return captureHistory.getRowKey_history(option, sortParams);
    }
}
