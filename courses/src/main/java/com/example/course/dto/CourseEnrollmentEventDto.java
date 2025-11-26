package com.example.course.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseEnrollmentEventDto {
    private String enrollmentId;
    private Long userId;
    private String userEmail;
    private String userName;
    private Long courseId;
    private String courseName;
}


