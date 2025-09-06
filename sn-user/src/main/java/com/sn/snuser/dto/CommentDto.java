package com.sn.snuser.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private String id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserDto creator;
    private String text;
}
