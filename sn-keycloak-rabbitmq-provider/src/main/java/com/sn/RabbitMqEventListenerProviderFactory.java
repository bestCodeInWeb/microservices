package com.sn;

import org.keycloak.Config;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;

public class RabbitMqEventListenerProviderFactory implements EventListenerProviderFactory {

    @Override
    public EventListenerProvider create(KeycloakSession session) {
        System.out.println("Factory.create() called. Creating new RabbitMqEventListenerProvider...");
        return new RabbitMqEventListenerProvider(session);
    }

    @Override
    public void init(Config.Scope config) {
        System.out.println("Factory.init() called (build-time).");
    }

    @Override
    public void postInit(org.keycloak.models.KeycloakSessionFactory factory) {
        System.out.println("Factory.postInit() called (run-time).");
    }

    @Override
    public void close() {
        System.out.println("Factory.close() called.");
    }

    @Override
    public String getId() {
        return "rabbitmq-events";
    }
}
