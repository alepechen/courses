package com.example.course.controller;

import com.example.course.dao.CourseRepository;
import com.example.course.dao.LessonRepository;
import com.example.course.dao.RatingRepository;
import com.example.course.dao.UserCourseRepository;
import com.example.course.dao.entity.Course;
import com.example.course.dao.entity.Lesson;
import com.example.course.dto.LessonRequest;
import com.example.course.mapper.LessonMapper;
import com.example.course.testutil.TestDataFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class LessonControllerE2ETest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private UserCourseRepository userCourseRepository;
    @Autowired
    private LessonMapper lessonMapper;

    @BeforeEach
    void setup() {
        lessonRepository.deleteAll();
        userCourseRepository.deleteAll();
        ratingRepository.deleteAll();
        courseRepository.deleteAll();
    }

    @WithMockUser(username = "test", roles = {"ADMIN"})
    @Test
    void createLessonShouldReturn200AndSave() throws Exception {
        Course course = TestDataFactory.sampleCourse();
        course = courseRepository.save(course);
        LessonRequest lessonRequest = TestDataFactory.lessonRequest(course.getId());
        mockMvc.perform(post("/admin/lesson").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(lessonRequest))).andExpect(status().isCreated()).andExpect(jsonPath("$.title").value(lessonRequest.getTitle())).andExpect(jsonPath("$.text").value(lessonRequest.getText()));

        List<Lesson> lessons = lessonRepository.findAll();
        assertEquals(1, lessons.size());
        assertEquals(lessonRequest.getTitle(), lessons.get(0).getTitle());
    }

    @WithMockUser(username = "test", roles = {"ADMIN"})
    @Transactional
    @Test
    void deleteLessonShouldReturn200() throws Exception {
        Course course = TestDataFactory.sampleCourse();
        course = courseRepository.save(course);

        LessonRequest lessonRequest = TestDataFactory.lessonRequest(course.getId());
        Lesson lesson = lessonRepository.save(lessonMapper.toEntity(lessonRequest));
        course.setLessons(new ArrayList<>());
        course.getLessons().add(lesson);
        courseRepository.save(course);
        mockMvc.perform(delete("/admin/lesson/{id}", lesson.getId())).andExpect(status().isOk()).andExpect(jsonPath("$.message").value("Lesson deleted successfully."));

        Optional<Lesson> deletedLesson = lessonRepository.findById(lesson.getId());
        assertTrue(deletedLesson.isEmpty());

        Course updatedCourse = courseRepository.findById(course.getId()).orElseThrow();
        assertFalse(updatedCourse.getLessons().contains(lesson));
    }
}
