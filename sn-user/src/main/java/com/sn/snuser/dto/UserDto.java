package com.sn.snuser.dto;

import com.sn.snuser.model.enums.Gender;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserDto {
    private String id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String firstName;
    private String lastName;
    private Gender gender;
    private String country;
    private String city;
    private String street;
}
