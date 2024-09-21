package com.prueba.sofka.config;


import com.rabbitmq.client.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public ConnectionFactory connectionFactory() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost"); // O el host de tu servidor RabbitMQ
        connectionFactory.setUsername("guest"); // O el nombre de usuario de tu servidor RabbitMQ
        connectionFactory.setPassword("guest"); // O la contraseña de tu servidor RabbitMQ
        return connectionFactory;
    }
}


