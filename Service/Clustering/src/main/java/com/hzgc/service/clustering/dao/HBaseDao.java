package com.hzgc.service.clustering.dao;

import com.hzgc.common.faceclustering.ClusteringAttribute;
import com.hzgc.common.hbase.HBaseHelper;
import com.hzgc.common.faceclustering.table.ClusteringTable;
import com.hzgc.common.util.object.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class HBaseDao {



    public HBaseDao() {
        HBaseHelper.getHBaseConnection();
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
            } else {
                log.info("No clustering in the database to be delete");
                return clusteringAttributeList;
            }
        } catch (IOException e) {
            log.info(e.getMessage());
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
            log.info(e.getMessage());
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
            log.info(e.getMessage());
            e.printStackTrace();
        }
        return alarmInfoList;
    }

    public Map<String, Integer> getTotleNum(String startTime, String endTime){
        Table clusteringInfoTable = HBaseHelper.getTable(ClusteringTable.TABLE_ClUSTERINGINFO);
        Map<String, Integer> map = new HashMap<>();
        Scan scan = new Scan();
        scan.addColumn(ClusteringTable.ClUSTERINGINFO_COLUMNFAMILY, ClusteringTable.ClUSTERINGINFO_COLUMN_YES);
        scan.setStartRow(Bytes.toBytes(startTime));
        scan.setStopRow(Bytes.toBytes(endTime));
        try {
            ResultScanner results = clusteringInfoTable.getScanner(scan);
            for(Result result : results){
                byte[] bytes = result.getValue(ClusteringTable.ClUSTERINGINFO_COLUMNFAMILY, ClusteringTable.ClUSTERINGINFO_COLUMN_YES);
                List<ClusteringAttribute> clusteringAttributeList = (List<ClusteringAttribute>) ObjectUtil.byteToObject(bytes);
                int size = clusteringAttributeList.size();
                String rowkey = new String(result.getRow());
                map.put(rowkey, size);
            }
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }
}
