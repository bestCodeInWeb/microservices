package com.sn.snuser.service;

import com.sn.snuser.model.Message;
import com.sn.snuser.repository.MessageRepository;
import org.springframework.stereotype.Service;

@Service
public class MessageServicePg implements MessageService {
    private final MessageRepository messageRepository;

    public MessageServicePg(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Message save(Message newMessage) {
        return messageRepository.save(newMessage);
    }
}
