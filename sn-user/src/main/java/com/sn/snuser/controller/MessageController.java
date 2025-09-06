package com.sn.snuser.controller;

import com.sn.snuser.dto.MessageDto;
import com.sn.snuser.mapper.MessageMapper;
import com.sn.snuser.model.Message;
import com.sn.snuser.service.MessageService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public MessageDto addMessage(@RequestBody MessageDto messageDto) {
        Message newMessage = MessageMapper.INSTANCE.toEntity(messageDto);
        return MessageMapper.INSTANCE.toDto(messageService.save(newMessage));
    }

    @GetMapping
    public List<MessageDto> getMessages(@RequestParam String like) {
        return new ArrayList<>(); //todo find by text
    }
}
