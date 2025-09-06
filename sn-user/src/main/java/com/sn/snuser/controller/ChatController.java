package com.sn.snuser.controller;

import com.sn.snuser.dto.ChatDto;
import com.sn.snuser.mapper.ChatMapper;
import com.sn.snuser.service.ChatService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("chats")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/{chatId}")
    public ChatDto getChatById(@RequestParam String chatId) {
        return chatService.findById(chatId).map(ChatMapper.INSTANCE::toDto).orElseThrow(); //todo
    }

    @PostMapping
    public ChatDto createNewChat(@RequestBody ChatDto chatId) {
        return null; //todo
    }
}
