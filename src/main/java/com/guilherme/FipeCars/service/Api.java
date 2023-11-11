package com.guilherme.FipeCars.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class Api {
    private final RestTemplate restTemplate = new RestTemplate();
    public String getData(String url) {

        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
            String response = responseEntity.getBody();
            return response;
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Error in API: " + e.getMessage());
        }
    }
}
