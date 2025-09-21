package com.sn.snuser.service.rabbitmq;

import com.sn.events.UserEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class UserEventListener {

    private final UserEventService userEventService;

    public UserEventListener(UserEventService userEventService) {
        this.userEventService = userEventService;
    }


    @RabbitListener(queues = "${rabbitmq.queues.sn-user.keycloak-user-events}")
    public void receiveMessage(UserEvent event) {
        switch (event.getOperationType()) {
            case "CREATE" -> userEventService.createUser(event.getUser(), event.getTimestamp());
            case "UPDATE" -> userEventService.updateUser(event.getUser(), event.getTimestamp());
            case "DELETE" -> userEventService.deleteUser(event.getUser(), event.getTimestamp());
            case "ACTION" -> userEventService.action(event.getUser(), event.getTimestamp());
            default -> System.out.println("Unknown event");
        }
    }
}