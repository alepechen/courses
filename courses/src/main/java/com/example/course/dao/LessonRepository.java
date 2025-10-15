package com.example.course.dao;

import com.example.course.dao.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    @Query("SELECT COUNT(l) FROM User u JOIN u.lessons l WHERE u.id = :userId AND l.course.id = :courseId")
    Long countLessonsByUserAndCourse(@Param("userId") Long userId, @Param("courseId") Long courseId);

    @Query("SELECT l FROM User u JOIN u.lessons l WHERE u.id = :userId AND l.course.id = :courseId")
    List<Lesson> findLessonsByUserAndCourse(@Param("userId") Long userId, @Param("courseId") Long courseId);

    List<Lesson> findByCourseId(Long courseId);

}
