package com.sn.snuser.mapper;

import com.sn.snuser.dto.MediaDto;
import com.sn.snuser.model.Media;
import org.mapstruct.Mapper;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper(componentModel = "spring") // если ты используешь Spring
public interface MediaMapper {
    MediaMapper INSTANCE = getMapper(MediaMapper.class);
    MediaDto toDto(Media entity);
    Media toEntity(MediaDto dto);
}
