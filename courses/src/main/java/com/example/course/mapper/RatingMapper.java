package com.example.course.mapper;

import com.example.course.dao.entity.Course;
import com.example.course.dao.entity.Rating;
import com.example.course.dto.RatingDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RatingMapper {

    default Rating toEntity(RatingDto source) {
        if (source == null) {
            return null;
        }

        Rating rating = new Rating();
        rating.setScore(source.getScore());

        if (source.getCourseId() != null) {
            Course course = new Course();
            course.setId(source.getCourseId());
            rating.setCourse(course);
        }

        return rating;
    }

    default RatingDto toDto(Rating source) {
        if (source == null) {
            return null;
        }
        RatingDto dto = new RatingDto();
        dto.setScore(source.getScore());

        if (source.getCourse() != null) {
            dto.setCourseId(source.getCourse().getId());
        }

        return dto;
    }
}

