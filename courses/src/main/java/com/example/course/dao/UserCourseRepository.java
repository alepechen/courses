package com.example.course.dao;

import com.example.course.dao.entity.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserCourseRepository extends JpaRepository<UserCourse, Long> {
    Optional<UserCourse> findByIdUserIdAndIdCourseId(Long userId, Long courseId);

    List<UserCourse> findByIdCourseId(Long courseId);

    List<UserCourse> findByIdUserId(Long userId);
}
