package com.hzgc.service.starepo.service;

import com.hzgc.common.table.starepo.ObjectInfoTable;
import com.hzgc.jni.FaceAttribute;
import com.hzgc.jni.FaceFunction;
import com.hzgc.jni.PictureData;
import com.hzgc.service.starepo.bean.*;
import com.hzgc.service.starepo.bean.param.GetObjectInfoParam;
import com.hzgc.service.starepo.bean.param.ObjectInfoParam;
import com.hzgc.service.starepo.dao.SqlAndArgs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
public class ParseByOption {

    public String addObjectType() {
        return "upsert into " + ObjectInfoTable.TABLE_NAME
                + "( " + ObjectInfoTable.ROWKEY + ", "
                + ObjectInfoTable.TYPE_COLF + "." + ObjectInfoTable.TYPE_NAME + ", "
                + ObjectInfoTable.TYPE_COLF + "." + ObjectInfoTable.TYPE_CREATOR + ", "
                + ObjectInfoTable.TYPE_COLF + "." + ObjectInfoTable.TYPE_REMARK + ", "
                + ObjectInfoTable.TYPE_COLF + "." + ObjectInfoTable.TYPE_ADD_TIME
                + ") values (?,?,?,?,?)";
    }

    public String deleteObjectType_select() {
        return "select id from " + ObjectInfoTable.TABLE_NAME + " where " + ObjectInfoTable.PKEY + " = ?";
    }

    public String deleteObjectType_delete() {
        return "delete from " + ObjectInfoTable.TABLE_NAME + " where " + ObjectInfoTable.ROWKEY + " = ?";
    }

    public String updateObjectType() {
        return "upsert into " + ObjectInfoTable.TABLE_NAME
                + "( " + ObjectInfoTable.ROWKEY + ", "
                + ObjectInfoTable.TYPE_COLF + "." + ObjectInfoTable.TYPE_NAME + ", "
                + ObjectInfoTable.TYPE_COLF + "." + ObjectInfoTable.TYPE_CREATOR + ", "
                + ObjectInfoTable.TYPE_COLF + "." + ObjectInfoTable.TYPE_REMARK + ", "
                + ObjectInfoTable.TYPE_COLF + "." + ObjectInfoTable.TYPE_UPDATE_TIME
                + ") values (?,?,?,?,?)";
    }

    public String searchObjectType() {
        String sql = "select " + ObjectInfoTable.ROWKEY + ", "
                + ObjectInfoTable.TYPE_COLF + "." + ObjectInfoTable.TYPE_NAME + ", "
                + ObjectInfoTable.TYPE_COLF + "." + ObjectInfoTable.TYPE_CREATOR + ", "
                + ObjectInfoTable.TYPE_COLF + "." + ObjectInfoTable.TYPE_REMARK + ", "
                + ObjectInfoTable.TYPE_COLF + "." + ObjectInfoTable.TYPE_ADD_TIME
                + " from " + ObjectInfoTable.TABLE_NAME + " where " + ObjectInfoTable.ROWKEY + " > ?" + " LIMIT ?";
        return sql;
    }

    public SqlAndArgs getSqlFromGetObjectInfoParm(GetObjectInfoParam param) {
        StringBuilder sql = new StringBuilder();
        List<Object> setValues = new ArrayList<>();
        List<PictureData> pictureDataList = param.getPictureDataList();
        List<StaticSortParam> params = param.getSortParamList();
        sql.append("select ");
        if (pictureDataList != null && pictureDataList.size() != 0) {
            // 最终需要返回的内容
            sql.append(sameFieldNeedReturn());
            if (param.isSinglePerson() && pictureDataList.size() == 1) {
                sql.append(", sim as sim")
                        .append(" from (select ")
                        .append(sameFieldNeedReturn())
                        .append(", FACECOMP(")
                        .append(ObjectInfoTable.PERSON_COLF).append(".").append(ObjectInfoTable.FEATURE)
                        .append(", ?");
                StringBuilder featureString = new StringBuilder();
                int size = pictureDataList.size();
                int count = 1;
                for (PictureData picture : pictureDataList) {
                    FaceAttribute faceAttribute = picture.getFeature();
                    if (faceAttribute != null) {
                        if (count == size) {
                            featureString.append(FaceFunction.floatArray2string(faceAttribute.getFeature()));
                        } else {
                            featureString.append(FaceFunction.floatArray2string(faceAttribute.getFeature())).append(",");
                        }
                    }
                    count++;
                }
                setValues.add(new String(featureString));
                sql.append(") as sim from ")
                        .append(ObjectInfoTable.TABLE_NAME)
                        .append(" where ")
                        .append(sameWhereSql(param, setValues))
                        .append(")")
                        .append(" where sim > ? ");
                setValues.add(param.getSimilarity());

                sql.append(" order by ")
                        .append(sameSortSql(params, true));
            } else if (!param.isSinglePerson() && pictureDataList.size() != 1) {
                sql.append(", type")
                        .append(", sim")
                        .append(" from (");

                // 拼装子sql
                List<StringBuffer> subSqls = new ArrayList<>();
                for (PictureData photo : pictureDataList) {
                    String key = photo.getImageID();
                    FaceAttribute faceAttribute = photo.getFeature();
                    float[] feature = null;
                    if (faceAttribute != null && faceAttribute.getFeature() != null) {
                        feature = faceAttribute.getFeature();
                    }
                    StringBuffer subSql = new StringBuffer("");
                    subSql.append("select ")
                            .append(sameFieldNeedReturn())
                            .append(", ? as type");
                    setValues.add(key);
                    subSql.append(", FACECOMP(")
                            .append(ObjectInfoTable.PERSON_COLF).append(".")
                            .append(ObjectInfoTable.FEATURE)
                            .append(", ?");
                    setValues.add(FaceFunction.floatArray2string(feature));
                    subSql.append(") as sim from ")
                            .append(ObjectInfoTable.TABLE_NAME)
                            .append(" where ")
                            .append(sameWhereSql(param, setValues));
                    subSqls.add(subSql);
                }

                //完成子sql 的拼装
                sql.append(subSqls.get(0));
                for (int i = 1; i < subSqls.size(); i++) {
                    sql.append(" union all ");
                    sql.append(subSqls.get(i));
                }

                sql.append(") where sim > ? ");
                setValues.add(param.getSimilarity());
                //排序sql 拼装
                if (params != null && params.size() != 0) {
                    sql.append(" order by type,");
                }
                sql.append(sameSortSql(params, true));
            } else {
                log.info("Start get object info, param is error");
                return null;
            }
        } else {
            sql.append(sameFieldNeedReturn())
                    .append(" from ")
                    .append(ObjectInfoTable.TABLE_NAME)
                    .append(" where ")
                    .append(sameWhereSql(param, setValues));
            if (params != null) {
                sql.append(" order by ")
                        .append(sameSortSql(params, false));
            }
        }
        // 进行分组
        SqlAndArgs sqlAndArgs = new SqlAndArgs();
        sqlAndArgs.setArgs(setValues);
        sqlAndArgs.setSql(sql.toString());
        return sqlAndArgs;
    }

    /**
     * 返回排序sql 自句
     *
     * @param params        排序参数
     * @param serarchByPics 是否有图片
     * @return 返回排序参数语句
     */
    private StringBuilder sameSortSql(List<StaticSortParam> params, boolean serarchByPics) {
        StringBuilder sameSortSql = new StringBuilder();
        if (params != null) {
            int count = 0;
            if (serarchByPics) {
                if (params.contains(StaticSortParam.RELATEDASC)) {
                    sameSortSql.append("sim asc");
                    if (params.size() > 1) {
                        sameSortSql.append(", ");
                    }
                }
                if (params.contains(StaticSortParam.RELATEDDESC)) {
                    sameSortSql.append("sim desc");
                    if (params.size() > 1) {
                        sameSortSql.append(", ");
                    }
                }

            }
            if (params.contains(StaticSortParam.IMPORTANTASC)) {
                sameSortSql.append(ObjectInfoTable.PERSON_COLF).append(".").append(ObjectInfoTable.IMPORTANT);
                sameSortSql.append(" asc");
                count++;
            }
            if (params.contains(StaticSortParam.IMPORTANTDESC)) {
                sameSortSql.append(ObjectInfoTable.PERSON_COLF).append(".").append(ObjectInfoTable.IMPORTANT);
                sameSortSql.append(" desc");
                count++;
            }
            if (params.contains(StaticSortParam.TIMEASC)) {
                if (count > 0) {
                    sameSortSql.append(", ");
                    sameSortSql.append(ObjectInfoTable.PERSON_COLF).append(".").append(ObjectInfoTable.CREATETIME);
                    sameSortSql.append(" asc");
                } else {
                    sameSortSql.append(ObjectInfoTable.PERSON_COLF).append(".").append(ObjectInfoTable.CREATETIME);
                    sameSortSql.append(" asc");
                }
            }
            if (params.contains(StaticSortParam.TIMEDESC)) {
                if (count > 0) {
                    sameSortSql.append(", ");
                    sameSortSql.append(ObjectInfoTable.PERSON_COLF).append(".").append(ObjectInfoTable.CREATETIME);
                    sameSortSql.append(" desc");
                } else {
                    sameSortSql.append(ObjectInfoTable.PERSON_COLF).append(".").append(ObjectInfoTable.CREATETIME);
                    sameSortSql.append(" desc");
                }
            }
        }
        return sameSortSql;
    }

    /**
     * @return 不同情况下需要返回的相同的字段
     */
    private StringBuffer sameFieldNeedReturn() {
        StringBuffer sameFieldReturn = new StringBuffer("");
        sameFieldReturn.append(ObjectInfoTable.ROWKEY);
        sameFieldReturn.append(", ");
        sameFieldReturn.append(ObjectInfoTable.PERSON_COLF).append(".").append(ObjectInfoTable.PKEY);
        sameFieldReturn.append(", ");
        sameFieldReturn.append(ObjectInfoTable.PERSON_COLF).append(".").append(ObjectInfoTable.NAME);
        sameFieldReturn.append(", ");
        sameFieldReturn.append(ObjectInfoTable.PERSON_COLF).append(".").append(ObjectInfoTable.SEX);
        sameFieldReturn.append(", ");
        sameFieldReturn.append(ObjectInfoTable.PERSON_COLF).append(".").append(ObjectInfoTable.IDCARD);
        sameFieldReturn.append(", ");
        sameFieldReturn.append(ObjectInfoTable.PERSON_COLF).append(".").append(ObjectInfoTable.CREATOR);
        sameFieldReturn.append(", ");
        sameFieldReturn.append(ObjectInfoTable.PERSON_COLF).append(".").append(ObjectInfoTable.CPHONE);
        sameFieldReturn.append(", ");
        sameFieldReturn.append(ObjectInfoTable.PERSON_COLF).append(".").append(ObjectInfoTable.CREATETIME);
        sameFieldReturn.append(", ");
        sameFieldReturn.append(ObjectInfoTable.PERSON_COLF).append(".").append(ObjectInfoTable.UPDATETIME);
        sameFieldReturn.append(", ");
        sameFieldReturn.append(ObjectInfoTable.PERSON_COLF).append(".").append(ObjectInfoTable.REASON);
        sameFieldReturn.append(", ");
        sameFieldReturn.append(ObjectInfoTable.PERSON_COLF).append(".").append(ObjectInfoTable.IMPORTANT);
        sameFieldReturn.append(", ");
        sameFieldReturn.append(ObjectInfoTable.PERSON_COLF).append(".").append(ObjectInfoTable.STATUS);
        sameFieldReturn.append(", ");
        sameFieldReturn.append(ObjectInfoTable.PERSON_COLF).append(".").append(ObjectInfoTable.LOCATION);
        return sameFieldReturn;
    }


    /**
     * 封装共同的子where 查询
     *
     * @param param       传过来的搜索参数
     * @param setArgsList 需要对sql 设置的值
     * @return 子where查询
     */
    private StringBuilder sameWhereSql(GetObjectInfoParam param, List<Object> setArgsList) {
        StringBuilder whereQuery = new StringBuilder();
        boolean isChanged = false;

        // 关于姓名的搜索
        if (!StringUtils.isBlank(param.getObjectName())) {
            whereQuery.append(ObjectInfoTable.PERSON_COLF)
                    .append(".")
                    .append(ObjectInfoTable.NAME).append(" = ?");
            setArgsList.add(param.getObjectName());
            isChanged = true;
        }

        // 关于身份证号的查询
        if (!StringUtils.isBlank(param.getIdcard())) {
            if (isChanged) {
                whereQuery.append(" and ");
            }
            whereQuery.append(ObjectInfoTable.PERSON_COLF)
                    .append(".").
                    append(ObjectInfoTable.IDCARD)
                    .append(" = ?");
            setArgsList.add(param.getIdcard());
            isChanged = true;
        }

        // 关于性别的查询
        if (param.getSex() != 0) {
            if (isChanged) {
                whereQuery.append(" and ");
            }
            whereQuery.append(ObjectInfoTable.PERSON_COLF)
                    .append(".")
                    .append(ObjectInfoTable.SEX)
                    .append(" = ?");
            setArgsList.add(param.getSex());
            isChanged = true;
        }

        // 关于人员类型列表的查询
        List<String> pkeys = param.getObjectTypeKeyList();
        if (pkeys != null) {
            if (pkeys.size() == 1) {
                if (isChanged) {
                    whereQuery.append(" and ").append(ObjectInfoTable.PERSON_COLF)
                            .append(".")
                            .append(ObjectInfoTable.PKEY)
                            .append(" = ? ");
                    setArgsList.add(pkeys.get(0));
                } else {
                    whereQuery.append(ObjectInfoTable.PERSON_COLF)
                            .append(".")
                            .append(ObjectInfoTable.PKEY)
                            .append(" = ? ");
                    setArgsList.add(pkeys.get(0));
                    isChanged = true;
                }
            } else {
                for (int i = 0; i < pkeys.size(); i++) {
                    if (i == pkeys.size() - 1) {
                        whereQuery.append(" or ");
                        whereQuery.append(ObjectInfoTable.PERSON_COLF)
                                .append(".")
                                .append(ObjectInfoTable.PKEY)
                                .append(" = ?)");
                        setArgsList.add(pkeys.get(i));
                    } else if (i == 0) {
                        if (isChanged) {
                            whereQuery.append(" and (");
                        }
                        whereQuery.append(ObjectInfoTable.PERSON_COLF)
                                .append(".")
                                .append(ObjectInfoTable.PKEY)
                                .append(" = ?");
                        setArgsList.add(pkeys.get(i));
                        isChanged = true;
                    } else {
                        whereQuery.append(" or ")
                                .append(ObjectInfoTable.PERSON_COLF)
                                .append(".")
                                .append(ObjectInfoTable.PKEY)
                                .append(" = ?");
                        setArgsList.add(pkeys.get(i));
                    }
                }
            }
        }

        // 关于创建人姓名的查询
        String creator = param.getCreator();
        if (!StringUtils.isBlank(creator)) {
            if (isChanged) {
                whereQuery.append(" and ");
            }
            whereQuery.append(ObjectInfoTable.PERSON_COLF)
                    .append(".")
                    .append(ObjectInfoTable.CREATOR)
                    .append(" = ?");
            setArgsList.add(creator);
            isChanged = true;
        }

        // 关于布控人手机号的查询
        if (!StringUtils.isBlank(param.getCreatorConractWay()))

        {
            if (isChanged) {
                whereQuery.append(" and ");
            }
            whereQuery.append(ObjectInfoTable.PERSON_COLF)
                    .append(".")
                    .append(ObjectInfoTable.CPHONE)
                    .append(" = ?");
            setArgsList.add(param.getCreatorConractWay());
            isChanged = true;
        }

        // 关于是否是重点人员的查询
        if (isChanged) {
            whereQuery.append(" and ");
        }
        whereQuery.append(ObjectInfoTable.PERSON_COLF)
                .append(".")
                .append(ObjectInfoTable.IMPORTANT);
        whereQuery.append(" = ?");
        setArgsList.add(param.getFollowLevel());

        // 属于人员状态，建议迁入和常住人口的查询
        whereQuery.append(" and ");
        whereQuery.append(ObjectInfoTable.PERSON_COLF)
                .append(".")
                .append(ObjectInfoTable.STATUS);
        whereQuery.append(" = ?");
        setArgsList.add(param.getStatus());

        // 人员位置搜索，location
        String location = param.getLocation();
        if (!StringUtils.isBlank(location))

        {
            whereQuery.append(" and ");
            whereQuery.append(ObjectInfoTable.PERSON_COLF).append(".").append(ObjectInfoTable.LOCATION);
            whereQuery.append(" = ?");
            setArgsList.add(location);
        }
        return whereQuery;
    }

    /**
     * 根据传过来的person的封装的数据Map，进行生成一个sql,用来进行插入和更新
     *
     * @param objectInfo 需要更新的数据
     * @return 拼装成的sql 以及需要设置的值
     */
    public ConcurrentHashMap<String, CopyOnWriteArrayList<Object>> getUpdateSqlFromObjectInfo(ObjectInfoParam objectInfo) {
        CopyOnWriteArrayList<Object> setValues = new CopyOnWriteArrayList<>();
        StringBuffer sql = new StringBuffer("");
        sql.append("upsert into ");
        sql.append(ObjectInfoTable.TABLE_NAME);
        sql.append("(");
        sql.append(ObjectInfoTable.ROWKEY);
        setValues.add(objectInfo.getId());

        String name = objectInfo.getName();
        if (name != null) {
            sql.append(", ");
            sql.append(ObjectInfoTable.NAME);
            setValues.add(name);
        }
        String pkey = objectInfo.getObjectTypeKey();
        if (pkey != null) {
            sql.append(", ");
            sql.append(ObjectInfoTable.PKEY);
            setValues.add(pkey);
        }
        String idcard = objectInfo.getIdcard();
        if (idcard != null) {
            sql.append(", ");
            sql.append(ObjectInfoTable.IDCARD);
            setValues.add(idcard);
        }

        int sex = objectInfo.getSex();
        if (sex != 0) {
            sql.append(", ");
            sql.append(ObjectInfoTable.SEX);
            setValues.add(sex);
        }

        String reason = objectInfo.getReason();
        if (reason != null) {
            sql.append(", ");
            sql.append(ObjectInfoTable.REASON);
            setValues.add(reason);
        }
        String creator = objectInfo.getCreator();
        if (creator != null) {
            sql.append(", ");
            sql.append(ObjectInfoTable.CREATOR);
            setValues.add(creator);
        }
        String cphone = objectInfo.getCreatorConractWay();
        if (cphone != null) {
            sql.append(", ");
            sql.append(ObjectInfoTable.CPHONE);
            setValues.add(cphone);
        }

        int important = objectInfo.getFollowLevel();
        if (important != 0) {
            sql.append(", ");
            sql.append(ObjectInfoTable.IMPORTANT);
            setValues.add(important);
        }

        int status = objectInfo.getStatus();
        if (status != 0) {
            sql.append(", ");
            sql.append(ObjectInfoTable.STATUS);
            setValues.add(status);
        }

        sql.append(") values(?");
        StringBuilder tmp = new StringBuilder("");
        for (int i = 0; i <= setValues.size() - 2; i++) {
            tmp.append(", ?");
        }
        sql.append(tmp);
        sql.append(")");
        ConcurrentHashMap<String, CopyOnWriteArrayList<Object>> sqlAndSetValues = new ConcurrentHashMap<>();
        sqlAndSetValues.put(new String(sql), setValues);
        return sqlAndSetValues;
    }

    public String getPhotoByObjectId() {
        return "select " + ObjectInfoTable.PERSON_COLF + "." + ObjectInfoTable.PHOTO
                + " from " + ObjectInfoTable.TABLE_NAME + " where " + ObjectInfoTable.ROWKEY + " = ?";
    }

    public String getTypeNameMapping(List<String> objectTypeKeys) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ")
                .append(ObjectInfoTable.TYPE_COLF)
                .append(".")
                .append(ObjectInfoTable.TYPE_NAME)
                .append(", ")
                .append(ObjectInfoTable.ROWKEY)
                .append(" from ")
                .append(ObjectInfoTable.TABLE_NAME)
                .append(" where ")
                .append(ObjectInfoTable.ROWKEY)
                .append(" in (");
        int count = 0;
        for (String ignored : objectTypeKeys) {
            sql.append("'")
                    .append(ignored)
                    .append("'");
            count++;
            if (count == objectTypeKeys.size()) {
                sql.append(")");
            } else {
                sql.append(", ");
            }
        }
        return sql.toString();
    }

    public String addObjectInfo(ObjectInfoParam objectInfo) {
        return "upsert into " + ObjectInfoTable.TABLE_NAME + "("
                + ObjectInfoTable.ROWKEY + ", "
                + ObjectInfoTable.PERSON_COLF + "." + ObjectInfoTable.NAME + ", "
                + ObjectInfoTable.PERSON_COLF + "." + ObjectInfoTable.PKEY + ", "
                + ObjectInfoTable.PERSON_COLF + "." + ObjectInfoTable.IDCARD + ", "
                + ObjectInfoTable.PERSON_COLF + "." + ObjectInfoTable.SEX + ", "
                + ObjectInfoTable.PERSON_COLF + "." + ObjectInfoTable.PHOTO + ", "
                + ObjectInfoTable.PERSON_COLF + "." + ObjectInfoTable.FEATURE + ", "
                + ObjectInfoTable.PERSON_COLF + "." + ObjectInfoTable.REASON + ", "
                + ObjectInfoTable.PERSON_COLF + "." + ObjectInfoTable.CREATOR + ", "
                + ObjectInfoTable.PERSON_COLF + "." + ObjectInfoTable.CPHONE + ", "
                + ObjectInfoTable.PERSON_COLF + "." + ObjectInfoTable.CREATETIME + ", "
                + ObjectInfoTable.PERSON_COLF + "." + ObjectInfoTable.UPDATETIME + ", "
                + ObjectInfoTable.PERSON_COLF + "." + ObjectInfoTable.IMPORTANT + ", "
                + ObjectInfoTable.PERSON_COLF + "." + ObjectInfoTable.STATUS
                + ") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    }

    public String deleteObjectInfo(List<String> rowkeys) {
        return "delete from " + ObjectInfoTable.TABLE_NAME + " where " + ObjectInfoTable.ROWKEY + " = ?";
    }

    public String getOBjectTypeByObjectId(String objectId) {
        return "select " + ObjectInfoTable.ROWKEY +
                ", " +
                ObjectInfoTable.TYPE_COLF + "." + ObjectInfoTable.TYPE_CREATOR +
                ", " +
                ObjectInfoTable.TYPE_COLF + "." + ObjectInfoTable.TYPE_REMARK +
                " from " +
                ObjectInfoTable.TABLE_NAME +
                " where " + ObjectInfoTable.ROWKEY + " = '" + objectId + "'";
    }

    public String getPictureData() {
        return "select " + ObjectInfoTable.PERSON_COLF + "." + ObjectInfoTable.PHOTO + ","
                + ObjectInfoTable.PERSON_COLF + "." + ObjectInfoTable.FEATURE
                + " from " + ObjectInfoTable.TABLE_NAME + " where id = ?";
    }

    public String countStatus() {
        return "select count(*) as num from " + ObjectInfoTable.TABLE_NAME + " where "
                + ObjectInfoTable.PERSON_COLF + "." + ObjectInfoTable.STATUS + " = 0";
    }

    public String objectTypeCount() {
        return "select count(" + ObjectInfoTable.TYPE_COLF + "." + ObjectInfoTable.TYPE_NAME
                + ") as num from " + ObjectInfoTable.TABLE_NAME;
    }

}
