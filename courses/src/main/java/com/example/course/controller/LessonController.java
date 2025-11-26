package com.example.course.controller;

import com.example.course.dao.entity.Lesson;
import com.example.course.dto.LessonDto;
import com.example.course.dto.LessonRequest;
import com.example.course.dto.MessageResponse;
import com.example.course.mapper.LessonMapper;
import com.example.course.service.LessonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@Api
public class LessonController {

    private final LessonService service;
    private final LessonMapper mapper;

    @GetMapping("/lessons/{id}")
    @ApiOperation("Get lesson")
    public ResponseEntity<LessonDto> findById(@PathVariable Long id) {
        return service.findById(id).map(lesson -> ResponseEntity.ok(mapper.toDto(lesson))).orElseThrow(() -> new NoSuchElementException("Lesson not found for ID: " + id));

    }

    @PostMapping("admin/lessons")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("Create lesson")
    public ResponseEntity<LessonDto> create(@RequestBody @Valid LessonRequest lessonDto) {
        Lesson persistedLesson = service.save(mapper.toEntity(lessonDto));
        service.addLessonToCourse(persistedLesson);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(persistedLesson));
    }

    @PutMapping("admin/lessons/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("Update lesson")
    public ResponseEntity<LessonDto> update(@PathVariable Long id, @RequestBody LessonRequest request) {
        Lesson lesson = service.findById(id).orElseThrow(() -> new NoSuchElementException("Lesson not found for ID: " + id));
        lesson.setTitle(request.getTitle());
        lesson.setText(request.getText());
        Lesson updatedLesson = service.save(lesson);
        return ResponseEntity.ok(mapper.toDto(updatedLesson));
    }

    @DeleteMapping("admin/lessons/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("Delete lesson")
    public ResponseEntity<MessageResponse> deleteLesson(@PathVariable Long id) {
        service.deleteLessonFromCourse(id);
        return ResponseEntity.ok(new MessageResponse("Lesson deleted successfully."));
    }

    @PutMapping("user/lessons/{id}/complete")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation("Complete lesson")
    public ResponseEntity<MessageResponse> completeLesson(@PathVariable Long id, @RequestParam("userId") Long userId) {
        service.completeLesson(id, userId);
        return ResponseEntity.ok(new MessageResponse("Lesson completed successfully."));
    }
}



