package com.javacode.core.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record DateTimeRecord(
        @JsonFormat(
                shape = JsonFormat.Shape.STRING,
                pattern = "yyyy:MM:dd#HH:mm:ss",
                timezone = "Europe/Moscow",
                locale = "en"
        )
        LocalDateTime timestamp) {

}