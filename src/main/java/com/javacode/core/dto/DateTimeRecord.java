package com.javacode.core.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record DateTimeRecord(
        LocalDateTime timestamp) {
}