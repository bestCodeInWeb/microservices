package com.sn.snuser.service.rabbitmq;

import com.sn.events.UserEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class UserEventListener {
    private static final Logger log = LoggerFactory.getLogger(UserEventListener.class);
    private final UserEventService userEventService;

    public UserEventListener(UserEventService userEventService) {
        this.userEventService = userEventService;
    }


    @RabbitListener(queues = "${rabbitmq.queues.sn-user.keycloak-user-events}")
    public void receiveMessage(UserEvent event) {
        log.info("Received UserEvent: operation={}, userId={}", event.getOperationType(), event.getUser().getId());

        try {
            switch (event.getOperationType()) {
                case "CREATE" -> userEventService.createUser(event.getUser(), event.getTimestamp());
                case "UPDATE" -> userEventService.updateUser(event.getUser(), event.getTimestamp());
                case "DELETE" -> userEventService.deleteUser(event.getUser(), event.getTimestamp());
                // Ми також отримуємо "ACTION" події, але вони нас не цікавлять для CRUD
                case "ACTION" -> userEventService.handleAction(event.getUser(), event.getTimestamp());
                default -> log.warn("Unknown event operation type: {}", event.getOperationType());
            }
            log.debug("Successfully processed UserEvent for userId={}", event.getUser().getId());

        } catch (Exception e) {
            // "Best practice": Якщо сталася помилка, ми логуємо її
            // і кидаємо AmqpRejectAndDontRequeueException.
            // Це скаже RabbitMQ *не* намагатися повторити повідомлення
            // і (оскільки ми налаштували DLQ) відправити його у "мертву" чергу.
            log.error("Failed to process UserEvent for userId={}. Error: {}. Sending to DLQ.",
                    event.getUser().getId(), e.getMessage(), e);
            throw new AmqpRejectAndDontRequeueException("Event processing failed", e);
        }
    }
}