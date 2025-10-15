package com.example.course.service;

import com.example.course.dto.ApiErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class HandleErrorService {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiErrorDto> noSuchElementExceptionHandler(NoSuchElementException ex) {
        ApiErrorDto apiError = new ApiErrorDto(OffsetDateTime.now(), ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDto> handleGenericException(Exception ex) {
        ApiErrorDto apiError = new ApiErrorDto(OffsetDateTime.now(), "Internal server error: " + ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

