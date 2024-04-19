package io.soffa.foundation.service.data.jpa;


import io.soffa.foundation.commons.Mappers;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Map;

@Converter
public class MapConverter implements AttributeConverter<Map<String, Object>, String> {

    @Override
    public String convertToDatabaseColumn(Map<String, Object> map) {
        return Mappers.JSON_FULLACCESS.serialize(map);
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String dbData) {
        return Mappers.JSON_FULLACCESS.toMap(dbData);
    }


}
