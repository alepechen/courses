package com.example.course.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "lessons", schema = "public")

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column
    private String text;

    @Column
    private String author;

    @CreatedDate
    @Column(name = "date_created")
    private Timestamp dateCreated;

    @Column
    private String changeAuthor;

    @LastModifiedDate
    @Column(name = "date_changed")
    private Timestamp dateChanged;

    @ManyToOne(optional = false)
    @JoinColumn(name = "course_id")
    private Course course;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany
    @JoinTable(
            name = "user_lessons",
            joinColumns = @JoinColumn(name = "lesson_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnore
    private Set<User> users = new HashSet<>();
}