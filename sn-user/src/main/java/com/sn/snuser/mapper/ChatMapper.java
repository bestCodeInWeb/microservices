package com.sn.snuser.mapper;

import com.sn.snuser.dto.ChatDto;
import com.sn.snuser.model.Chat;
import org.mapstruct.Mapper;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper(componentModel = "spring")
public interface ChatMapper {
    ChatMapper INSTANCE = getMapper(ChatMapper.class);
    ChatDto toDto(Chat entity);
    Chat toEntity(ChatDto dto);
}
