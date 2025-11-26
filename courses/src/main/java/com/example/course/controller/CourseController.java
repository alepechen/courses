package com.example.course.controller;

import com.example.course.dao.entity.Course;
import com.example.course.dao.entity.CourseStatus;
import com.example.course.dao.entity.Rating;
import com.example.course.dto.*;
import com.example.course.mapper.CourseMapper;
import com.example.course.mapper.LessonMapper;
import com.example.course.mapper.RatingMapper;
import com.example.course.service.CourseService;
import com.example.course.service.RatingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNullElse;

@RestController
@RequiredArgsConstructor
@Api
public class CourseController {

    private final CourseService service;
    private final CourseMapper mapper;
    private final RatingService ratingService;
    private final RatingMapper ratingMapper;
    private final LessonMapper lessonMapper;

    @GetMapping("/courses/data")
    @ApiOperation("Get courses")
    public ResponseEntity<List<CourseDto>> findAll() {
        List<CourseDto> dtoList = service.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
        if (dtoList.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(dtoList);
        }
    }

    @GetMapping("/courses/{id}")
    @ApiOperation("Get course")
    public ResponseEntity<CourseWithLessons> findById(@PathVariable Long id) {
        return service.findById(id).map(course -> {
            CourseWithLessons dto = mapper.toDtoWithLessons(course, lessonMapper);
            Double average = ratingService.getCourseWithRating(id);
            average = average != null ? average : 0.0;
            dto.setAverageRating(average);
            return ResponseEntity.ok(dto);
        }).orElseThrow(() -> new NoSuchElementException("Course not found for ID: " + id));

    }

    @GetMapping("/courses/find")
    @ApiOperation("Find courses by title")
    public ResponseEntity<List<CourseDto>> getCoursesByTitlePrefix(@RequestParam(name = "titlePrefix", required = false) String titlePrefix) {
        List<CourseDto> dtoList = service.findByPrefix(requireNonNullElse(titlePrefix, "")).stream().map(mapper::toDto).collect(Collectors.toList());
        if (dtoList.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(dtoList);
        }
    }

    @PostMapping("admin/courses")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("Create course")
    public ResponseEntity<CourseDto> create(@RequestBody @Valid CourseCreateDto courseCreateDto) {
        Course persistedCourse = service.save(mapper.toEntity(courseCreateDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(persistedCourse));
    }

    @PutMapping("admin/courses/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("Update course")
    public ResponseEntity<CourseDto> update(@PathVariable Long id, @RequestBody CourseRequestToUpdate request) {
        Course course = service.findById(id).orElseThrow(() -> new NoSuchElementException("Course not found for ID: " + id));
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        Course updatedCourse = service.save(course);
        return ResponseEntity.ok(mapper.toDto(updatedCourse));
    }

    @DeleteMapping("admin/courses/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("Delete course")
    public ResponseEntity<MessageResponse> deleteCourseById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok(new MessageResponse("Course deleted successfully."));
    }

    @PostMapping("user/courses/{courseId}/enroll")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation("Start course")
    public ResponseEntity<MessageResponse> startCourse(@PathVariable("courseId") Long courseId, @RequestParam("userId") Long userId) {
        service.startCourse(courseId, userId);
        return ResponseEntity.ok(new MessageResponse("Course started successfully."));
    }

    @PostMapping("user/courses/{courseId}/complete")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation("Complete course")
    public ResponseEntity<MessageResponse> completeCourse(@PathVariable("courseId") Long courseId, @RequestParam("userId") Long userId) {
        service.updateCourseStatus(userId, courseId, CourseStatus.COMPLETED);
        return ResponseEntity.ok(new MessageResponse("Course completed successfully."));
    }

    @DeleteMapping("user/courses/{courseId}/remove")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation("Leave course")
    public ResponseEntity<MessageResponse> deleteCourse(@PathVariable("courseId") Long courseId, @RequestParam("userId") Long userId) {
        service.updateCourseStatus(userId, courseId, CourseStatus.REMOVED);
        return ResponseEntity.ok(new MessageResponse("Course left successfully."));
    }

    @PostMapping("user/courses/rating")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation("Add rating")
    public ResponseEntity<RatingDto> addRating(@RequestBody RatingDto ratingDto) {
        Rating persistedRating = ratingService.addRating(ratingMapper.toEntity(ratingDto));
        return ResponseEntity.ok(ratingMapper.toDto(persistedRating));
    }

    @PostMapping("/courses/filter")
    @ApiOperation("Filter courses")
    public ResponseEntity<Page<CourseDto>> filterCourses(@RequestBody CourseFilterDto filterRequest) {
        Page<CourseDto> page = service.getCourses(filterRequest).map(mapper::toDto);
        if (page.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(page);
        }
    }
}
