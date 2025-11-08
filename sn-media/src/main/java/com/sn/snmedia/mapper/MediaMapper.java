package com.sn.snmedia.mapper;

import com.sn.snmedia.dto.MediaDto;
import com.sn.snmedia.model.Media;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper(componentModel = "spring")
public interface MediaMapper {
    MediaMapper INSTANCE = getMapper(MediaMapper.class);

    @Mapping(target = "filePath", ignore = true) // Не повертаємо фізичний шлях клієнту
    MediaDto toDto(Media entity);
    Media toEntity(MediaDto dto);
}
