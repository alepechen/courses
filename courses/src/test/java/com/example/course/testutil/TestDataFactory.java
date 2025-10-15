package com.example.course.testutil;

import com.example.course.dao.entity.Course;
import com.example.course.dao.entity.CourseCategory;
import com.example.course.dao.entity.Rating;
import com.example.course.dao.entity.User;
import com.example.course.dto.*;
import java.util.Random;
import java.util.UUID;

public class TestDataFactory {
    private static final Random RANDOM = new Random();
    private static final CourseCategory[] CATEGORIES = CourseCategory.values();

    private static String randomString(String prefix) {
        return prefix + "_" + UUID.randomUUID().toString().substring(0, 8);
    }

    private static CourseCategory randomCategory() {
        return CATEGORIES[RANDOM.nextInt(CATEGORIES.length)];
    }
    public static Course sampleCourse() {
        return Course.builder()
                .title(randomString("Title"))
                .description(randomString("Description"))
                .author(randomString("Author"))
                .category(randomCategory())
                .build();
    }
    public static UserCreateDto sampleUser() {
        return new UserCreateDto(
                randomString("Username"),
                randomString("Password"),
                randomString("Email")
        );
    }

    public static Course sampleMockCourse(Long id) {
        return Course.builder()
                .id(id)
                .title(randomString("MockTitle"))
                .description(randomString("MockDescription"))
                .author(randomString("MockAuthor"))
                .category(randomCategory())
                .build();
    }

    public static CourseWithLessons sampleCourseWithLessons(Long id, Double rating) {
        CourseWithLessons dto = new CourseWithLessons();
        dto.setId(id);
        dto.setTitle(randomString("CourseWithLessons"));
        dto.setDescription(randomString("Description"));
        dto.setAuthor(randomString("Author"));
        dto.setAverageRating(rating);
        return dto;
    }
    public static CourseCreateDto sampleCourseCreateDto() {
        return new CourseCreateDto(
                randomString("CreateTitle"),
                randomString("CreateDescription"),
                randomString("CreateAuthor"),
                randomCategory()
        );
    }
    public static CourseRequestToUpdate sampleCourseUpdateDto() {
        return new CourseRequestToUpdate(
                randomString("UpdatedTitle"),
                randomString("UpdatedDescription")
        );
    }
    public static RatingDto sampleRatingDto() {
        return new RatingDto(RANDOM.nextLong(), RANDOM.nextInt(11));
    }
    public static Rating sampleRating() {
        Course course = sampleMockCourse(RANDOM.nextLong());  // reuse your course factory
        return new Rating(RANDOM.nextLong(), course, RANDOM.nextInt(11));
    }

    public static LessonRequest lessonRequest(Long courseId){
        return new LessonRequest(
                randomString("LessonTitle"),
                randomString("LessonDescription"),
                courseId
        );
    }

}
