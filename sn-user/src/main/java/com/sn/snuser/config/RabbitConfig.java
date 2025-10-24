package com.sn.snuser.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${rabbitmq.exchanges.keycloak-user}")
    private String keycloakUserExchange;

    @Value("${rabbitmq.queues.sn-user.keycloak-user-events}")
    private String userQueue;

    @Value("${rabbitmq.routing-keys.keycloak.event}")
    private String userRoutingKey;

    @Value("${rabbitmq.exchanges.keycloak-user}.dlx")
    private String keycloakUserDlx; // DLX = Dead Letter Exchange

    @Value("${rabbitmq.queues.sn-user.keycloak-user-events}.dlq")
    private String userDlq; // DLQ = Dead Letter Queue

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue keycloakQueue() {
        return QueueBuilder.durable(userQueue)
                .withArgument("x-dead-letter-exchange", keycloakUserDlx)
                .build();
    }

    @Bean
    public TopicExchange keycloakExchange() {
        return ExchangeBuilder
                .topicExchange(keycloakUserExchange)
                .durable(true)
                .build();
    }

    @Bean
    public Binding keycloakBinding(Queue keycloakQueue, TopicExchange keycloakExchange/*, RabbitAdmin rabbitAdmin*/) {
        return BindingBuilder.bind(keycloakQueue).to(keycloakExchange).with(userRoutingKey);
    }

    @Bean
    public FanoutExchange deadLetterExchange() {
        return new FanoutExchange(keycloakUserDlx);
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(userDlq).build();
    }

    @Bean
    public Binding deadLetterBinding(Queue deadLetterQueue, FanoutExchange deadLetterExchange) {
        return BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange);
    }
}