package com.shyam.caesard.rest.client.impl;

import com.shyam.caesard.rest.client.CaesarsRestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;

/* shyam created on 7/7/20 */
@Service
public class CaesarsRestClientImpl implements CaesarsRestClient {

    private static final Logger log = LoggerFactory.getLogger(CaesarsRestClientImpl.class);


    @Autowired
    RestTemplate restTemplate;


    @Value("${caesars.getTripDetailsUrl}")
    private String getTripDetailsUrl;

    @Value("${caesars.propertyCodes}")
    private String propertyCodes;

    @Value("${caesars.emsPropertyId}")
    private String emsPropertyId;

    @Value("${caesars.authToken}")
    private String authToken;

    @Override
    public String getTripDetails() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        Calendar c = Calendar.getInstance();
        String fromDate = dateFormat.format(c.getTime());
        c.add(Calendar.DAY_OF_MONTH, 90);
        String toDate = dateFormat.format(c.getTime());

        String entity = "{\n" +
                "  \"TripId\": 0,\n" +
                "  \"Region\": \"ALL?\",\n" +
                "  \"FromDate\": \"" + fromDate + "\",\n" +
                "  \"ToDate\": \"" + toDate + "\",\n" +
                "  \"Pickup\": \"\",\n" +
                "  \"AdjacentAirport\": false,\n" +
                "  \"TripNumber\": 0,\n" +
                "  \"PropertyID\": 0,\n" +
                "  \"InventoryType\": 6,\n" +
                "  \"GuestCount\": 1,\n" +
                "  \"TemplateID\": \"\",\n" +
                "  \"PropertyCode\": \"" + propertyCodes + "\",\n" +
                "  \"ControlSegment\": 0,\n" +
                "  \"NewPlayerControlSegment\": 0,\n" +
                "  \"UserId\": \"1407\",\n" +
                "  \"LockTime\": \"08/01/2015\",\n" +
                "  \"EMSPropertyID\": \"0\",\n" +
                "  \"EMSPropertyCode\": \"" + emsPropertyId + "\",\n" +
                "  \"InventoryName\": null,\n" +
                "  \"SkyforceUserFlag\": \"Y\",\n" +
                "  \"NoServiceCheckFlag\": false,\n" +
                "  \"PickupCity\": \"\",\n" +
                "  \"TrNo1\": null,\n" +
                "  \"TrNo2\": null,\n" +
                "  \"TrNo3\": null,\n" +
                "  \"TrNo4\": null\n" +
                "}";


        HttpEntity<String> request =  new HttpEntity(entity, httpHeaders());

        String quote = restTemplate.postForObject(
                getTripDetailsUrl, request, String.class);

        return quote;
    }

    HttpHeaders httpHeaders(){

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("authorization-token", authToken);

        return headers;
    }
}
