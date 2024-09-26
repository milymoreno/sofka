package com.prueba.sofkaestadisticas.aplicattion.components;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQListener {

    @RabbitListener(queues = "${rabbitmq.queue}")
    public void receiveMessage(String message) {
        
        System.out.println("Mensaje recibido: " + message);        
        
    }
}
 
