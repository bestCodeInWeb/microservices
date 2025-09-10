package com.sn.snuser.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqListener {

    @RabbitListener(queues = "keycloak-events")
    public void receiveMessage(Object message) {
        System.out.println("Получено сообщение: " + message);
    }
}
