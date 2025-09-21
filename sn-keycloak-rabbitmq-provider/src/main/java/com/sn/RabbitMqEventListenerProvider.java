package com.sn;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.sn.events.User;
import com.sn.events.UserEvent;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.admin.AdminEvent;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class RabbitMqEventListenerProvider implements EventListenerProvider {
    private static final String EXCHANGE_NAME = "keycloak.user.events";
    private static final String ROUTING_KEY = "keycloak.event";
    private final Connection connection;
    private final Channel channel;
    private final ObjectMapper objectMapper = JacksonAvroConfig.configuredMapper();

    public RabbitMqEventListenerProvider() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("rabbitmq");
            factory.setUsername("user");
            factory.setPassword("password");

            this.connection = factory.newConnection();
            this.channel = connection.createChannel();

            channel.exchangeDeclare(EXCHANGE_NAME, "topic", true);
        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to RabbitMQ", e);
        }
    }

    @Override
    public void onEvent(Event event) {
        try {
            String json = objectMapper.writeValueAsString(event);
            channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, null, json.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean includeRepresentation) {
        try {
            User user = null;

            if (includeRepresentation && adminEvent.getRepresentation() != null) {
                try {
                    JsonNode node = objectMapper.readTree(adminEvent.getRepresentation());
                    user = new User(
                            extractUserId(adminEvent),
                            node.path("username").asText(null),
                            node.path("firstName").asText(null),
                            node.path("lastName").asText(null),
                            node.path("email").asText(null)
                    );
                } catch (Exception e) {
                    user = null;
                }
            }

            UserEvent userEvent = new UserEvent(adminEvent.getOperationType().name(), user, adminEvent.getTime());

            String json = objectMapper.writeValueAsString(userEvent);
            channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, null, json.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            if (channel != null && channel.isOpen()) channel.close();
            if (connection != null && connection.isOpen()) connection.close();
        } catch (Exception ignored) {}
    }

    private String extractUserId(AdminEvent adminEvent) {
        String path = adminEvent.getResourcePath();
        if (path != null && path.startsWith("users/")) {
            return path.substring("users/".length());
        }
        return null;
    }
}
