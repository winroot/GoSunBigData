package com.hzgc.service.starepo.dao;

import com.hzgc.common.jni.FaceAttribute;
import com.hzgc.common.service.table.column.ObjectInfoTable;
import com.hzgc.common.service.table.column.SearchRecordTable;
import com.hzgc.service.starepo.bean.*;
import com.hzgc.service.starepo.service.ObjectInfoHandlerTool;
import com.hzgc.service.starepo.service.ParseByOption;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import javax.annotation.Resource;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
@Slf4j
public class PhoenixDao implements Serializable {

    @Resource(name = "phoenixJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    /**
     * 针对单个对象信息的添加处理  （外）
     * @return 返回值为0，表示插入成功，返回值为1，表示插入失败
     */
    public Integer addObjectInfo(PersonObject person) {

        String sql = "upsert into objectinfo(" + ObjectInfoTable.ROWKEY + ", " + ObjectInfoTable.NAME + ", "
                + ObjectInfoTable.PLATFORMID + ", " + ObjectInfoTable.TAG + ", " + ObjectInfoTable.PKEY + ", "
                + ObjectInfoTable.IDCARD + ", " + ObjectInfoTable.SEX + ", " + ObjectInfoTable.PHOTO + ", "
                + ObjectInfoTable.FEATURE + ", " + ObjectInfoTable.REASON + ", " + ObjectInfoTable.CREATOR + ", "
                + ObjectInfoTable.CPHONE + ", " + ObjectInfoTable.CREATETIME + ", " + ObjectInfoTable.UPDATETIME + ", "
                + ObjectInfoTable.IMPORTANT + ", " + ObjectInfoTable.STATUS
                + ") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            if (person.getFeature() != null && person.getFeature().length == 512) {
                jdbcTemplate.update(sql,person.getId(),person.getName(),person.getPlatformid(),person.getTag(),person.getPkey(),person.getIdcard(),person.getSex(),person.getPhoto(),jdbcTemplate.getDataSource().getConnection().createArrayOf("FLOAT", PersonObject.otherArrayToObject(person.getFeature())),person.getReason(),person.getCreator(),person.getCphone(),new Timestamp(System.currentTimeMillis()),person.getUpdatetime(),person.getImportant(),person.getStatus());
            }else {
                jdbcTemplate.update(sql,person.getId(),person.getName(),person.getPlatformid(),person.getTag(),person.getPkey(),person.getIdcard(),person.getSex(),person.getPhoto(),null,person.getReason(),person.getCreator(),person.getCphone(),new Timestamp(System.currentTimeMillis()),person.getUpdatetime(),person.getImportant(),person.getStatus());
            }
        }catch (Exception e){
            e.printStackTrace();
            return 1;
        }
        //数据变动，更新objectinfo table 中的一条数据,表示静态库中的数据有变动
        new ObjectInfoHandlerTool().updateTotalNumOfHbase();
        return 0;
    }

    /**
     * 删除对象的信息  （外）（李第亮）
     * @param rowkeys 具体的一个人员信息的ID，值唯一
     * @return 返回值为0，表示删除成功，返回值为1，表示删除失败
     */
    public Integer deleteObjectInfo(List<String> rowkeys) {
        log.info("rowKeys: " + rowkeys);
        // 获取table 对象，通过封装HBaseHelper 来获取
        long start = System.currentTimeMillis();
        String sql = "delete from objectinfo where " + ObjectInfoTable.ROWKEY + " = ?";
        try {
            List<Object[]> batchArgs = new ArrayList<>();
            for (int i = 0; i < rowkeys.size(); i++) {
                batchArgs.add(new Object[]{rowkeys.get(i)});
            }
            jdbcTemplate.batchUpdate(sql, batchArgs);
        }catch (Exception e){
            e.printStackTrace();
            return 1;
        }
        log.info("删除静态信息库的" + rowkeys.size() + "条数据花费时间： " + (System.currentTimeMillis() - start));
        //数据变动，更新objectinfo table 中的一条数据,表示静态库中的数据有变动
        new ObjectInfoHandlerTool().updateTotalNumOfHbase();
        return 0;
    }

    /**
     * 修改对象的信息   （外）（李第亮）
     * @param personObject K-V 对，里面存放的是字段和值之间的一一对应关系，参考添加里的描述
     * @return 返回值为0，表示更新成功，返回值为1，表示更新失败
     */
    public Integer updateObjectInfo(Map<String, Object> personObject) {
        try {
            ConcurrentHashMap<String, CopyOnWriteArrayList<Object>> sqlAndSetValues = ParseByOption.getUpdateSqlFromPersonMap(personObject);
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
            jdbcTemplate.batchUpdate(sql,batchArgs);
        }catch (Exception e){
            e.printStackTrace();
            return 1;
        }
        //数据变动，更新objectinfo table 中的一条数据,表示静态库中的数据有变动
        new ObjectInfoHandlerTool().updateTotalNumOfHbase();
        return 0;
    }

    /**
     * 根据rowkey 进行查询 （外）
     * @param id  标记一条对象信息的唯一标志。
     * @return  返回搜索所需要的结果封装成的对象，包含搜索id，成功与否标志，记录数，记录信息，照片id
     */
    public List<PersonObject> searchByRowkey(String id) {
            String sql = "select * from " + ObjectInfoTable.TABLE_NAME + " where id = ?";
            RowMapper<PersonObject> rowMapper = new BeanPropertyRowMapper <>(PersonObject.class);
            List<PersonObject> personObjects = jdbcTemplate.query(sql,rowMapper,id);
            return personObjects;
    }

    /**
     * 可以匹配精确查找，以图搜索人员信息，模糊查找   （外）（李第亮）
     * @param pSearchArgsModel 搜索参数的封装
     * @return 返回搜索所需要的结果封装成的对象，包含搜索id，成功与否标志，记录数，记录信息，照片id
     */
    public ObjectSearchResult getObjectInfo(PSearchArgsModel pSearchArgsModel) {
        log.info("pSearchArgsModel: " + pSearchArgsModel);
        long start = System.currentTimeMillis();
        // 总的结果
        ObjectSearchResult objectSearchResult = new ObjectSearchResult();
        String searchTotalId = UUID.randomUUID().toString().replace("-", "");
        objectSearchResult.setSearchTotalId(searchTotalId);
        List<PersonSingleResult> finalResults = new ArrayList<>();

        //封装的sql 以及需要设置的值
        Map<String, List<Object>> finalSqlAndValues = ParseByOption.getSqlFromPSearchArgsModel( pSearchArgsModel);

        // 取出封装的sql 以及需要设置的值，进行sql 查询
        if (finalSqlAndValues == null) {
            objectSearchResult.setSearchStatus(1);
            log.info("创建sql 失败，请检查代码");
            return objectSearchResult;
        }
        for (Map.Entry<String, List<Object>> entry : finalSqlAndValues.entrySet()) {
            String sql = entry.getKey();
            List<Object> setValues = entry.getValue();
            List<Object[]> batchArgs = new ArrayList<>();
            Object[] objects = new Object[setValues.size()];
            try {
                // 实例化pstm 对象，并且设置值，
                for (int i = 0; i < setValues.size(); i++) {
                    objects[i] = setValues.get(i);
                }
                batchArgs.add(objects);
                SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql,batchArgs);
                Map<String, byte[]> photos = pSearchArgsModel.getImages();
                Map<String, FaceAttribute> faceAttributeMap = pSearchArgsModel.getFaceAttributeMap();
                // 有图片的情况下
                if (photos != null && photos.size() != 0
                        && faceAttributeMap != null && faceAttributeMap.size() != 0
                        && faceAttributeMap.size() == photos.size()) {
                    if (!pSearchArgsModel.isTheSameMan()) {  // 不是同一个人
                        // 分类的人
                        Map<String, List<PersonObject>> personObjectsMap = new HashMap<>();

                        List<PersonObject> personObjects = new ArrayList<>();
                        PersonObject personObject;
                        List<String> types = new ArrayList<>();
                        int lable = 0;
                        while (sqlRowSet.next()) {
                            String type = sqlRowSet.getString("type");
                            if (!types.contains(type)) {
                                types.add(type);
                                if (types.size() > 1) {
                                    personObjectsMap.put(types.get(lable), personObjects);
                                    personObjects = new ArrayList<>();
                                    lable++;
                                }
                            }
                            personObject = new PersonObject();
                            personObject.setId(sqlRowSet.getString(ObjectInfoTable.ROWKEY));
                            personObject.setPkey(sqlRowSet.getString(ObjectInfoTable.PKEY));
                            personObject.setPlatformid(sqlRowSet.getString(ObjectInfoTable.PLATFORMID));
                            personObject.setName(sqlRowSet.getString(ObjectInfoTable.NAME));
                            personObject.setSex(sqlRowSet.getInt(ObjectInfoTable.SEX));
                            personObject.setIdcard(sqlRowSet.getString(ObjectInfoTable.IDCARD));
                            personObject.setCreator(sqlRowSet.getString(ObjectInfoTable.CREATOR));
                            personObject.setCphone(sqlRowSet.getString(ObjectInfoTable.CPHONE));
                            personObject.setUpdatetime(sqlRowSet.getTimestamp(ObjectInfoTable.UPDATETIME));
                            personObject.setCreatetime(sqlRowSet.getTimestamp(ObjectInfoTable.CREATETIME));
                            personObject.setReason(sqlRowSet.getString(ObjectInfoTable.REASON));
                            personObject.setTag(sqlRowSet.getString(ObjectInfoTable.TAG));
                            personObject.setImportant(sqlRowSet.getInt(ObjectInfoTable.IMPORTANT));
                            personObject.setStatus(sqlRowSet.getInt(ObjectInfoTable.STATUS));
                            personObject.setSim(sqlRowSet.getFloat(ObjectInfoTable.RELATED));
                            personObject.setLocation(sqlRowSet.getString(ObjectInfoTable.LOCATION));
                            personObjects.add(personObject);
                        }
                        // 封装最后需要返回的结果
                        for (Map.Entry<String, List<PersonObject>> entryVV : personObjectsMap.entrySet()) {
                            String key = entryVV.getKey();
                            List<PersonObject> persons = entryVV.getValue();
                            PersonSingleResult personSingleResult = new PersonSingleResult();
                            personSingleResult.setSearchRowkey(searchTotalId + key);
                            personSingleResult.setSearchNums(persons.size());
                            personSingleResult.setPersons(persons);
                            List<byte[]> photosTmp = new ArrayList<>();
                            photosTmp.add(photos.get(key));
                            personSingleResult.setSearchPhotos(photosTmp);
                            if (personSingleResult.getPersons() != null || personSingleResult.getPersons().size() != 0) {
                                finalResults.add(personSingleResult);
                            }
                        }
                    } else {  // 是同一个人
                        PersonSingleResult personSingleResult = new PersonSingleResult();
                        personSingleResult.setSearchRowkey(searchTotalId);

                        List<byte[]> searchPhotos = new ArrayList<>();
                        for (Map.Entry<String, byte[]> entryV1 : photos.entrySet()) {
                            searchPhotos.add(entryV1.getValue());
                        }
                        personSingleResult.setSearchPhotos(searchPhotos);
                        // 封装personSingleResult
                        new ObjectInfoHandlerTool().getPersonSingleResult(personSingleResult, sqlRowSet, true);
                        if (personSingleResult.getPersons() != null || personSingleResult.getPersons().size() != 0) {
                            finalResults.add(personSingleResult);
                        }
                    }
                } else { // 没有图片的情况下
                    PersonSingleResult personSingleResult = new PersonSingleResult();   // 需要进行修改
                    personSingleResult.setSearchRowkey(searchTotalId);
                    //封装personSingleResult
                    new ObjectInfoHandlerTool().getPersonSingleResult(personSingleResult, sqlRowSet, false);
                    if (personSingleResult.getPersons() != null || personSingleResult.getPersons().size() != 0) {
                        finalResults.add(personSingleResult);
                    }
                }
            } catch (Exception e) {
                objectSearchResult.setSearchStatus(1);
                e.printStackTrace();
                return objectSearchResult;
            }
        }

        objectSearchResult.setFinalResults(finalResults);
        objectSearchResult.setSearchStatus(0);

        log.info("总的搜索时间是: " + (System.currentTimeMillis() - start));
        new ObjectInfoHandlerTool().saveSearchRecord(jdbcTemplate, objectSearchResult);
        Integer pageSize = pSearchArgsModel.getPageSize();
        Integer startCount = pSearchArgsModel.getStart();
        if (startCount != null && pageSize != null) {
            new ObjectInfoHandlerTool().formatTheObjectSearchResult(objectSearchResult, startCount, pageSize);
        }
        log.info("***********************");
        log.info(objectSearchResult.toString());
        log.info("***********************");
        return objectSearchResult;
    }

    /**
     * 根据rowkey 返回人员的照片
     * @param rowkey 人员在对象信息库中的唯一标志。
     * @return 图片的byte[] 数组
     */
    public Byte getPhotoByKey(String rowkey) {
        String sql = "select photo from " + ObjectInfoTable.TABLE_NAME + " where id = ?";
        byte photo;
        try {
            SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql,rowkey);
            sqlRowSet.next();
            photo = sqlRowSet.getByte(ObjectInfoTable.PHOTO);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return photo;
    }

    private static ObjectSearchResult getObjectSearchResultError(String errorMsg) {
        ObjectSearchResult objectSearchResultError = new ObjectSearchResult();
        objectSearchResultError.setSearchStatus(1);
        log.info(errorMsg);
        return objectSearchResultError;
    }

    /**
     * 根据传过来的搜索rowkey 返回搜索记录 （外） （李第亮）
     * @param  subQueryId 子查询Id
     * @return  返回一个ObjectSearchResult 对象，
     * @author 李第亮
     */
    public PersonSingleResult getRocordOfObjectInfo(String subQueryId) {
        // sql 查询语句
        String sql = "select " + SearchRecordTable.ID + ", " + SearchRecordTable.RESULT + " from "
                + SearchRecordTable.TABLE_NAME + " where " + SearchRecordTable.ID + " = ?";
        PersonSingleResult personSingleResult = null;

        try {
            RowMapper<PersonSingleResult> rowMapper = new BeanPropertyRowMapper <>(PersonSingleResult.class);
            personSingleResult = jdbcTemplate.queryForObject(sql,rowMapper,subQueryId);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return personSingleResult;
    }
}
