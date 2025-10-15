package com.example.course.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseFilterDto {
    private String category;
    private String author;
    private String sortBy;
    private String direction;
    private int page;
    private int size;
}

