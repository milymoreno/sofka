package com.prueba.sofka.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.ConnectionFactory;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    @Value("${rabbitmq.host}")
    private String host;

    @Value("${rabbitmq.port}")
    private int port;

    @Value("${rabbitmq.username}")
    private String username;

    @Value("${rabbitmq.password}")
    private String password;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.queue}")
    private String queue;

    @Value("${rabbitmq.exchangeType}")
    private String exchangeType;

    @Bean
    public ConnectionFactory connectionFactory() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(exchange); // Crea el intercambio de tipo fanout
    }

    @Bean
    public Queue sofkianoEventQueue() {
        return new Queue(queue, true); // Crea la cola como durable
    }

    @Bean
    public Binding binding(FanoutExchange fanoutExchange, Queue sofkianoEventQueue) {
        Binding binding = BindingBuilder.bind(sofkianoEventQueue).to(fanoutExchange);
        System.out.println("Binding created: Queue " +sofkianoEventQueue.getName()+"bound to Exchange "+ fanoutExchange.getName());
        return binding;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    // Getters
    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getExchange() {
        return exchange;
    }

    public String getQueue() {
        return queue;
    }

    public String getExchangeType() {
        return exchangeType;
    }
}



// package com.prueba.sofka.config;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import com.rabbitmq.client.ConnectionFactory;

// @Configuration
// public class RabbitMQConfig {

//     @Value("${rabbitmq.host}")
//     private String host;

//     @Value("${rabbitmq.port}")
//     private int port;

//     @Value("${rabbitmq.username}")
//     private String username;

//     @Value("${rabbitmq.password}")
//     private String password;

//     @Value("${rabbitmq.exchange}")
//     private String exchange;

//     @Value("${rabbitmq.queue}")
//     private String queue;

//     @Value("${rabbitmq.exchangeType}")
//     private String exchangeType;

//     @Bean
//     public ConnectionFactory connectionFactory() {
//         ConnectionFactory connectionFactory = new ConnectionFactory();
//         connectionFactory.setHost(host);
//         connectionFactory.setPort(port);
//         connectionFactory.setUsername(username);
//         connectionFactory.setPassword(password);
//         return connectionFactory;
//     }
   
//     public String getHost() {
//         return host;
//     }

//     public int getPort() {
//         return port;
//     }

//     public String getExchange() {
//         return exchange;
//     }

//     public String getQueue() {
//         return queue;
//     }

//     public String getExchangeType() {
//         return exchangeType;
//     }
// }

