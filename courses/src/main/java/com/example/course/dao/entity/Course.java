package com.example.course.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "courses", schema = "public")
@NamedEntityGraph(name = "joinLessons", attributeNodes = {@NamedAttributeNode("lessons")})
@NamedEntityGraph(name = "noJoins")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long id;

    @Column(name = "course_title")
    private String title;

    @Column(name = "course_description")
    private String description;

    @Column(name = "course_author")
    private String author;

    @CreatedDate
    @Column(name = "date_created")
    private LocalDateTime createdAt;

    @Column(name = "change_author")
    private String changeAuthor;

    @LastModifiedDate
    @Column(name = "date_changed")
    private LocalDateTime changedAt;

    @Enumerated(EnumType.STRING)
    private CourseCategory category;

    @OneToMany(mappedBy = "course", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Lesson> lessons;

    @Formula("(SELECT COUNT(*) FROM user_courses uc WHERE uc.course_id = {alias}.course_id)")
    private Integer userCount;

    public void addLesson(Lesson lesson) {
        lesson.setCourse(this);
        this.lessons.add(lesson);
    }

}
