package com.hzgc.service.starepo.dao;

import com.hzgc.common.service.table.column.ObjectInfoTable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.*;

@Repository
@Slf4j
public class ObjectTypeDao {

    @Resource(name = "phoenixJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    /**
     * 添加objectType
     *
     * @param name    类型名
     * @param creator 创建者
     * @param remark  备注
     * @return boolean
     */
    public boolean addObjectType(String name, String creator, String remark) {
        log.info("objectType" + name);
        if (name == null || "".equals(name)) {
            log.info("name is null");
            return false;
        }
        long start = System.currentTimeMillis();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String typeId = "type_" + System.currentTimeMillis() + uuid.substring(0, 8);
        String sql = "upsert into objectinfo(" + ObjectInfoTable.ROWKEY + ", "
                + ObjectInfoTable.TYPE_COLF + "." + ObjectInfoTable.TYPE_NAME + ", "
                + ObjectInfoTable.TYPE_COLF + "." + ObjectInfoTable.TYPE_CREATOR + ", "
                + ObjectInfoTable.TYPE_COLF + "." + ObjectInfoTable.TYPE_REMARK + ", "
                + ObjectInfoTable.TYPE_COLF + "." + ObjectInfoTable.TYPE_ADD_TIME
                + ") values (?,?,?,?,?)";
        log.info("sql:" + sql);
        try {
            jdbcTemplate.update(sql, typeId, name, creator, remark, new java.sql.Timestamp(System.currentTimeMillis()));
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
        log.info("添加一条数据到静态库花费时间： " + (System.currentTimeMillis() - start));
        return true;
    }

    /**
     * 删除objectType
     *
     * @param id 类型ID
     * @return boolean
     */
    public boolean deleteObjectType(String id) {
        log.info("rowkey to delete : " + id);

        if (id == null || "".equals(id)) {
            return false;
        }
        long start = System.currentTimeMillis();
        String sql1 = "select id from objectinfo where " + ObjectInfoTable.PKEY + " = ?";
        String sql2 = "delete from objectinfo where " + ObjectInfoTable.ROWKEY + " = ?";
        try {
            SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql1, id);
            while (sqlRowSet.next()) {
                String rowkey = sqlRowSet.getString(ObjectInfoTable.ROWKEY);
                log.info("存在该类型的对象，不能删除");
                return false;
            }
            jdbcTemplate.update(sql2, id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        log.info("删除静态信息库的" + id + "数据花费时间： " + (System.currentTimeMillis() - start));
        return true;
    }

    /**
     * 修改ObjectType
     *
     * @param id      类型ID
     * @param name    类型名
     * @param creator 创建者
     * @param remark  备注
     * @return boolean
     */
    public boolean updateObjectType(String id, String name, String creator, String remark) {
        log.info("objectType" + id + " : " + name);
        if (id == null || "".equals(id)) {
            return false;
        }
        if (name == null || "".equals(name)) {
            return false;
        }
        long start = System.currentTimeMillis();
        String sql = "upsert into objectinfo(" + ObjectInfoTable.ROWKEY + ", "
                + ObjectInfoTable.TYPE_COLF + "." + ObjectInfoTable.TYPE_NAME + ", "
                + ObjectInfoTable.TYPE_COLF + "." + ObjectInfoTable.TYPE_CREATOR + ", "
                + ObjectInfoTable.TYPE_COLF + "." + ObjectInfoTable.TYPE_REMARK + ", "
                + ObjectInfoTable.TYPE_COLF + "." + ObjectInfoTable.TYPE_UPDATE_TIME
                + ") values (?,?,?,?,?)";
        try {
            jdbcTemplate.update(sql, id, name, creator, remark, new java.sql.Timestamp(System.currentTimeMillis()));
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
        log.info("添加一条数据到静态库花费时间： " + (System.currentTimeMillis() - start));
        return true;
    }

    /**
     * 查询objectType
     *
     * @param name      类型名
     * @param pageIndex 页码
     * @param pageSize  每页行数
     * @return
     */
    public List<Map<String, String>> searchObjectType(String name, int pageIndex, int pageSize) {
        if (pageIndex == 0) {
            pageIndex = 1;
        }
        if (pageSize == 0) {
            pageSize = 5;
        }
        List<Map<String, String>> result;
        SqlRowSet sqlRowSet;
        StringBuilder sql = new StringBuilder("select " + ObjectInfoTable.ROWKEY + ", "
                + ObjectInfoTable.TYPE_COLF + "." + ObjectInfoTable.TYPE_NAME + ", "
                + ObjectInfoTable.TYPE_COLF + "." + ObjectInfoTable.TYPE_CREATOR + ", "
                + ObjectInfoTable.TYPE_COLF + "." + ObjectInfoTable.TYPE_REMARK + ", "
                + ObjectInfoTable.TYPE_COLF + "." + ObjectInfoTable.TYPE_ADD_TIME
                + " from " + ObjectInfoTable.TABLE_NAME);
        sql.append(" where " + ObjectInfoTable.ROWKEY + " > ?");
        if (name != null && !"".equals(name)) {
            sql.append("AND " + ObjectInfoTable.TYPE_COLF + "." + ObjectInfoTable.TYPE_NAME + " like '%").append(name).append("%'");
        }
        sql.append(" LIMIT ").append(pageSize);
        try {
            String startRow = "a";
            if (pageIndex == 1) {
                sqlRowSet = getDate(sql.toString(), startRow);
                result = getResult(sqlRowSet);
            } else {
                for (int i = 1; i < pageIndex; i++) {
                    sqlRowSet = getDate(sql.toString(), startRow);
                    startRow = getLastRowkey(sqlRowSet);
                }
                sqlRowSet = getDate(sql.toString(), startRow);
                result = getResult(sqlRowSet);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            return null;
        }
        return result;
    }

    private List<Map<String, String>> getResult(SqlRowSet sqlRowSet) throws SQLException {
        List<Map<String, String>> result = new ArrayList<>();
        while (sqlRowSet.next()) {
            Map<String, String> map = new HashMap<>();
            map.put(ObjectInfoTable.ROWKEY, sqlRowSet.getString(ObjectInfoTable.ROWKEY));
            map.put(ObjectInfoTable.TYPE_NAME,
                    sqlRowSet.getString(ObjectInfoTable.TYPE_NAME));
            map.put(ObjectInfoTable.TYPE_CREATOR,
                    sqlRowSet.getString(ObjectInfoTable.TYPE_CREATOR));
            map.put(ObjectInfoTable.TYPE_REMARK,
                    sqlRowSet.getString(ObjectInfoTable.TYPE_REMARK));
            result.add(map);
        }
        return result;
    }


    private SqlRowSet getDate(String sql, String startRow) throws SQLException {
        return jdbcTemplate.queryForRowSet(sql, startRow);
    }

    private String getLastRowkey(SqlRowSet sqlRowSet) throws SQLException {
        String lastRowKey = null;
        while (sqlRowSet.next()) {
            lastRowKey = sqlRowSet.getString(ObjectInfoTable.ROWKEY);
        }
        return lastRowKey;
    }

}
