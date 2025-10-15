package com.example.course.service;

import com.example.course.dao.CourseRepository;
import com.example.course.dao.LessonRepository;
import com.example.course.dao.UserRepository;
import com.example.course.dao.entity.Course;
import com.example.course.dao.entity.Lesson;
import com.example.course.dao.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Optional<Lesson> findById(Long id) {
        return lessonRepository.findById(id);
    }

    @Transactional
    public Lesson save(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    @Transactional
    public void addLessonToCourse(Lesson lesson) {
        Long courseId = lesson.getCourse().getId();
        Course course = courseRepository.findById(courseId).orElseThrow();
        course.addLesson(lesson);
        lessonRepository.save(lesson);
    }

    @Transactional
    public void deleteLessonFromCourse(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow();
        Long courseId = lesson.getCourse().getId();
        Course course = courseRepository.findById(courseId).orElseThrow();
        course.getLessons().remove(lesson);
        courseRepository.save(course);
        lessonRepository.deleteById(lessonId);
    }

    @Transactional
    public void completeLesson(Long lessonId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));

        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new EntityNotFoundException("Lesson not found"));

        user.completeLesson(lesson);
        userRepository.save(user);
    }
}

