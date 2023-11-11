package com.guilherme.FipeCars.service;

public interface IConvertData {
    public <T> T convertData(String json, Class<T> classe);
}
