package com.sn.snuser.service;

import com.sn.snuser.dto.ChatDto;
import com.sn.snuser.model.Chat;

import java.util.Optional;

public interface ChatService {
    Optional<Chat> findById(String chatId);
}
