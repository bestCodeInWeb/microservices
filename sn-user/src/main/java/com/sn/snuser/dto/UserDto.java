package com.sn.snuser.dto;

import com.sn.snuser.model.enums.Gender;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String userName;
    private String firstName;
    private String lastName;
    private Gender gender;
    private AddressDto address;
    private ContactsDto contacts;
}
