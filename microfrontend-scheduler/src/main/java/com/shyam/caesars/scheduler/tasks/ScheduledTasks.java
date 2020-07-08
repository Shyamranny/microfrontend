package com.shyam.caesars.scheduler.tasks;

/* shyam created on 7/7/20 */

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.shyam.caesard.rest.client.CaesarsRestClient;
import com.shyam.caesars.core.exception.CaesarsException;
import com.shyam.caesars.elasticsearch.service.ElasticSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    CaesarsRestClient caesarsRestClient;

    @Autowired
    ElasticSearchService elasticSearchService;

    @Scheduled(initialDelay = 0, fixedRate = 1000 * 60 * 30)
    public void initialLoaderToDatabase(){
        log.info("{} - initial loading to database", dateFormat.format(new Date()));
        String tripList = caesarsRestClient.getTripDetails();
        log.debug("Response from service: {}", tripList);

        List<String> results = getTripsFromResponse(tripList);
        String[] itemsArray = new String[results.size()];
        itemsArray = results.toArray(itemsArray);

        elasticSearchService.saveTrips(itemsArray);
    }

    private List<String> getTripsFromResponse(String tripList) {

        List<String> trips = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();

        try {

            ArrayNode arrayNode = (ArrayNode) mapper.readTree(tripList).get("lstListTrips");
            if(arrayNode.isArray()) {
                arrayNode.forEach(s -> trips.add(s.toString()));
            }

        } catch (JsonProcessingException e) {
            throw new CaesarsException("Exception while parsing input json from service");
        }

        return trips;

    }
}
