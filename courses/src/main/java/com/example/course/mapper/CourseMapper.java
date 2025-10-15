package com.example.course.mapper;

import com.example.course.dao.entity.Course;
import com.example.course.dao.entity.Lesson;
import com.example.course.dto.CourseCreateDto;
import com.example.course.dto.CourseDto;
import com.example.course.dto.CourseWithLessons;
import com.example.course.dto.LessonSummaryDto;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = LessonMapper.class)
public interface CourseMapper {

    CourseDto toDto(Course course); // No second argument needed

    Course toEntity(CourseCreateDto dto);

    @Mapping(target = "lessons", expression = "java(mapLessons(course.getLessons(), lessonMapper))")
    CourseWithLessons toDtoWithLessons(Course course, @Context LessonMapper lessonMapper);

    default List<LessonSummaryDto> mapLessons(List<Lesson> lessons, @Context LessonMapper lessonMapper) {
        if (lessons == null) return null;
        return lessons.stream().map(lessonMapper::toSummaryDto).collect(Collectors.toList());
    }
}
