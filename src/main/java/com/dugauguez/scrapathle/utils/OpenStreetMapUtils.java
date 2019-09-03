package com.dugauguez.scrapathle.utils;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class OpenStreetMapUtils {

    @Autowired
    RestTemplate restTemplate;

    @Value("${openstreetmap.uri.base}")
    private String host;

    private String callWs(String url) {
        return restTemplate.getForObject(url, String.class);
    }

    private String buildHttpQuery(String[] addressFields) {

        StringBuffer query = new StringBuffer();
        query.append(host).append("/search?q=");

        for (int i = 0; i < addressFields.length; i++) {
            query.append(addressFields[i]);
            if (i < (addressFields.length - 1)) {
                query.append("+");
            }
        }
        query.append("&format=json&addressdetails=1");

        log.debug("Query : {}", query);

        return query.toString();

    }

    private Map<String, Double> buildHttpResponse(String queryResult) {

        if (queryResult == null) {
            return null;
        }

        Map<String, Double> res = new HashMap<>();

        Object obj = JSONValue.parse(queryResult);
        log.debug("obj = {}", obj);

        if (obj instanceof JSONArray) {
            JSONArray array = (JSONArray) obj;
            if (!array.isEmpty()) {
                JSONObject jsonObject = (JSONObject) array.get(0);

                String lon = (String) jsonObject.get("lon");
                String lat = (String) jsonObject.get("lat");
                log.debug("lon = {}", lon);
                log.debug("lat = {}", lat);
                res.put("lon", Double.parseDouble(lon));
                res.put("lat", Double.parseDouble(lat));

            }
        }

        return res;

    }

    public Map<String, Double> getCoordinates(String address) {

        String[] split = address.split(" ");

        if (split.length == 0) {
            return null;
        }

        return buildHttpResponse(callWs(buildHttpQuery(split)));
    }
}
