package com.prueba.sofka.application.components;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.sofka.application.service.IEstadisticaService;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Delivery;

@Component
public class SofkianoEventListener {

    private static final String QUEUE_NAME = "sofkiano-event-queue";
    private final ConnectionFactory connectionFactory;
    private final ObjectMapper objectMapper;
    private final IEstadisticaService estadisticaService;

    public SofkianoEventListener(ConnectionFactory connectionFactory, IEstadisticaService estadisticaService) {
        this.connectionFactory = connectionFactory;
        this.objectMapper = new ObjectMapper();
        this.estadisticaService = estadisticaService;
        consumeMessages();
    }

    private void consumeMessages() {
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.basicConsume(QUEUE_NAME, true, this::handleMessage, consumerTag -> {});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleMessage(String consumerTag, Delivery message) {
    try {        
        Map<String, Object> eventData = objectMapper.readValue(message.getBody(), Map.class);      
        estadisticaService.procesarEvento(eventData);
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    // private void handleMessage(String consumerTag, Delivery message) {
    //     try {
    //         // Convertimos el mensaje a un Map
    //         Map<String, Object> eventData = objectMapper.readValue(message.getBody(), Map.class);
            
    //         // Extraemos los clientes de ingreso y egreso
    //         List<Map<String, Object>> clientesIngreso = (List<Map<String, Object>>) eventData.get("clientesIngreso");
    //         List<Map<String, Object>> clientesEgreso = (List<Map<String, Object>>) eventData.get("clientesEgreso");

    //         // Llamamos al servicio para procesar el evento y guardar la estadística
    //         if (clientesIngreso != null && !clientesIngreso.isEmpty()) {
    //             estadisticaService.procesarEvento(clientesIngreso);
    //         }
    //         if (clientesEgreso != null && !clientesEgreso.isEmpty()) {
    //             estadisticaService.procesarEvento(clientesEgreso);
    //         }
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }
}
