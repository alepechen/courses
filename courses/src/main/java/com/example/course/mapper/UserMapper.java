package com.example.course.mapper;

import com.example.course.dao.entity.User;
import com.example.course.dto.UserCreateDto;
import com.example.course.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User source);

    User toEntity(UserCreateDto source);
}
