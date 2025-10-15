package com.example.course.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CourseRequestToUpdate {
    @NotNull(message = "Title can not be empty")
    private String title;
    @NotNull(message = "Description can not be empty")
    private String description;

}

