package com.hzgc.service.clustering.service;

import com.hzgc.common.service.clustering.AlarmInfo;
import com.hzgc.common.service.clustering.ClusteringAttribute;
import com.hzgc.common.service.table.column.ClusteringTable;
import com.hzgc.common.service.table.column.DynamicTable;
import com.hzgc.common.util.empty.IsEmpty;
import com.hzgc.service.clustering.bean.ClusteringInfo;
import com.hzgc.service.clustering.bean.SortParam;
import com.hzgc.service.clustering.dao.ElasticSearchDao;
import com.hzgc.service.clustering.dao.HBaseDao;
import org.apache.log4j.Logger;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 告警聚类结果查询接口实现(彭聪)
 */
@Service
public class ClusteringSearchService {

    private static Logger LOG = Logger.getLogger(ClusteringSearchService.class);

    @Autowired
    private HBaseDao hBaseDao;

    @Autowired
    private ElasticSearchDao elasticSearchDao;

    /**
     * 查询聚类信息
     *
     * @param time      聚类时间
     * @param start     返回数据下标开始符号
     * @param limit     行数
     * @param sortParam 排序参数（默认按出现次数排序）
     * @return 聚类列表
     */
    public ClusteringInfo clusteringSearch(String region, String time, int start, int limit, String sortParam) {
        List<ClusteringAttribute> clusteringList = hBaseDao.clusteringSearch(region, time);
        if (IsEmpty.strIsRight(sortParam)) {
            SortParam sortParams = ListUtils.getOrderStringBySort(sortParam);
            ListUtils.sort(clusteringList, sortParams.getSortNameArr(), sortParams.getIsAscArr());
        }
        int total = clusteringList.size();
        ClusteringInfo clusteringInfo = new ClusteringInfo();
        clusteringInfo.setTotalClustering(total);
        if (start > -1 && start <= total) {
            if ((start + limit) > total) {
                clusteringInfo.setClusteringAttributeList(clusteringList.subList(start, total));
            } else {
                clusteringInfo.setClusteringAttributeList(clusteringList.subList(start, start + limit));
            }
        } else {
            LOG.info("start or limit out of index ");
        }
        return clusteringInfo;
    }

    /**
     * 查询单个聚类详细信息(告警记录)
     *
     * @param clusterId 聚类ID
     * @param time      聚类时间
     * @param start     分页查询开始行
     * @param limit     查询条数
     * @param sortParam 排序参数（默认时间先后排序）
     * @return 返回该类下面所以告警信息
     */
    public List<AlarmInfo> detailClusteringSearch(String clusterId, String time, int start, int limit, String sortParam) {
        List<AlarmInfo> alarmInfoList = hBaseDao.detailClusteringSearch(clusterId, time);
        if (IsEmpty.strIsRight(sortParam)) {
            SortParam sortParams = ListUtils.getOrderStringBySort(sortParam);
            ListUtils.sort(alarmInfoList, sortParams.getSortNameArr(), sortParams.getIsAscArr());
        }
        int total = alarmInfoList.size();
        if (start > -1 && start <= total) {
            if ((start + limit) > total) {
                return alarmInfoList.subList(start, total);
            } else {
                return alarmInfoList.subList(start, start + limit);
            }
        } else {
            LOG.info("start or limit out of index");
            return null;
        }
    }

    /**
     * 查询单个聚类详细信息(告警ID)
     *
     * @param clusterId 聚类ID
     * @param time      聚类时间
     * @param start     分页查询开始行
     * @param limit     查询条数
     * @param sortParam 排序参数（默认时间先后排序）
     * @return 返回该类下面所以告警信息
     */
    public List<Integer> detailClusteringSearch_v1(String clusterId, String time, int start, int limit, String sortParam) {
        SearchHit[] results = elasticSearchDao.detailClusteringSearch_v1(clusterId, time, start, limit);
        List<Integer> alarmIdList = new ArrayList<>();
        if (results != null && results.length > 0) {
            for (SearchHit result : results) {
                int alarmTime = (int) result.getSource().get(DynamicTable.ALARM_ID);
                alarmIdList.add(alarmTime);
            }
        } else {
            LOG.info("no data get from es");
        }
        return alarmIdList;
    }

    /**
     * delete a clustering
     *
     * @param clusterIdList clusteringId include region information
     * @param time          clustering time
     * @param flag          yes: delete the ignored clustering, no :delete the not ignored clustering
     * @return ture or false indicating whether delete is successful
     */
    public boolean deleteClustering(List<String> clusterIdList, String time, String flag) {
        if (clusterIdList != null && time != null) {
            String clusteringId = clusterIdList.get(0);
            String region = clusteringId.split("-")[0];
            byte[] colName;
            if (flag.toLowerCase().equals("yes")) {
                colName = ClusteringTable.ClUSTERINGINFO_COLUMN_NO;
            } else if (flag.toLowerCase().equals("no")) {
                colName = ClusteringTable.ClUSTERINGINFO_COLUMN_YES;
            } else {
                LOG.info("flag is error, it must be yes or no");
                return false;
            }
            List<ClusteringAttribute> clusteringAttributeList = hBaseDao.getClustering(region, time, colName);
            for (String clusterId : clusterIdList) {
                Iterator<ClusteringAttribute> iterator = clusteringAttributeList.iterator();
                ClusteringAttribute clusteringAttribute;
                while (iterator.hasNext()) {
                    clusteringAttribute = iterator.next();
                    if (clusterId.equals(clusteringAttribute.getClusteringId())) {
                        iterator.remove();
                    }
                }
            }
            return hBaseDao.putClustering(region, time, colName, clusteringAttributeList);
        }
        return false;
    }

    /**
     * ignore a clustering
     *
     * @param clusterIdList cluteringId include region information
     * @param time          clutering time
     * @param flag          yes is ignore, no is not ignore
     * @return
     */
    public boolean ignoreClustering(List<String> clusterIdList, String time, String flag) {
        if (clusterIdList != null && time != null) {
            String clusteringId = clusterIdList.get(0);
            String region = clusteringId.split("-")[0];
            byte[] colNameSrc;
            byte[] colNameDes;
            if (flag.toLowerCase().equals("yes")) {
                colNameSrc = ClusteringTable.ClUSTERINGINFO_COLUMN_YES;
                colNameDes = ClusteringTable.ClUSTERINGINFO_COLUMN_NO;
            } else if (flag.toLowerCase().equals("no")) {
                colNameSrc = ClusteringTable.ClUSTERINGINFO_COLUMN_NO;
                colNameDes = ClusteringTable.ClUSTERINGINFO_COLUMN_YES;
            } else {
                LOG.info("flag is error, it must be yes or no");
                return false;
            }
            List<ClusteringAttribute> listSrc = hBaseDao.getClustering(region, time, colNameSrc);
            List<ClusteringAttribute> listDes = hBaseDao.getClustering(region, time, colNameDes);
            //yes 表示数据需要忽略（HBase表中存入"n"列），no 表示数据不需要忽略（HBase表中存入"y"列）
            for (String clusterId : clusterIdList) {
                Iterator<ClusteringAttribute> iterator = listSrc.iterator();
                ClusteringAttribute clusteringAttribute;
                while (iterator.hasNext()) {
                    clusteringAttribute = iterator.next();
                    if (clusterId.equals(clusteringAttribute.getClusteringId())) {
                        clusteringAttribute.setFlag(flag);
                        listDes.add(clusteringAttribute);
                        iterator.remove();
                    }
                }
            }
            boolean booSrc = hBaseDao.putClustering(region, time, colNameSrc, listSrc);
            boolean booDes = hBaseDao.putClustering(region, time, colNameDes, listDes);
            return booSrc && booDes;
        }
        return false;
    }

    /**
     * get detail Clustering from HBase
     *
     * @param clusterId clustering id
     * @param time      clustering time
     * @param start     index start
     * @param limit     count of data
     * @param sortParam the parameters of sort
     * @return
     */
    @Deprecated
    public List<Integer> detailClusteringSearch_Hbase(String clusterId, String time, int start, int limit, String sortParam) {
        List<Integer> alarmInfoList = hBaseDao.detailClusteringSearch_Hbase(clusterId, time);
        if (IsEmpty.strIsRight(sortParam)) {
            SortParam sortParams = ListUtils.getOrderStringBySort(sortParam);
            ListUtils.sort(alarmInfoList, sortParams.getSortNameArr(), sortParams.getIsAscArr());
        }
        if (start > -1) {
            if ((start + limit) > alarmInfoList.size() - 1) {
                return alarmInfoList.subList(start, alarmInfoList.size());
            } else {
                return alarmInfoList.subList(start, start + limit);
            }
        } else {
            LOG.info("start must bigger than -1");
            return null;
        }
    }
}
