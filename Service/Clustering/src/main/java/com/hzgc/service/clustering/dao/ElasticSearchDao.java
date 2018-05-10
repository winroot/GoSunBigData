package com.hzgc.service.clustering.dao;

import com.hzgc.common.es.ElasticSearchHelper;
import com.hzgc.common.table.dynrepo.DynamicTable;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class ElasticSearchDao {

    private static Logger LOG = Logger.getLogger(ElasticSearchDao.class);

    private TransportClient esClient;

    public ElasticSearchDao(@Value("${es.cluster.name}") String clusterName,
                            @Value("${es.hosts}") String esHost,
                            @Value("${es.cluster.port}") String esPort) {
        this.esClient = ElasticSearchHelper.getEsClient(clusterName, esHost, Integer.parseInt(esPort));
    }

    public SearchHit[] detailClusteringSearch_v1(String clusterId, String time, int start, int limit) {
        BoolQueryBuilder totalBQ = QueryBuilders.boolQuery();
        if (clusterId != null && time != null) {
            totalBQ.must(QueryBuilders.matchPhraseQuery(DynamicTable.CLUSTERING_ID, time + "-" + clusterId));
        }

        SearchRequestBuilder searchRequestBuilder = esClient
                .prepareSearch(DynamicTable.DYNAMIC_INDEX)
                .setTypes(DynamicTable.PERSON_INDEX_TYPE)
                .addSort(DynamicTable.TIMESTAMP, SortOrder.DESC)
                .setFrom(start)
                .setSize(limit)
                .setQuery(totalBQ);
        return searchRequestBuilder.get().getHits().getHits();
    }
}
