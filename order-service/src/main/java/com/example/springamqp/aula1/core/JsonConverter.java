package com.example.springamqp.aula1.core;

public interface JsonConverter {
    String toJson(Object object);
    <T> T readValue(String json, Class<T> clazz);
}
