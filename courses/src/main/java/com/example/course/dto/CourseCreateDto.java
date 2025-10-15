package com.example.course.dto;

import com.example.course.dao.entity.CourseCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseCreateDto {

    @NotBlank(message = "Title can not be empty")
    private String title;

    @NotBlank(message = "Description can not be empty")
    private String description;

    @NotBlank(message = "Author can not be empty")
    private String author;

    @NotNull
    private CourseCategory category;

}

