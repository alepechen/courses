package com.example.course.mapper;

import com.example.course.dao.entity.Course;
import com.example.course.dao.entity.Lesson;
import com.example.course.dto.LessonDto;
import com.example.course.dto.LessonRequest;
import com.example.course.dto.LessonSummaryDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LessonMapper {
    default Lesson toEntity(LessonRequest source) {
        if (source == null) {
            return null;
        }

        Lesson lesson = new Lesson();
        lesson.setTitle(source.getTitle());
        lesson.setText(source.getText());

        if (source.getCourseId() != null) {
            Course course = new Course();
            course.setId(source.getCourseId());
            lesson.setCourse(course);
        }

        return lesson;
    }

    default LessonSummaryDto toSummaryDto(Lesson source) {
        if (source == null) {
            return null;
        }

        LessonSummaryDto dto = new LessonSummaryDto();
        dto.setId(source.getId());
        dto.setTitle(source.getTitle());

        return dto;
    }

    default LessonDto toDto(Lesson source) {
        if (source == null) {
            return null;
        }

        LessonDto dto = new LessonDto();
        dto.setId(source.getId());
        dto.setTitle(source.getTitle());
        dto.setText(source.getText());

        if (source.getCourse() != null) {
            dto.setCourseId(source.getCourse().getId());
        }

        return dto;
    }
}

