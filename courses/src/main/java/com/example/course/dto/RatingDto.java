package com.example.course.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingDto {
    @NotNull(message = "Course can not be empty")
    private Long courseId;
    @NotNull(message = "Score can not be empty")
    private int score;
}
