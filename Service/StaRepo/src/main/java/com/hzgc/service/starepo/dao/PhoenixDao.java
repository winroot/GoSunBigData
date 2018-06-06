package com.hzgc.service.starepo.dao;

import com.hzgc.common.table.dynrepo.SearchRecordTable;
import com.hzgc.common.table.starepo.ObjectInfoTable;
import com.hzgc.common.table.starepo.ObjectTypeTable;
import com.hzgc.common.util.empty.IsEmpty;
import com.hzgc.common.util.uuid.UuidUtil;
import com.hzgc.jni.FaceAttribute;
import com.hzgc.jni.PictureData;
import com.hzgc.service.starepo.bean.export.PersonSingleResult;
import com.hzgc.service.starepo.bean.param.GetObjectInfoParam;
import com.hzgc.service.starepo.bean.param.ObjectInfoParam;
import com.hzgc.service.starepo.bean.param.ObjectTypeParam;
import com.hzgc.service.starepo.service.ParseByOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.Serializable;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
@Slf4j
public class PhoenixDao implements Serializable {

    @Resource(name = "phoenixJdbcTemplate")
    @SuppressWarnings("unused")
    private JdbcTemplate jdbcTemplate;
    @Autowired
    @SuppressWarnings("unused")
    private ParseByOption parseByOption;

    /**
     * 添加 objectType
     *
     * @param name    对象名称
     * @param creator 创建者
     * @param remark  备注
     * @return boolean
     */
    public boolean addObjectType(String name, String creator, String remark) {
        log.info("Add objectType param : name=" + name + "; creator=" + creator + "; remark=" + remark + ".");
        if (!IsEmpty.strIsRight(name)) {
            log.info("Add objectType param ：name is null.");
            return false;
        }
        long start = System.currentTimeMillis();
        String typeId = "type_" + start + UuidUtil.getUuid().substring(0, 8);
        log.info("Add objectType id : " + typeId);
        String sql = parseByOption.addObjectType();
        log.info("Add objectType SQL : " + sql);
        try {
            jdbcTemplate.update(sql, typeId, name, creator, remark, new Timestamp(System.currentTimeMillis()));
        } catch (Exception e) {
            log.info("Add objectType failed!");
            log.error(e.getMessage());
            return false;
        }
        log.info("Add objectType takes time ：" + (System.currentTimeMillis() - start));
        return true;
    }

    /**
     * 删除objectType
     *
     * @param objectTypeKeyList 类型Key列表
     * @return boolean
     */
    public boolean deleteObjectType(List<String> objectTypeKeyList) {
        log.info("Delete objectType param : List<id> = " + objectTypeKeyList.toString());
        String sql_select = parseByOption.deleteObjectType_select();
        String sql_delete = parseByOption.deleteObjectType_delete();
        try {
            for (String id : objectTypeKeyList) {
                SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql_select, id);
                if (sqlRowSet.next()) {
                    log.info("此 objectType : id = [" + id + "]下存在有 objectInfo ;不能删除此对象类型!");
                    return false;
                } else {
                    jdbcTemplate.update(sql_delete, id);
                }
            }
        } catch (Exception e) {
            log.info("Delete objectType failed!");
            log.error(e.getMessage());
            return false;
        }
        log.info("Delete List<objectType> = [" + objectTypeKeyList.toString() + "]");
        return true;
    }

    /**
     * 修改ObjectType
     *
     * @param id      objectKey
     * @param name    objectName
     * @param creator 创建者
     * @param remark  备  注
     * @return boolean
     */
    public boolean updateObjectType(String id, String name, String creator, String remark) {
        log.info("Update objectType param : id=" + id + "; name=" + name + "; creator=" + creator + "; remark=" + remark + ".");
        if (id == null || "".equals(id)) {
            log.info("Update objectType param ：id is null.");
            return false;
        }
        if (name == null || "".equals(name)) {
            log.info("Update objectType param ：name is null.");
            return false;
        }
        if (!IsEmpty.strIsRight(creator) || !IsEmpty.strIsRight(remark)) {
            log.info("Update objectType param ：creator or remark is null, the database needs to be queried!");
            ObjectTypeParam objectType = getObjectTypeByObjectId(id);
            if (!IsEmpty.strIsRight(creator)) {
                creator = objectType.getCreator();
                log.info("Update objectType param ：creator is null, Query Database set creator = " + creator);
            }
            if (!IsEmpty.strIsRight(remark)) {
                remark = objectType.getRemark();
                log.info("Update objectType param ：remark is null, Query Database set remark = " + remark);
            }
        }
        String sql = parseByOption.updateObjectType();
        log.info("Update objectType SQL : " + sql);
        try {
            jdbcTemplate.update(sql, id, name, creator, remark, new Timestamp(System.currentTimeMillis()));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        log.info("Update objectType successful");
        return true;
    }

    /**
     * 查询单个objectType(创建人、备注)  (内)
     *
     * @param objectId 对象类型Key
     * @return ObjectTypeParam
     */
    private ObjectTypeParam getObjectTypeByObjectId(String objectId) {
        String sql = parseByOption.getOBjectTypeByObjectId(objectId);
        ObjectTypeParam objectType = new ObjectTypeParam();
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);
        while (sqlRowSet.next()) {
            objectType.setCreator(sqlRowSet.getString(ObjectTypeTable.TYPE_CREATOR));
            objectType.setRemark(sqlRowSet.getString(ObjectTypeTable.TYPE_REMARK));
        }
        log.info("Get objectType by rowkey result: " + objectType.toString());
        return objectType;
    }

    /**
     * 查询objectTypeName
     *
     * @param personKey 对象类型列表
     * @return Map
     */
    public Map<String, String> searchTypeNames(List<String> personKey) {
        log.info("Search object types:" + Arrays.toString(personKey.toArray()));
        Map<String, String> map = new HashMap<>();
        String sql = parseByOption.getTypeNameMapping(personKey);
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);
        while (sqlRowSet.next()) {
            String name = sqlRowSet.getString(ObjectTypeTable.TYPE_NAME);
            String typeKey = sqlRowSet.getString(ObjectTypeTable.ROWKEY);
            map.put(typeKey, name);
        }
        return map;
    }

    /**
     * 查询objectType
     *
     * @param start 查询起始位置
     * @param limit 查询数量
     * @return List<ObjectTypeParam>
     */
    public List<ObjectTypeParam> searchObjectType(int start, int limit) {
        log.info("Search objectType Param : start=" + start + "; limit=" + limit + ".");
        String sql = parseByOption.searchObjectType();
        log.info("Search objectType SQL: " + sql);
        List<ObjectTypeParam> result = null;
        SqlRowSet sqlRowSet;
        try {
            if (start == 1) {
                sqlRowSet = jdbcTemplate.queryForRowSet(sql, "0", limit);
                result = getResult(sqlRowSet);
            } else {
                sqlRowSet = jdbcTemplate.queryForRowSet(sql, "0", start - 1);
                String lastRowkey = getLastRowkey(sqlRowSet);
                sqlRowSet = jdbcTemplate.queryForRowSet(sql, lastRowkey, limit);
                result = getResult(sqlRowSet);
            }
        } catch (SQLException e) {
            log.info("Search objectType failed!");
            log.error(e.getMessage());
            e.printStackTrace();
        }
        log.info("Search objectType result: " + (result != null ? result.toString() : null));
        return result;
    }

    /**
     * 查询objectType （内部方法）
     */
    private List<ObjectTypeParam> getResult(SqlRowSet sqlRowSet) throws SQLException {
        List<ObjectTypeParam> result = new ArrayList<>();
        while (sqlRowSet.next()) {
            ObjectTypeParam objectTypeParam = new ObjectTypeParam();
            objectTypeParam.setObjectTypeKey(sqlRowSet.getString(ObjectTypeTable.ROWKEY));
            objectTypeParam.setObjectTypeName(sqlRowSet.getString(ObjectTypeTable.TYPE_NAME));
            objectTypeParam.setCreator(sqlRowSet.getString(ObjectTypeTable.TYPE_CREATOR));
            objectTypeParam.setRemark(sqlRowSet.getString(ObjectTypeTable.TYPE_REMARK));
            result.add(objectTypeParam);
        }
        return result;
    }

    /**
     * 查询objectType （内部方法）
     */
    private String getLastRowkey(SqlRowSet sqlRowSet) throws SQLException {
        String lastRowKey = null;
        while (sqlRowSet.next()) {
            lastRowKey = sqlRowSet.getString(ObjectTypeTable.ROWKEY);
        }
        return lastRowKey;
    }

    /**
     * 针对单个对象信息的添加处理  （外）
     *
     * @return 返回值为0，表示插入成功，返回值为1，表示插入失败
     */
    public Integer addObjectInfo(ObjectInfoParam objectInfo) {
        String sql = parseByOption.addObjectInfo(objectInfo);
        try {
            Timestamp createTime = new Timestamp(System.currentTimeMillis());
            if (objectInfo.getPictureDatas().getFeature() != null) {
                float[] in = objectInfo.getPictureDatas().getFeature().getFeature();
                Object[] out = new Object[in.length];
                for (int i = 0; i < in.length; i++) {
                    out[i] = in[i];
                }
                //生成phoenix识别的float数组
                Connection connection = jdbcTemplate.getDataSource().getConnection();
                Array sqlArray = connection.createArrayOf("FLOAT", out);
                connection.close();
                jdbcTemplate.update(sql,
                        objectInfo.getId(),
                        objectInfo.getName(),
                        objectInfo.getObjectTypeKey(),
                        objectInfo.getIdcard(),
                        objectInfo.getSex(),
                        objectInfo.getPictureDatas().getImageData(),
                        sqlArray,
                        objectInfo.getReason(),
                        objectInfo.getCreator(),
                        objectInfo.getCreatorConractWay(),
                        createTime,
                        createTime,
                        objectInfo.getFollowLevel(),
                        objectInfo.getStatus());
            } else {
                jdbcTemplate.update(sql,
                        objectInfo.getId(),
                        objectInfo.getName(),
                        objectInfo.getObjectTypeKey(),
                        objectInfo.getIdcard(),
                        objectInfo.getSex(),
                        objectInfo.getPictureDatas().getImageData(),
                        null,
                        objectInfo.getReason(),
                        objectInfo.getCreator(),
                        objectInfo.getCreatorConractWay(),
                        createTime,
                        createTime,
                        objectInfo.getFollowLevel(),
                        objectInfo.getStatus());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
        log.info("Add object info successfull");
        return 0;
    }

    /**
     * 删除 ObjectInfo
     *
     * @param rowkeys 对象ID列表
     * @return 返回值为0，表示删除成功，返回值为1，表示删除失败
     */
    public Integer deleteObjectInfo(List<String> rowkeys) {
        // 获取table 对象，通过封装HBaseHelper 来获取
        String sql = parseByOption.deleteObjectInfo(rowkeys);
        try {
            List<Object[]> batchArgs = new ArrayList<>();
            for (String rowkey : rowkeys) {
                batchArgs.add(new Object[]{rowkey});
            }
            jdbcTemplate.batchUpdate(sql, batchArgs);
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
        return 0;
    }

    /**
     * 修改对象的信息   （外）（李第亮）
     *
     * @param objectInfo K-V 对，里面存放的是字段和值之间的一一对应关系，参考添加里的描述
     * @return 返回值为0，表示更新成功，返回值为1，表示更新失败
     */
    public Integer updateObjectInfo(ObjectInfoParam objectInfo) {
        try {
            ConcurrentHashMap<String, CopyOnWriteArrayList<Object>> sqlAndSetValues = parseByOption.getUpdateSqlFromObjectInfo(objectInfo);
            String sql = null;
            CopyOnWriteArrayList<Object> setValues = new CopyOnWriteArrayList<>();
            for (Map.Entry<String, CopyOnWriteArrayList<Object>> entry : sqlAndSetValues.entrySet()) {
                sql = entry.getKey();
                setValues = entry.getValue();
            }
            List<Object[]> batchArgs = new ArrayList<>();
            Object[] objects = new Object[setValues.size()];
            for (int i = 0; i < setValues.size(); i++) {
                objects[i] = setValues.get(i);
            }
            batchArgs.add(objects);
            jdbcTemplate.batchUpdate(sql, batchArgs);
            log.info("Update object info successfull");
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
        return 0;
    }

    /**
     * 更新人员信息状态值
     *
     * @param objectId 对象ID
     * @param status   状态值
     * @return 返回值为0，表示更新成功，返回值为1，表示更新失败
     */
    public Integer updateObjectInfo_status(String objectId, int status) {
        log.info("Update objectInfo status param : objectId = " + objectId + ", status = " + status);
        String sql = parseByOption.updateObjectInfo_status(objectId, status);
        log.info("Update objectInfo status SQL : " + sql);
        try {
            jdbcTemplate.update(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
        log.info("Update object info status successfull");
        return 0;
    }

    /**
     * 可以匹配精确查找，以图搜索人员信息，模糊查找   （外）（李第亮）
     *
     * @param param 查询参数
     * @return 返回搜索所需要的结果封装成的对象，包含搜索id，成功与否标志，记录数，记录信息，照片id
     */
    public SqlRowSet getObjectInfo(GetObjectInfoParam param) {
        //封装的sql 以及需要设置的值
        SqlAndArgs sqlAndArgs = parseByOption.getSqlFromGetObjectInfoParm(param);
        // 取出封装的sql 以及需要设置的值，进行sql 查询
        if (sqlAndArgs == null) {
            log.warn("Start get object info, generate sql failed");
            return null;
        }
        log.info("Start get object info, generate sql successfull, sql is:");
        log.info(sqlAndArgs.getSql());
        log.info(sqlAndArgs.getArgs().toString());
        return jdbcTemplate.queryForRowSet(sqlAndArgs.getSql(), sqlAndArgs.getArgs().toArray());
    }

    public PictureData getPictureData(String id) {
        log.info("Get objectInfo photo and feature by rowkey param : " + id);
        String sql = parseByOption.getPictureData();
        log.info("Get objectInfo photo and feature by rowkey SQL : " + sql);
        PictureData pictureData = new PictureData();
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql, id);
        if (mapList != null && mapList.size() > 0) {
            Map<String, Object> map = mapList.get(0);
            if (!map.isEmpty()) {
                byte[] photo = (byte[]) map.get(ObjectInfoTable.PHOTO);
                pictureData.setImageData(photo);
                Array featureArray = (Array) map.get(ObjectInfoTable.FEATURE);
                try {
                    float[] feature = (float[]) featureArray.getArray();
                    if (photo != null && photo.length > 0){
                    FaceAttribute faceAttribute = new FaceAttribute();
                    faceAttribute.setFeature(feature);
                    pictureData.setFeature(faceAttribute);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return pictureData;
    }

    /**
     * 根据rowkey 返回人员的照片
     *
     * @param objectId 人员在对象信息库中的唯一标志。
     * @return 图片的byte[] 数组
     */
    public byte[] getPhotoByObjectId(String objectId) {
        String sql = parseByOption.getPhotoByObjectId();
        byte[] photo = null;
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, objectId);
        while (sqlRowSet.next()) {
            photo = (byte[]) sqlRowSet.getObject(ObjectInfoTable.PHOTO);
        }
        return photo;
    }

    /**
     * 根据传过来的搜索rowkey 返回搜索记录 （外） （李第亮）
     *
     * @param subQueryId 子查询Id
     * @return 返回一个ObjectSearchResult 对象，
     * @author 李第亮
     */
    public PersonSingleResult getRocordOfObjectInfo(String subQueryId) {
        // sql 查询语句
        String sql = "select " + SearchRecordTable.ID + ", " + SearchRecordTable.RESULT + " from "
                + SearchRecordTable.TABLE_NAME + " where " + SearchRecordTable.ID + " = ?";
        PersonSingleResult personSingleResult = null;

        try {
            RowMapper<PersonSingleResult> rowMapper = new BeanPropertyRowMapper<>(PersonSingleResult.class);
            personSingleResult = jdbcTemplate.queryForObject(sql, rowMapper, subQueryId);
            ResultSet resultSet = null;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return personSingleResult;
    }

    /**
     * 统计常住人口
     *
     * @return int 常住人口数量
     */
    public int countStatus() {
        log.info("Start count objectInfo person.status from " + ObjectInfoTable.TABLE_NAME + "table.");
        String sql = parseByOption.countStatus();
        log.info("Count objectInfo person.status SQL : " + sql);
        int count = 0;
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);
        while (sqlRowSet.next()) {
            count = sqlRowSet.getInt("num");
        }
        log.info("Count objectInfo person.status is: " + count);
        return count;
    }

    public Map migrationCount(String month, Timestamp start_time, Timestamp end_time) {
        log.info("Start count objectInfo migration number");
        String sql = parseByOption.migrationCount();
        log.info("Count objectInfo migration SQL : " + sql);
        Map<String, Integer> map = new HashMap<>();
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, start_time, end_time);
        while (sqlRowSet.next()) {
            int count = sqlRowSet.getInt("num");
            map.put(month, count);
        }
        log.info("Count objectInfo migration is: " + map.get(month));
        return map;
    }
}
