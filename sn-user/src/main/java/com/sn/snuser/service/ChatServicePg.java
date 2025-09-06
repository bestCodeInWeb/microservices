package com.sn.snuser.service;

import com.sn.snuser.model.Chat;
import com.sn.snuser.repository.ChatRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatServicePg implements ChatService {
    private final ChatRepository chatRepository;

    public ChatServicePg(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public Optional<Chat> findById(String chatId) {
        return chatRepository.findById(chatId);
    }
}
