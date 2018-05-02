package com.hzgc.service.starepo.service;

import com.hzgc.service.starepo.util.PhoenixJDBCHelper;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import com.hzgc.common.service.table.column.ObjectInfoTable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Service
public class ObjectTypeServiceImpl implements ObjectTypeService {
    private static Logger LOG = Logger.getLogger(ObjectInfoHandlerImpl.class);
    @Override
    public boolean addObjectType(String name, String creator, String remark) {
        LOG.info("objectType" + name);
        if(name == null || "".equals(name)){
            return false;
        }
        java.sql.Connection conn = null;
        long start = System.currentTimeMillis();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String typeId = "type_" + System.currentTimeMillis() + uuid.substring(0, 8);
        String sql = "upsert into objectinfo(" + ObjectInfoTable.ROWKEY + ", "
                + ObjectInfoTable.TYPE_COLF + "." + ObjectInfoTable.TYPE_NAME + ", "
                + ObjectInfoTable.TYPE_COLF + "." + ObjectInfoTable.TYPE_CREATOR + ", "
                + ObjectInfoTable.TYPE_COLF + "." + ObjectInfoTable.TYPE_REMARK + ", "
                + ObjectInfoTable.TYPE_COLF + "." + ObjectInfoTable.TYPE_ADD_TIME
                +") values (?,?,?,?,?)";
        LOG.info("sql:" + sql);
        PreparedStatement pstm = null;
        try {
            conn = PhoenixJDBCHelper.getInstance().getConnection();
            pstm = conn.prepareStatement(sql);
            pstm.setString(1, typeId);
            pstm.setString(2, name);
            pstm.setString(3, creator);
            pstm.setString(4, remark);
            pstm.setTimestamp(5,new java.sql.Timestamp(System.currentTimeMillis()));
            pstm.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            return false;
        } finally {
            PhoenixJDBCHelper.closeConnection(null, pstm, null);
        }
        LOG.info("添加一条数据到静态库花费时间： " + (System.currentTimeMillis() - start));
        return true;
    }

    @Override
    public boolean deleteObjectType(String id) {
        LOG.info("rowkey to delete : " + id);

        if(id == null || "".equals(id)){
            return false;
        }
        long start = System.currentTimeMillis();
        java.sql.Connection conn = null;
        String sql1 = "select id from objectinfo where " + ObjectInfoTable.PKEY + " = ?";
        String sql2 = "delete from objectinfo where " + ObjectInfoTable.ROWKEY + " = ?";
        PreparedStatement pstm = null;
        try {
            conn = PhoenixJDBCHelper.getInstance().getConnection();
            pstm = conn.prepareStatement(sql1);
            pstm.setString(1,id);
            ResultSet resultSet = pstm.executeQuery();
            while (resultSet.next()){
                String rowkey = resultSet.getString(ObjectInfoTable.ROWKEY);
                LOG.info("存在该类型的对象，不能删除");
                PhoenixJDBCHelper.closeConnection(null, pstm, null);
                return false;
            }
            pstm = conn.prepareStatement(sql2);
            pstm.setString(1, id);
            pstm.executeUpdate();
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            PhoenixJDBCHelper.closeConnection(null, pstm, null);
        }
        LOG.info("删除静态信息库的" + id + "数据花费时间： " + (System.currentTimeMillis() - start));
        return true;
    }

    @Override
    public boolean updateObjectType(String  id, String name, String creator, String remark) {
        LOG.info("objectType" + id + " : " + name);
        if(id == null || "".equals(id)){
            return false;
        }
        if(name == null || "".equals(name)){
            return false;
        }
        java.sql.Connection conn = null;
        long start = System.currentTimeMillis();
        String sql = "upsert into objectinfo(" + ObjectInfoTable.ROWKEY + ", "
                + ObjectInfoTable.TYPE_COLF + "." + ObjectInfoTable.TYPE_NAME + ", "
                + ObjectInfoTable.TYPE_COLF + "." + ObjectInfoTable.TYPE_CREATOR + ", "
                + ObjectInfoTable.TYPE_COLF + "." + ObjectInfoTable.TYPE_REMARK + ", "
                + ObjectInfoTable.TYPE_COLF + "." + ObjectInfoTable.TYPE_UPDATE_TIME
                +") values (?,?,?,?,?)";
        PreparedStatement pstm = null;
        try {
            conn = PhoenixJDBCHelper.getInstance().getConnection();
            pstm = conn.prepareStatement(sql);
            pstm.setString(1, id);
            pstm.setString(2, name);
            pstm.setString(3, creator);
            pstm.setString(4, remark);
            pstm.setTimestamp(5,new java.sql.Timestamp(System.currentTimeMillis()));
            pstm.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            return false;
        } finally {
            PhoenixJDBCHelper.closeConnection(null, pstm, null);
        }
        LOG.info("添加一条数据到静态库花费时间： " + (System.currentTimeMillis() - start));
        return true;
    }

    @Override
    public List<Map<String, String>> searchObjectType(String name, int pageIndex, int pageSize) {
        if(pageIndex == 0){
            pageIndex = 1;
        }
        if(pageSize == 0){
            pageSize = 5;
        }
        List<Map<String, String>> result = null;
        java.sql.Connection conn = null;
        ResultSet resultSet = null;
        PreparedStatement pstm = null;
        StringBuilder sql = new StringBuilder("select " + ObjectInfoTable.ROWKEY + ", "
                + ObjectInfoTable.TYPE_COLF + "." + ObjectInfoTable.TYPE_NAME + ", "
                + ObjectInfoTable.TYPE_COLF + "." + ObjectInfoTable.TYPE_CREATOR + ", "
                + ObjectInfoTable.TYPE_COLF + "." + ObjectInfoTable.TYPE_REMARK + ", "
                + ObjectInfoTable.TYPE_COLF + "." + ObjectInfoTable.TYPE_ADD_TIME
                + " from " + ObjectInfoTable.TABLE_NAME);
        sql.append( " where " + ObjectInfoTable.ROWKEY + " > ?");
        if(name != null && !"".equals(name)){
            sql.append("AND "+ ObjectInfoTable.TYPE_COLF + "." + ObjectInfoTable.TYPE_NAME + " like '%"+ name +"%'");
        }
        sql.append(" LIMIT " + pageSize);
        try {
            conn = PhoenixJDBCHelper.getInstance().getConnection();
            pstm = conn.prepareStatement(sql.toString());
            String startRow = "a";
            if(pageIndex == 1){
                resultSet = getDate(pstm, startRow);
                result = getResult(resultSet);
            }else{
                for(int i = 1;i< pageIndex; i++){
                    resultSet = getDate(pstm, startRow);
                    startRow = getLastRowkey(resultSet);
                }
                resultSet = getDate(pstm, startRow);
                result = getResult(resultSet);
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            return null;
        } finally {
            PhoenixJDBCHelper.closeConnection(null, pstm, null);
        }
        return result;
    }

    /**
     * 从ResultSet中取出数据
     * @param resultSet
     * @return
     * @throws SQLException
     */
    private List<Map<String, String>> getResult(ResultSet resultSet) throws SQLException {
        List<Map<String, String>> result = new ArrayList<>();
        while (resultSet.next()){
            Map<String, String> map = new HashMap<>();
            map.put(ObjectInfoTable.ROWKEY, resultSet.getString(ObjectInfoTable.ROWKEY));
            map.put(ObjectInfoTable.TYPE_NAME,
                    resultSet.getString(ObjectInfoTable.TYPE_NAME));
            map.put(ObjectInfoTable.TYPE_CREATOR,
                    resultSet.getString(ObjectInfoTable.TYPE_CREATOR));
            map.put(ObjectInfoTable.TYPE_REMARK,
                    resultSet.getString(ObjectInfoTable.TYPE_REMARK));
            result.add(map);
        }
        return result;
    }

    /**
     * 获取一页数据
     * @param pstm
     * @param startRow
     * @return
     * @throws SQLException
     */
    private ResultSet getDate(PreparedStatement pstm , String startRow) throws SQLException {
        pstm.setString(1, startRow);
        ResultSet resultSet = pstm.executeQuery();
        return resultSet;
    }

    /**
     * 取得最后一行Rowkey
     * @param resultSet
     * @return
     * @throws SQLException
     */
    private String getLastRowkey(ResultSet resultSet) throws SQLException {
        String lastRowKey = null;
        while(resultSet.next()){
            lastRowKey = resultSet.getString(ObjectInfoTable.ROWKEY);
        }
        return lastRowKey;
    }
}
