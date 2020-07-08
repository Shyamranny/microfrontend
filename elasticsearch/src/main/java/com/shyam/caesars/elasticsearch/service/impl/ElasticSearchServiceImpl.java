package com.shyam.caesars.elasticsearch.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shyam.caesars.core.exception.CaesarsException;
import com.shyam.caesars.elasticsearch.service.ElasticSearchService;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;

/* shyam created on 7/7/20 */
@Service
public class ElasticSearchServiceImpl implements ElasticSearchService {

    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchServiceImpl.class);

    @Value("${elasticSearch.tripsIndex}")
    private String tripsIndex;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public void saveTrips(String[] jsonArray) {

        logger.debug("Saving trip list to elastic search:{}", jsonArray.length);

        ObjectMapper mapper = new ObjectMapper();
        BulkRequest bulkRequest = new BulkRequest();
        Arrays.stream(jsonArray).forEach(a -> {

            try {

                JsonNode jsonNode = mapper.readTree(a);
                bulkRequest.add(new IndexRequest(tripsIndex).id(jsonNode.get("TripID").toString()).source(a, XContentType.JSON));

            } catch (JsonProcessingException e) {
                logger.error("Error while parsing json {}", a, e);
            }

        });

        try {
            restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            logger.debug("Saved {} trips to elastic search index: {}", jsonArray.length, tripsIndex);
        } catch (IOException e) {
            throw new CaesarsException("Exception in bulk insert");
        }

    }
}
