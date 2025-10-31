package com.example.course.dao;

import com.example.course.dao.entity.Course;
import com.example.course.dao.entity.CourseCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> , JpaSpecificationExecutor<Course> {

    List<Course> findByTitleLike(String titlePrefix);

    @EntityGraph(value = "joinLessons")
    @Query("SELECT c FROM Course c WHERE c.id = :id")
    Optional<Course> findByIdWithLessons(@Param("id") Long id);

    @EntityGraph(value = "noJoins")
    @Query("SELECT c FROM Course c")
    List<Course> findAll();

}
