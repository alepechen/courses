package com.example.course.testutil;

import com.example.course.service.CourseService;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@DataJpaTest
@ActiveProfiles("h2")
@Import(CourseService.class)
@ComponentScan(basePackages = "com.example.course.mapper")
public @interface DBTest {

}
