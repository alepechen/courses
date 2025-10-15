package com.example.course.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class LessonRequest {
    @NotNull(message = "Title can not be empty")
    private String title;

    @NotNull(message = "Description can not be empty")
    private String text;

    @NotNull(message = "Course id can not be empty")
    private Long courseId;
}
