package com.example.course.service;

import com.example.course.config.H2TestConfig;
import com.example.course.dao.CourseRepository;
import com.example.course.dao.RatingRepository;
import com.example.course.dao.UserCourseRepository;
import com.example.course.dao.UserRepository;
import com.example.course.dao.entity.Course;
import com.example.course.dao.entity.CourseStatus;
import com.example.course.dao.entity.User;
import com.example.course.dao.entity.UserCourse;
import com.example.course.dto.UserCreateDto;
import com.example.course.mapper.UserMapper;
import com.example.course.producer.EnrollmentEventProducer;
import com.example.course.testutil.DBTest;
import com.example.course.testutil.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DBTest
@Import(H2TestConfig.class)
public class CourseServiceTest {
    @Autowired
    private CourseService courseService;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private UserCourseRepository userCourseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DataSource dataSource;
    @MockBean
    private EnrollmentEventProducer enrollmentEventProducer;
    @MockBean
    private OutboxService outboxService;
    @BeforeEach
    void beforeEach() {
        userCourseRepository.deleteAll();
        ratingRepository.deleteAll();
        courseRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateCourse() {
        Course testCourse = TestDataFactory.sampleCourse();
        Course savedCourse = courseService.save(testCourse);
        assertNotNull(savedCourse.getId());
        assertEquals(testCourse.getTitle(), savedCourse.getTitle());
        assertEquals(testCourse.getDescription(), savedCourse.getDescription());
        assertEquals(testCourse.getAuthor(), savedCourse.getAuthor());
        assertEquals(testCourse.getCategory(), savedCourse.getCategory());
        assertEquals(1, courseRepository.count());
    }

    @Test
    void shouldFindById() {
        Course savedCourse = courseService.save(TestDataFactory.sampleCourse());
        Optional<Course> result = courseService.findById(savedCourse.getId());

        assertTrue(result.isPresent());
        assertEquals(savedCourse.getTitle(), result.get().getTitle());
        assertEquals(savedCourse.getDescription(), result.get().getDescription());
        assertEquals(savedCourse.getId(), result.get().getId());
    }

    @Test
    void coursesByTitlePrefix() {
        courseService.save(TestDataFactory.sampleCourse());
        List<Course> courses = courseService.findByPrefix("Ti");
        assertFalse(courses.isEmpty());
        assertTrue(courses.stream().anyMatch(c -> c.getTitle().startsWith("Ti")));
    }

    @Test
    void startCourse() {
        UserCreateDto sampleUserDto = TestDataFactory.sampleUser();
        User savedUser = userRepository.save(userMapper.toEntity(sampleUserDto));
        Course savedCourse = courseService.save(TestDataFactory.sampleCourse());
        UserCourse userCourse = new UserCourse(savedUser.getId(), savedCourse.getId(), CourseStatus.STARTED);
        userCourseRepository.save(userCourse);
        assertEquals(1, userCourseRepository.count());
    }

    @Test
    void updateCourseStatus() {
        UserCreateDto sampleUserDto = TestDataFactory.sampleUser();
        User savedUser = userRepository.save(userMapper.toEntity(sampleUserDto));
        Course savedCourse = courseService.save(TestDataFactory.sampleCourse());
        userCourseRepository.save(new UserCourse(savedUser.getId(), savedCourse.getId(), CourseStatus.STARTED));
        courseService.updateCourseStatus(savedUser.getId(), savedCourse.getId(), CourseStatus.COMPLETED);
        Optional<UserCourse> userCourse = userCourseRepository.findByIdUserIdAndIdCourseId(1L, savedCourse.getId());
        assertTrue(userCourse.isPresent());
        assertEquals(CourseStatus.COMPLETED, userCourse.get().getStatus());
    }
}
