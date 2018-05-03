package com.hzgc.service.clustering.dao;

import com.hzgc.common.service.clustering.AlarmInfo;
import com.hzgc.common.service.clustering.ClusteringAttribute;
import com.hzgc.common.service.connection.HBaseHelper;
import com.hzgc.common.service.table.column.ClusteringTable;
import com.hzgc.common.util.object.ObjectUtil;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class HBaseDao {

    private static Logger LOG = Logger.getLogger(HBaseDao.class);

    public HBaseDao() {
        HBaseHelper.getHBaseConnection();
    }

    public List<ClusteringAttribute> clusteringSearch(String region, String time) {
        Table clusteringInfoTable = HBaseHelper.getTable(ClusteringTable.TABLE_ClUSTERINGINFO);
        Get get = new Get(Bytes.toBytes(time + "-" + region));
        List<ClusteringAttribute> clusteringList = new ArrayList<>();
        try {
            Result result = clusteringInfoTable.get(get);
            if (result != null && result.size() > 0) {
                clusteringList = (List<ClusteringAttribute>) ObjectUtil.byteToObject(result.getValue(ClusteringTable.ClUSTERINGINFO_COLUMNFAMILY, ClusteringTable.ClUSTERINGINFO_COLUMN_YES));
            } else {
                LOG.info("no data get from HBase");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return clusteringList;
    }

    public List<AlarmInfo> detailClusteringSearch(String clusterId, String time) {
        Table clusteringInfoTable = HBaseHelper.getTable(ClusteringTable.TABLE_DETAILINFO);
        Get get = new Get(Bytes.toBytes(time + "-" + clusterId));
        List<AlarmInfo> alarmInfoList = new ArrayList<>();
        try {
            Result result = clusteringInfoTable.get(get);
            alarmInfoList = (List<AlarmInfo>) ObjectUtil.byteToObject(result.getValue(ClusteringTable.ClUSTERINGINFO_COLUMNFAMILY, ClusteringTable.ClUSTERINGINFO_COLUMN_YES));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return alarmInfoList;
    }

    public List<ClusteringAttribute> getClustering(String region, String time, byte[] colName) {
        List<ClusteringAttribute> clusteringAttributeList = new ArrayList<>();
        Table clusteringInfoTable = HBaseHelper.getTable(ClusteringTable.TABLE_ClUSTERINGINFO);
        Get get = new Get(Bytes.toBytes(time + "-" + region));
        try {
            Result result = clusteringInfoTable.get(get);
            byte[] bytes = result.getValue(ClusteringTable.ClUSTERINGINFO_COLUMNFAMILY, colName);
            if (bytes != null) {
                clusteringAttributeList = (List<ClusteringAttribute>) ObjectUtil.byteToObject(bytes);
            }else {
                LOG.info("no clustering in the database to be delete");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return clusteringAttributeList;
    }

    public boolean putClustering(String region, String time, byte[] colName, List<ClusteringAttribute> clusteringAttributeList) {
        Table clusteringInfoTable = HBaseHelper.getTable(ClusteringTable.TABLE_ClUSTERINGINFO);
        Put put = new Put(Bytes.toBytes(time + "-" + region));
        try {
            byte[] clusteringInfoByte = ObjectUtil.objectToByte(clusteringAttributeList);
            put.addColumn(ClusteringTable.ClUSTERINGINFO_COLUMNFAMILY, colName, clusteringInfoByte);
            clusteringInfoTable.put(put);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }



    public List<Integer> detailClusteringSearch_Hbase(String clusterId, String time) {
        Table clusteringInfoTable = HBaseHelper.getTable(ClusteringTable.TABLE_DETAILINFO);
        Get get = new Get(Bytes.toBytes(time + "-" + clusterId));
        List<Integer> alarmInfoList = new ArrayList<>();
        try {
            Result result = clusteringInfoTable.get(get);
            alarmInfoList = (List<Integer>) ObjectUtil.byteToObject(result.getValue(ClusteringTable.ClUSTERINGINFO_COLUMNFAMILY, ClusteringTable.ClUSTERINGINFO_COLUMN_YES));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return alarmInfoList;
    }
}
