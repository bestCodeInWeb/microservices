package com.sn.snuser.mapper;

import com.sn.snuser.dto.CommentDto;
import com.sn.snuser.model.Comment;
import org.mapstruct.Mapper;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper(componentModel = "spring") // если ты используешь Spring
public interface CommentMapper {
    CommentMapper INSTANCE = getMapper(CommentMapper.class);
    CommentDto toDto(Comment entity);
    Comment toEntity(CommentDto dto);
}
