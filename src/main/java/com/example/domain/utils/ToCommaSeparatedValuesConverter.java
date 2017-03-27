package com.example.domain.utils;


import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class ToCommaSeparatedValuesConverter implements AttributeConverter<String[], String> {
    private static final String COMMA_SEPARATOR = ", ";

    @Override
    public String convertToDatabaseColumn(String[] attribute) {
        return Optional.ofNullable(attribute)
                .map(a -> Arrays.stream(a).collect(Collectors.joining(COMMA_SEPARATOR)))
                .orElse("");
    }

    @Override
    public String[] convertToEntityAttribute(String dbData) {
        return Optional.ofNullable(dbData)
                .map(v -> v.split(COMMA_SEPARATOR))
                .orElse(new String[0]);
    }
}
