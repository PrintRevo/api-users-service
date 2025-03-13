package com.printrevo.tech.userservice.platform.db;

import com.printrevo.tech.userservice.entities.core.dto.DataEntry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Collections;
import java.util.Map;

import static java.util.Objects.isNull;

@Slf4j
@Converter
public final class PgJsonbToMapConverter implements AttributeConverter<Map<String, DataEntry>, String> {

    private final ObjectMapper objectMapper;

    public PgJsonbToMapConverter() {
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Override
    @SneakyThrows
    public String convertToDatabaseColumn(Map<String, DataEntry> mjo) {
        if (isNull(mjo)) return null;
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(mjo);
    }

    @Override
    public Map<String, DataEntry> convertToEntityAttribute(String dbData) {
        if (isNull(dbData) || dbData.equals("null")) return Collections.emptyMap();
        try {
            return objectMapper.readValue(dbData, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            return Collections.emptyMap();
        }
    }
}

