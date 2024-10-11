package com.javacode.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

@Configuration
public class CustomObjectMapperConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd#HH:mm:ss:SSS", Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));

        objectMapper.setDateFormat(dateFormat);
        return objectMapper;
    }
}
