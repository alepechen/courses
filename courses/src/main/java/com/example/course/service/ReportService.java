package com.example.course.service;

import com.example.course.dao.LessonRepository;
import com.example.course.dao.UserCourseRepository;
import com.example.course.dao.entity.Lesson;
import com.example.course.dao.entity.UserCourse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final LessonRepository lessonRepository;

    private final UserCourseRepository userCourseRepository;

    public List<UserCourse> findUserCoursesByCourseId(Long courseId) {
        return userCourseRepository.findByIdCourseId(courseId);
    }

    public List<UserCourse> findUserCoursesByUserId(Long userId) {
        return userCourseRepository.findByIdUserId(userId);
    }

    @Transactional(readOnly = true)
    public Long getUserCompletedLessonsCount(Long userId, Long courseId) {
        return lessonRepository.countLessonsByUserAndCourse(userId, courseId);
    }

    @Transactional(readOnly = true)
    public List<Lesson> findUserCompletedLessons(Long userId, Long courseId) {
        return lessonRepository.findLessonsByUserAndCourse(userId, courseId);
    }

    @Transactional(readOnly = true)
    public double getCourseCompletionPercentage(Long completedCount, Long courseId) {
        List<Lesson> allLessonsInCourse = lessonRepository.findByCourseId(courseId);
        if (allLessonsInCourse.isEmpty()) {
            return 0.0;
        }
        int total = allLessonsInCourse.size();
        return (double) completedCount / total * 100.0;
    }

}
