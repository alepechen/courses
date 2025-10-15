package com.example.course.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "user_courses")

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCourse {

    @EmbeddedId
    private UserCourseId id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseStatus status;

    public UserCourse(Long userId, Long courseId, CourseStatus status) {
        this.id = new UserCourseId(userId, courseId);
        this.status = status;
    }

    public Long getUserId() {
        return id.getUserId();
    }

    public Long getCourseId() {
        return id.getCourseId();
    }
}

