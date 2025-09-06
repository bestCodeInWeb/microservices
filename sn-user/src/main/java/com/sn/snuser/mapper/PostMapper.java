package com.sn.snuser.mapper;

import com.sn.snuser.dto.PostDto;
import com.sn.snuser.model.Post;
import org.mapstruct.Mapper;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper(componentModel = "spring") // если ты используешь Spring
public interface PostMapper {
    PostMapper INSTANCE = getMapper(PostMapper.class);
    PostDto toDto(Post entity);
    Post toEntity(PostDto dto);
}