package com.example.course.dto;

import com.example.course.dao.entity.CourseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserEnrollmentDto {
    private Long userId;

    private Long courseId;

    private CourseStatus status;

    private double completionPercentage;
}

