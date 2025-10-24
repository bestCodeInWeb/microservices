package com.sn;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.sn.events.User;
import com.sn.events.UserEvent;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.events.admin.OperationType;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.events.admin.ResourceType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.concurrent.TimeoutException;

public class RabbitMqEventListenerProvider implements EventListenerProvider {
    private static final String EXCHANGE_NAME = "keycloak.user.events";

    private final KeycloakSession session;
    private Connection connection; // Зробимо не-final, щоб ініціалізувати в конструкторі
    private Channel channel; // Зробимо не-final

    private final ObjectMapper objectMapper = JacksonAvroConfig.configuredMapper();

    // Використовувані операції
    private static final Set<OperationType> ADMIN_USER_OPERATIONS = Set.of(
            OperationType.CREATE, OperationType.UPDATE, OperationType.DELETE
    );

    // Використовувані події користувача
    private static final Set<EventType> USER_EVENTS = Set.of(
            EventType.REGISTER, EventType.UPDATE_PROFILE, EventType.DELETE_ACCOUNT
    );

    // КОНСТРУКТОР (викликається під час запуску)
    public RabbitMqEventListenerProvider(KeycloakSession session) {
        System.out.println("RabbitMqEventListenerProvider CONSTRUCTOR called (run-time).");
        this.session = session;

        // Читаємо змінні середовища ТУТ (під час запуску)
        try {
            ConnectionFactory factory = new ConnectionFactory();

            // Читаємо змінні з System.getenv()
            String host = System.getenv().getOrDefault("RABBITMQ_HOST", "localhost");
            int port = Integer.parseInt(System.getenv().getOrDefault("RABBITMQ_PORT", "5672"));
            String user = System.getenv().getOrDefault("RABBITMQ_USER", "guest");
            String pass = System.getenv().getOrDefault("RABBITMQ_PASS", "guest");

            factory.setHost(host);
            factory.setPort(port);
            factory.setUsername(user);
            factory.setPassword(pass);

            System.out.printf("Attempting to connect to RabbitMQ at %s:%d", host, port);
            this.connection = factory.newConnection();
            this.channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, "topic", true);

            // ЦЕЙ ЛОГ МИ МАЄМО ПОБАЧИТИ ПЕРШИМ
            System.out.println("RabbitMqEventListenerProvider connected to RabbitMQ and exchange declared.");

        } catch (IOException | TimeoutException e) {
            System.out.printf("Failed to connect to RabbitMQ during provider startup: %s", e);
            // Ми не кидаємо RuntimeException, щоб не "вбити" Keycloak,
            // але логуємо помилку. Провайдер не працюватиме.
        }
    }

    // Метод onEvent (Event) - Події Користувача
    @Override
    public void onEvent(Event event) {
        if (this.channel == null) {
            System.out.println("RabbitMQ channel is not available. Skipping event.");
            return;
        }
        System.out.printf("Received USER event: %s", event.getType());

        if (!USER_EVENTS.contains(event.getType())) {
            System.out.println("Skipping user event, not in tracked list.");
            return;
        }

        RealmModel realm = session.realms().getRealm(event.getRealmId());
        UserModel userModel = session.users().getUserById(realm, event.getUserId());
        if (userModel == null) {
            System.out.printf("Could not find user for user event. UserID: %s", event.getUserId());
            return;
        }

        User user = mapUserModelToAvro(userModel);
        String operationType = mapEventTypeToOperation(event.getType());
        UserEvent userEvent = new UserEvent(operationType, user, event.getTime());
        String routingKey = "user." + operationType.toLowerCase();

        publishEvent(userEvent, routingKey);
    }

    // Метод onEvent (AdminEvent) - Події Адміна
    @Override
    public void onEvent(AdminEvent adminEvent, boolean includeRepresentation) {
        if (this.channel == null) {
            System.out.println("RabbitMQ channel is not available. Skipping event.");
            return;
        }
        System.out.printf("Received ADMIN event: %s on resource: %s", adminEvent.getOperationType(), adminEvent.getResourceType());

        if (adminEvent.getResourceType() != ResourceType.USER || !ADMIN_USER_OPERATIONS.contains(adminEvent.getOperationType())) {
            System.out.println("Skipping admin event, not a tracked user operation.");
            return;
        }

        User user = new User();
        String userId = extractUserIdFromPath(adminEvent.getResourcePath());
        if (userId == null) {
            System.out.println("Could not extract user ID from admin event resource path.");
            return;
        }

        user.setId(userId);

        // Спроба отримати дані з representation (якщо є)
        if (includeRepresentation && adminEvent.getRepresentation() != null) {
            try {
                JsonNode node = objectMapper.readTree(adminEvent.getRepresentation());
                user.setUsername(node.path("username").asText(null));
                user.setFirstName(node.path("firstName").asText(null));
                user.setLastName(node.path("lastName").asText(null));
                user.setEmail(node.path("email").asText(null));
            } catch (JsonProcessingException e) {
                System.out.printf("Failed to parse admin event representation: %s", e);
            }
        }

        // Якщо representation немає, спробуємо отримати UserModel (для DELETE або якщо representation був неповний)
        if (user.getUsername() == null) {
            System.out.printf("Representation missing data, fetching UserModel for ID: %s", userId);
            RealmModel realm = session.realms().getRealm(adminEvent.getRealmId());
            UserModel userModel = session.users().getUserById(realm, userId);
            if (userModel != null) {
                // Дозаповнюємо дані
                if (user.getUsername() == null) user.setUsername(userModel.getUsername());
                if (user.getFirstName() == null) user.setFirstName(userModel.getFirstName());
                if (user.getLastName() == null) user.setLastName(userModel.getLastName());
                if (user.getEmail() == null) user.setEmail(userModel.getEmail());
            } else {
                System.out.printf("Could not find user model for admin event. UserID: %s", userId);
            }
        }

        String operationType = adminEvent.getOperationType().name();
        UserEvent userEvent = new UserEvent(operationType, user, adminEvent.getTime());
        String routingKey = "user." + operationType.toLowerCase();

        publishEvent(userEvent, routingKey);
    }

    // Внутрішні методи (хелпери)
    private void publishEvent(UserEvent userEvent, String routingKey) {
        try {
            String json = objectMapper.writeValueAsString(userEvent);
            System.out.printf("Publishing event with routing key '%s'. Payload: %s", routingKey, json);
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, json.getBytes(StandardCharsets.UTF_8));
            // ЦЕЙ ЛОГ МИ ШУКАЄМО!
            System.out.printf("Published event to exchange '%s' with key '%s' for user ID %s",
                    EXCHANGE_NAME, routingKey, userEvent.getUser().getId());
        } catch (IOException e) {
            System.out.printf("Failed to serialize or publish event: %s", e);
        }
    }

    private String extractUserIdFromPath(String resourcePath) {
        if (resourcePath == null || !resourcePath.startsWith("users/")) {
            return null;
        }
        String potentialId = resourcePath.substring("users/".length());
        if (potentialId.contains("/")) {
            // Це може бути під-ресурс, як-от users/{id}/groups. Беремо {id}.
            return potentialId.split("/")[0];
        }
        return potentialId;
    }

    private User mapUserModelToAvro(UserModel userModel) {
        User user = new User();
        user.setId(userModel.getId());
        user.setUsername(userModel.getUsername());
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setEmail(userModel.getEmail());
        return user;
    }

    private String mapEventTypeToOperation(EventType eventType) {
        return switch (eventType) {
            case REGISTER -> "CREATE";
            case UPDATE_PROFILE -> "UPDATE";
            case DELETE_ACCOUNT -> "DELETE";
            default -> eventType.name();
        };
    }

    @Override
    public void close() {
        try {
            System.out.println("Closing RabbitMQ channel and connection.");
            if (channel != null && channel.isOpen()) channel.close();
            if (connection != null && connection.isOpen()) connection.close();
        } catch (IOException | TimeoutException e) {
            System.out.printf("Error while closing RabbitMQ connection: %s", e);
        }
    }
}
