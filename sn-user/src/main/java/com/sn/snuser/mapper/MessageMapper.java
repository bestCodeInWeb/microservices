package com.sn.snuser.mapper;

import com.sn.snuser.dto.MessageDto;
import com.sn.snuser.model.Message;
import org.mapstruct.Mapper;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper(componentModel = "spring") // если ты используешь Spring
public interface MessageMapper {
    MessageMapper INSTANCE = getMapper(MessageMapper.class);
    MessageDto toDto(Message entity);
    Message toEntity(MessageDto dto);
}