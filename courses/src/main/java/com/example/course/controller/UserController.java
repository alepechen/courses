package com.example.course.controller;

import com.example.course.dao.entity.User;
import com.example.course.dto.MessageResponse;
import com.example.course.dto.UserCreateDto;
import com.example.course.dto.UserDto;
import com.example.course.mapper.UserMapper;
import com.example.course.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Api
public class UserController {
    private final UserService service;
    private final UserMapper mapper;

    @GetMapping("admin/users")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("Get users")
    public ResponseEntity<List<UserDto>> findAll() {
        List<UserDto> dtoList = service.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        if (dtoList.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(dtoList);
        }
    }

    @GetMapping("admin/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("Get user")
    public ResponseEntity<UserDto> findById(@PathVariable Long id) {
        return service.findById(id)
                .map(lesson -> ResponseEntity.ok(mapper.toDto(lesson)))
                .orElseThrow(() -> new NoSuchElementException("User not found for ID: " + id));

    }

    @PostMapping("admin/users")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("Create user")
    public ResponseEntity<UserDto> create(@RequestBody @Valid UserCreateDto userCreateDto) {
        User persistedUser = service.save(mapper.toEntity(userCreateDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(persistedUser));
    }

    @PutMapping("admin/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("Update user")
    public ResponseEntity<UserDto> update(@PathVariable Long id,
                                          @RequestBody UserDto request) {
        User user = service.findById(id).orElseThrow(() -> new NoSuchElementException("User not found for ID: " + id));
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        User updatedUser = service.save(user);
        return ResponseEntity.ok(mapper.toDto(updatedUser));
    }

    @DeleteMapping("admin/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("Delete user")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok(new MessageResponse("User deleted successfully."));
    }

}
