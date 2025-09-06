package com.sn.snuser.mapper;


import com.sn.snuser.dto.UserDto;
import com.sn.snuser.model.User;
import org.mapstruct.Mapper;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper(componentModel = "spring") // если ты используешь Spring
public interface UserMapper {
    UserMapper INSTANCE = getMapper(UserMapper.class);
    UserDto toDto(User entity);
    User toEntity(UserDto dto);
}
