package com.example.course.dao;

import com.example.course.dao.entity.Rating;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface RatingRepository extends CrudRepository<Rating, Long> {

    @Query("SELECT AVG(r.score) FROM Rating r WHERE r.course.id = :courseId")
    Double findAverageRatingByCourseId(Long courseId);
}

