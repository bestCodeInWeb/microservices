package com.sn.snuser.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    private String id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserDto creator;
    private String content;
}
