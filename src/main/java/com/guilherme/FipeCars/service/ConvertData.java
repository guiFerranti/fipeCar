package com.guilherme.FipeCars.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConvertData implements IConvertData{
    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public <T> T convertData(String json, Class<T> classe) {
        try {
            return objectMapper.readValue(json, classe);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro in getData: " + e.getMessage());
        }
    }
}
