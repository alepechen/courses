package com.example.course.service;

import com.example.course.dao.CourseRepository;
import com.example.course.dao.UserCourseRepository;
import com.example.course.dao.entity.Course;
import com.example.course.dao.entity.CourseStatus;
import com.example.course.dao.entity.UserCourse;
import com.example.course.dto.CourseFilterDto;
import com.example.course.spec.CourseSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    private final UserCourseRepository userCourseRepository;

    @Transactional(readOnly = true)
    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Course> findById(Long id) {
        return courseRepository.findByIdWithLessons(id);
    }

    @Transactional(readOnly = true)
    public Optional<Course> findCourseById(Long id) {
        return courseRepository.findById(id);
    }

    @Transactional
    public Course save(Course course) {
        return courseRepository.save(course);
    }

    @Transactional
    public void deleteById(Long courseId) {
        courseRepository.deleteById(courseId);
    }

    @Transactional(readOnly = true)
    public List<Course> findByPrefix(String titlePrefix) {
        return courseRepository.findByTitleLike(titlePrefix + "%");
    }

    @Transactional
    public void startCourse(Long courseId, Long userId) {
        UserCourse userCourse = new UserCourse(userId, courseId, CourseStatus.STARTED);
        userCourseRepository.save(userCourse);
    }

    @Transactional
    public void updateCourseStatus(Long userId, Long courseId, CourseStatus newStatus) {
        UserCourse userCourse = userCourseRepository.findByIdUserIdAndIdCourseId(userId, courseId).orElseThrow(() -> new EntityNotFoundException("UserCourse not found for userId " + userId + " and courseId " + courseId));
        userCourse.setStatus(newStatus);
        userCourseRepository.save(userCourse);
    }

    @Transactional
    public Page<Course> getCourses(CourseFilterDto request) {
        Pageable pageable = buildPageable(request);
        Specification<Course> spec = CourseSpecification.filter(request);
        return courseRepository.findAll(spec, pageable);
    }

    private Pageable buildPageable(CourseFilterDto request) {
        int page = Math.max(request.getPage(), 0);
        int size = request.getSize() > 0 ? request.getSize() : 3;
        String sortBy = request.getSortBy() != null ? request.getSortBy() : "userCount";
        String direction = request.getDirection() != null ? request.getDirection() : "desc";
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        return PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
    }
}

