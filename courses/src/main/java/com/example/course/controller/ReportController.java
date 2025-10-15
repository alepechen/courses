package com.example.course.controller;

import com.example.course.dao.entity.CourseStatus;
import com.example.course.dao.entity.UserCourse;
import com.example.course.dto.CourseDto;
import com.example.course.dto.LessonSummaryDto;
import com.example.course.dto.UserEnrollmentDto;
import com.example.course.mapper.CourseMapper;
import com.example.course.mapper.LessonMapper;
import com.example.course.service.CourseService;
import com.example.course.service.ReportService;
import com.example.course.utils.CsvUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Api
public class ReportController {

    private final ReportService service;
    private final LessonMapper mapper;
    private final CourseService courseService;
    private final CourseMapper courseMapper;


    @GetMapping(value = "/reports/course/{courseId}", produces = "text/csv")
    @ApiOperation("Download course report as csv")
    public ResponseEntity<ByteArrayResource> getUsersWithStatus(@PathVariable Long courseId) {
        List<UserCourse> userCourses = service.findUserCoursesByCourseId(courseId);

        List<UserEnrollmentDto> result = userCourses.stream().map(uc -> {
            Long userId = uc.getUserId();
            CourseStatus status = uc.getStatus();

            Long completedLessonCount = service.getUserCompletedLessonsCount(userId, courseId);

            double percentage = service.getCourseCompletionPercentage(completedLessonCount, courseId);

            return new UserEnrollmentDto(userId, courseId, status, percentage);
        }).collect(Collectors.toList());

        byte[] csvBytes = CsvUtil.generateCsv(result);
        ByteArrayResource resource = new ByteArrayResource(csvBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=course-report-" + courseId + ".csv");
        headers.add(HttpHeaders.CONTENT_TYPE, "text/csv");

        return ResponseEntity.ok().headers(headers).contentLength(csvBytes.length).body(resource);
    }

    @GetMapping("user/reports/course/{courseId}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation("Get user completed lessons for course")
    public ResponseEntity<List<LessonSummaryDto>> getUserWithLessons(@PathVariable("courseId") Long courseId, @RequestParam("userId") Long userId) {
        List<LessonSummaryDto> result = service.findUserCompletedLessons(userId, courseId).stream().map(mapper::toSummaryDto).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("user/reports/{userId}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation("Get user courses")
    public ResponseEntity<List<CourseDto>> getUserWithCourses(@PathVariable("userId") Long userId) {
        List<UserCourse> userCourses = service.findUserCoursesByUserId(userId);
        List<CourseDto> result = userCourses.stream().map(uc -> courseService.findCourseById(uc.getCourseId()).map(course -> {
            CourseDto dto = courseMapper.toDto(course);
            dto.setStatus(uc.getStatus().toString());
            return dto;
        })).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

}