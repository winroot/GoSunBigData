package com.hzgc.service.clustering.dao;

import com.hzgc.common.es.ElasticSearchHelper;
import com.hzgc.common.table.dynrepo.DynamicTable;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class ElasticSearchDao {

    private TransportClient esClient;

    public ElasticSearchDao(@Value("${es.cluster.name}") String clusterName,
                            @Value("${es.hosts}") String esHost,
                            @Value("${es.cluster.port}") String esPort) {
        this.esClient = ElasticSearchHelper.getEsClient(clusterName, esHost, Integer.parseInt(esPort));
    }

    public SearchHit[] detailClusteringSearch_v1(String region, String time, int start, int limit) {
        BoolQueryBuilder totalBQ = QueryBuilders.boolQuery();
        if (region != null && time != null) {
            totalBQ.must(QueryBuilders.matchPhraseQuery(DynamicTable.CLUSTERING_ID, time + "-" + region));
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
