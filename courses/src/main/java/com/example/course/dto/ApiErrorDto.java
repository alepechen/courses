package com.example.course.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiErrorDto {
    private OffsetDateTime dateOccurred;
    private String message;
}

