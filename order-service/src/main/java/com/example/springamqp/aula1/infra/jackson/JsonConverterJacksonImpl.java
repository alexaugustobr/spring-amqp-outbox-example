package com.example.springamqp.aula1.infra.jackson;

import com.example.springamqp.aula1.core.JsonConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JsonConverterJacksonImpl implements JsonConverter {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public String toJson(Object object) {
        try {
            return this.objectMapper.writeValueAsString(object);
        } catch (Exception var3) {
            throw new JsonConverterException("Error writing to JSON", var3);
        }
    }

    @Override
    public <T> T readValue(String json, Class<T> clazz) {
        try {
            return this.objectMapper.readValue(json, clazz);
        } catch (Exception var4) {
            throw new JsonConverterException("Error reading from JSON", var4);
        }
    }

}
