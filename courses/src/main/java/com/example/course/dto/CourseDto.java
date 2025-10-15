package com.example.course.dto;

import com.example.course.dao.entity.CourseCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto {

    private Long id;

    @NotBlank(message = "Title can not be empty")
    private String title;

    @NotBlank(message = "Description can not be empty")
    private String description;

    private String author;

    private LocalDateTime createdAt;

    private LocalDateTime changedAt;
    @NotNull
    private CourseCategory category;

    private String status;

}

