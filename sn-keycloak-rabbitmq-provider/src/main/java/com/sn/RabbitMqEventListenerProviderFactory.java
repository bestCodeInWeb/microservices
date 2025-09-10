package com.sn;

import org.keycloak.Config;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;

public class RabbitMqEventListenerProviderFactory implements EventListenerProviderFactory {
    @Override
    public EventListenerProvider create(org.keycloak.models.KeycloakSession session) {
        return new RabbitMqEventListenerProvider();
    }

    @Override
    public void init(Config.Scope config) {}

    @Override
    public void postInit(org.keycloak.models.KeycloakSessionFactory factory) {}

    @Override
    public void close() {}

    @Override
    public String getId() {
        return "rabbitmq-events";
    }
}
