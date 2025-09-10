package com.sn;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.admin.AdminEvent;

public class RabbitMqEventListenerProvider implements EventListenerProvider {
    private final Channel channel;

    public RabbitMqEventListenerProvider() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("rabbitmq"); // имя контейнера в docker-compose
            factory.setUsername("user");
            factory.setPassword("password");
            Connection connection = factory.newConnection();
            this.channel = connection.createChannel();
            channel.queueDeclare("keycloak-events", true, false, false, null);
        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to RabbitMQ", e);
        }
    }

    @Override
    public void onEvent(Event event) {
        try {
            if ("REGISTER".equals(event.getType().name())) {
                String msg = "User registered: " + event.getUserId();
                channel.basicPublish("", "keycloak-events", null, msg.getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean includeRepresentation) {
        try {
            if ("CREATE".equals(adminEvent.getOperationType().name())
                    || "UPDATE".equals(adminEvent.getOperationType().name())) {
                String msg = "Admin op: " + adminEvent.getOperationType() +
                        " on " + adminEvent.getResourcePath();
                channel.basicPublish("", "keycloak-events", null, msg.getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            channel.close();
        } catch (Exception ignored) {}
    }
}
