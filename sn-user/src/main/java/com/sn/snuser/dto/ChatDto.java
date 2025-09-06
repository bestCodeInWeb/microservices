package com.sn.snuser.dto;

import com.sn.snuser.model.Message;
import com.sn.snuser.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatDto {
    private String id;
    private Set<User> users;
    private List<MessageDto> messages;
}
