package com.hzgc.service.starepo.service;

import com.hzgc.common.table.starepo.ObjectInfoTable;
import com.hzgc.service.starepo.bean.*;
import com.hzgc.service.starepo.bean.export.PersonObjectGroupByPkey;
import com.hzgc.service.starepo.bean.export.ObjectSearchResult;
import com.hzgc.service.starepo.bean.export.PersonObject;
import com.hzgc.service.starepo.bean.export.PersonSingleResult;
import com.hzgc.service.starepo.dao.PhoenixDao;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Aspect
public class ObjectInfoHandlerTool {
    @Autowired
    private PhoenixDao phoenixDao;

    void getPersonSingleResult(PersonSingleResult personSingleResult, SqlRowSet resultSet, boolean searchByPics) {
        List<PersonObject> personObjects = new ArrayList<>();
        List<String> personKey = new ArrayList<>();
        try {
            while (resultSet.next()) {
                PersonObject personObject = PersonObject.builder();
                personObject.setObjectID(resultSet.getString(ObjectInfoTable.ROWKEY));
                String pkey = resultSet.getString(ObjectInfoTable.PKEY);
                personObject.setObjectTypeKey(pkey);
                personKey.add(pkey);
                personObject.setName(resultSet.getString(ObjectInfoTable.NAME))
                        .setSex(resultSet.getInt(ObjectInfoTable.SEX))
                        .setIdcard(resultSet.getString(ObjectInfoTable.IDCARD))
                        .setCreator(resultSet.getString(ObjectInfoTable.CREATOR))
                        .setCreatorConractWay(resultSet.getString(ObjectInfoTable.CPHONE))
                        .setCreateTime(resultSet.getTimestamp(ObjectInfoTable.CREATETIME))
                        .setReason(resultSet.getString(ObjectInfoTable.REASON))
                        .setFollowLevel(resultSet.getInt(ObjectInfoTable.IMPORTANT));
                if (searchByPics) {
                    personObject.setSimilarity(resultSet.getFloat(ObjectInfoTable.RELATED));
                }
                personObjects.add(personObject);
            }
            if (personKey.size() > 0) {
                Map<String, String> typeNameMapping = phoenixDao.searchTypeNames(personKey);
                for (PersonObject personObject: personObjects) {
                    personObject.setObjectTypeName(typeNameMapping.get(personObject.getObjectTypeKey()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        personSingleResult.setObjectInfoBeans(personObjects);
        personSingleResult.setTotal(personObjects.size());
    }

    /**
     * 根据请求参数，进行分页处理
     */
    public void formatTheObjectSearchResult(ObjectSearchResult objectSearchResult, int start, int size) {
        if (objectSearchResult == null) {
            return;
        }
        List<PersonSingleResult> personSingleResults = objectSearchResult.getSingleSearchResults();

        List<PersonSingleResult> finalPersonSingleResults = new ArrayList<>();
        if (personSingleResults != null) {
            for (PersonSingleResult personSingleResult : personSingleResults) {
                List<PersonObject> personObjects = personSingleResult.getObjectInfoBeans();
                if (personObjects != null) {
                    if ((start + size) > personObjects.size()) {
                        personSingleResult.setObjectInfoBeans(personObjects.subList(start, personObjects.size()));
                    } else {
                        personSingleResult.setObjectInfoBeans(personObjects.subList(start, start + size));
                    }
                }

                List<PersonObjectGroupByPkey> personObjectGroupByPkeyList = personSingleResult.getSingleObjKeyResults();
                List<PersonObjectGroupByPkey> finaLGroupByPkeyList = new ArrayList<>();

                if (personObjectGroupByPkeyList != null) {
                    for (PersonObjectGroupByPkey personObjectGroupByPkey : personObjectGroupByPkeyList) {
                        List<PersonObject> personObjectsV1 = personObjectGroupByPkey.getPersonObjectList();
                        if (personObjectsV1 != null) {
                            if ((start + size) > personObjectsV1.size()) {
                                personObjectGroupByPkey.setPersonObjectList(personObjectsV1.subList(start, personObjectsV1.size()));
                            } else {
                                personObjectGroupByPkey.setPersonObjectList(personObjectsV1.subList(start, start + size));
                            }
                            finaLGroupByPkeyList.add(personObjectGroupByPkey);
                        }
                    }
                    personSingleResult.setSingleObjKeyResults(finaLGroupByPkeyList);
                }
                finalPersonSingleResults.add(personSingleResult);
            }
        }
        objectSearchResult.setSingleSearchResults(finalPersonSingleResults);
    }

    /**
     * 对结果进行排序
     *
     * @param personObjects    最终返回的一个人员列表
     * @param staticSortParams 排序参数
     */
    void sortPersonObject(List<PersonObject> personObjects, List<StaticSortParam> staticSortParams) {
        if (staticSortParams != null) {
            if (staticSortParams.contains(StaticSortParam.RELATEDDESC)) {
                personObjects.sort((o1, o2) -> {
                    float sim1 = o1.getSimilarity();
                    float sim2 = o2.getSimilarity();
                    return Float.compare(sim2, sim1);
                });
            }
            if (staticSortParams.contains(StaticSortParam.RELATEDASC)) {
                personObjects.sort((o1, o2) -> {
                    float sim1 = o1.getSimilarity();
                    float sim2 = o2.getSimilarity();
                    return Float.compare(sim1, sim2);
                });
            }
            if (staticSortParams.contains(StaticSortParam.IMPORTANTASC)) {
                personObjects.sort((o1, o2) -> {
                    int important1 = o1.getFollowLevel();
                    int important2 = o2.getFollowLevel();
                    return Integer.compare(important1, important2);
                });
            }
            if (staticSortParams.contains(StaticSortParam.IMPORTANTDESC)) {
                personObjects.sort((o1, o2) -> {
                    int important1 = o1.getFollowLevel();
                    int important2 = o2.getFollowLevel();
                    return Integer.compare(important2, important1);
                });
            }
            if (staticSortParams.contains(StaticSortParam.TIMEASC)) {
                personObjects.sort((o1, o2) -> {
                    Timestamp timestamp1 = o1.getCreateTime();
                    Timestamp timestamp2 = o2.getCreateTime();
                    return Long.compare(timestamp1.getTime(), timestamp2.getTime());
                });
            }
            if (staticSortParams.contains(StaticSortParam.TIMEDESC)) {
                personObjects.sort((o1, o2) -> {
                    Timestamp timestamp1 = o1.getCreateTime();
                    Timestamp timestamp2 = o2.getCreateTime();
                    return Long.compare(timestamp2.getTime(), timestamp1.getTime());
                });
            }
        }
    }

}
