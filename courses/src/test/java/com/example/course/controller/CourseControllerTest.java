package com.example.course.controller;

import com.example.course.dao.entity.Course;
import com.example.course.dao.entity.CourseStatus;
import com.example.course.dao.entity.Rating;
import com.example.course.dto.CourseCreateDto;
import com.example.course.dto.CourseRequestToUpdate;
import com.example.course.dto.CourseWithLessons;
import com.example.course.dto.RatingDto;
import com.example.course.mapper.CourseMapper;
import com.example.course.mapper.LessonMapper;
import com.example.course.mapper.RatingMapper;
import com.example.course.service.CourseService;
import com.example.course.service.RatingService;
import com.example.course.testutil.TestDataFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CourseControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CourseService courseService;
    @MockBean
    private CourseMapper courseMapper;
    @MockBean
    private RatingService ratingService;
    @MockBean
    private RatingMapper ratingMapper;
    @MockBean
    private LessonMapper lessonMapper;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getCourseShouldReturn200() throws Exception {
        Long id = 1L;
        Course course = TestDataFactory.sampleMockCourse(id);
        CourseWithLessons dto = TestDataFactory.sampleCourseWithLessons(id, 4.5);
        when(courseService.findById(id)).thenReturn(Optional.of(course));
        when(ratingService.getCourseWithRating(id)).thenReturn(4.5);
        when(courseMapper.toDtoWithLessons(course, lessonMapper)).thenReturn(dto);

        mockMvc.perform(get("/course/" + id)).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(id)).andExpect(jsonPath("$.title", containsString("CourseWithLessons"))).andExpect(jsonPath("$.averageRating").value(4.5));
    }

    @Test
    void getCoursesByTitlePrefixShouldReturn200() throws Exception {
        List<Course> courses = List.of(TestDataFactory.sampleMockCourse(1L));
        when(courseService.findByPrefix("Mo")).thenReturn(courses);
        mockMvc.perform(get("/course/find").param("titlePrefix", "Mo")).andExpect(status().isOk());
        verify(courseService, times(1)).findByPrefix("Mo");
    }

    @WithMockUser(username = "test", roles = {"ADMIN"})
    @Test
    void createCourseShouldReturn200IfValid() throws Exception {
        CourseCreateDto courseRequestToCreate = TestDataFactory.sampleCourseCreateDto();
        mockMvc.perform(post("/admin/course").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(courseRequestToCreate))).andDo(print()).andExpect(status().isCreated());
    }

    @WithMockUser(username = "test", roles = {"ADMIN"})
    @Test
    void updateCourseShouldReturn200IfValid() throws Exception {
        CourseRequestToUpdate updateDto = TestDataFactory.sampleCourseUpdateDto();
        Course existingCourse = TestDataFactory.sampleMockCourse(1L);

        when(courseService.findById(1L)).thenReturn(Optional.of(existingCourse));
        when(courseService.save(any(Course.class))).thenReturn(existingCourse);
        mockMvc.perform(put("/admin/course/1").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updateDto))).andExpect(status().isOk());
    }

    @WithMockUser(username = "test", roles = {"USER"})
    @Test
    void assignUserToCourseShouldReturn200() throws Exception {
        Long courseId = 1L;
        Long userId = 123L;
        doNothing().when(courseService).startCourse(courseId, userId);

        mockMvc.perform(post("/user/{courseId}/enroll", courseId).param("userId", userId.toString())).andDo(print()).andExpect(status().isOk());
    }

    @WithMockUser(username = "test", roles = {"USER"})
    @Test
    void completeCourseByUserShouldReturn200() throws Exception {
        Long courseId = 1L;
        Long userId = 123L;
        doNothing().when(courseService).updateCourseStatus(courseId, userId, CourseStatus.COMPLETED);
        mockMvc.perform(post("/user/{courseId}/complete", courseId).param("userId", userId.toString())).andDo(print()).andExpect(status().isOk());
    }

    @WithMockUser(username = "test", roles = {"USER"})
    @Test
    void addRatingShouldReturn200() throws Exception {
        RatingDto ratingDto = TestDataFactory.sampleRatingDto();
        Rating savedRating = TestDataFactory.sampleRating();
        when(ratingService.addRating(any(Rating.class))).thenReturn(savedRating);

        mockMvc.perform(post("/user/course/rating").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(ratingDto))).andExpect(status().isOk());

    }

}
