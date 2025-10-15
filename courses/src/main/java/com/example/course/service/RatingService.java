package com.example.course.service;

import com.example.course.dao.RatingRepository;
import com.example.course.dao.entity.Rating;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;

    public Double getCourseWithRating(Long courseId) {
        return ratingRepository.findAverageRatingByCourseId(courseId);
    }

    public Rating addRating(Rating rating) {
        return ratingRepository.save(rating);
    }
}
