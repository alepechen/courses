package com.example.course.dao;

import com.example.course.dao.entity.Course;
import com.example.course.dao.entity.CourseCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query(
            value = "SELECT c.* FROM courses c " +
                    "LEFT JOIN user_courses uc ON c.course_id = uc.course_id " +
                    "WHERE (:category IS NULL OR c.category = CAST(:category AS VARCHAR)) " +
                    "AND (:author IS NULL OR c.course_author = CAST(:author AS VARCHAR)) " +
                    "GROUP BY c.course_id " +
                    "ORDER BY COUNT(uc.user_id) DESC",
            countQuery = "SELECT COUNT(*) FROM courses c " +
                    "WHERE (:category IS NULL OR c.category = CAST(:category AS VARCHAR)) " +
                    "AND (:author IS NULL OR c.course_author = CAST(:author AS VARCHAR)) ",
            nativeQuery = true
    )
    Page<Course> findAllOrderByUserCountWithFilters(
            @Param("category") String category,
            @Param("author") String author,
            Pageable pageable
    );


    @Query(
            value = "SELECT c FROM Course c " +
                    "WHERE (:category IS NULL OR c.category = :category) " +
                    "AND (:author IS NULL OR c.author = :author)"
    )
    Page<Course> findByCategoryAndAuthor(
            @Param("category") CourseCategory category,
            @Param("author") String author,
            Pageable pageable
    );

    List<Course> findByTitleLike(String titlePrefix);

    @EntityGraph(value = "joinLessons")
    @Query("SELECT c FROM Course c WHERE c.id = :id")
    Optional<Course> findByIdWithLessons(@Param("id") Long id);

    @EntityGraph(value = "noJoins")
    @Query("SELECT c FROM Course c")
    List<Course> findAll();

}
