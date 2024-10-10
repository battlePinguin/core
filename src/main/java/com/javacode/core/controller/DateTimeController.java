package com.javacode.core.controller;
import com.javacode.core.dto.DateTimeRecord;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/datetime")
public class DateTimeController {

    @GetMapping
    public DateTimeRecord getCurrentDateTime() {
        return new DateTimeRecord(LocalDateTime.now());
    }
}
