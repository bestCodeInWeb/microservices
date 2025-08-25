package com.sn.snuser.dto;

import com.sn.snuser.model.enums.MediaType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostDto {
    private String id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private UserDto owner;
    private UserDto placement;

    private String content;
    private String description;
    private String mediaUri;
    private MediaType mediaType;
}
