package com.prueba.sofka.application.components;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

@Component
public class SofkianoEventProducer {

    private static final String EXCHANGE_NAME = "sofkiano-exchange";
    private static final String QUEUE_NAME = "sofkiano-event-queue";

    private final ConnectionFactory connectionFactory;
    private final ObjectMapper objectMapper;

    public SofkianoEventProducer(ConnectionFactory connectionFactory) {
        this.objectMapper = new ObjectMapper();
        this.connectionFactory = connectionFactory;
        createQueue();
    }

    public void sendSofkianoChangeEvent(String sofkianoId, String nombreSofkiano, String clienteId, String nombreCliente, String fecha, String tipoEvento) {
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {
            
            // Serializar el mensaje
            byte[] message = serializeMessage(sofkianoId, nombreSofkiano, clienteId, nombreCliente, fecha, tipoEvento);
    
            // Imprimir todos los detalles del evento
            System.out.println("Enviando evento de cambio de Sofkiano:");
            System.out.println("Sofkiano ID: " + sofkianoId);
            System.out.println("Nombre del Sofkiano: " + nombreSofkiano);
            System.out.println("Cliente ID: " + clienteId);
            System.out.println("Nombre del Cliente: " + nombreCliente);
            System.out.println("Fecha del Evento: " + fecha);
            System.out.println("Tipo de Evento: " + tipoEvento);
            System.out.println("Mensaje serializado: " + new String(message)); // Si deseas imprimir el mensaje serializado
    
            // Publica el evento en RabbitMQ
            channel.basicPublish(EXCHANGE_NAME, "", MessageProperties.PERSISTENT_TEXT_PLAIN, message);
            System.out.println(" [x] Sent sofkiano change event for sofkianoId: " + sofkianoId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    // private byte[] serializeMessage(String sofkianoId, String nombreSofkiano, String clienteId, 
    //                             String nombreCliente, String fecha, String tipoEvento) throws IOException {
    //     Map<String, Object> sofkianoEventData = new HashMap<>();
    //     sofkianoEventData.put("sofkianoId", sofkianoId);
    //     sofkianoEventData.put("nombre", nombreSofkiano);
    //     sofkianoEventData.put("clienteId", clienteId);
    //     sofkianoEventData.put("nombreCliente", nombreCliente);
    //     sofkianoEventData.put("fecha",fecha);
    //     sofkianoEventData.put("tipoEvento",tipoEvento);        

    //     // Convertimos el mapa a JSON
    //     String jsonMessage = objectMapper.writeValueAsString(sofkianoEventData);
    //     return jsonMessage.getBytes();
    // }

    private byte[] serializeMessage(String sofkianoId, String nombreSofkiano, String clienteId, 
                                String nombreCliente, String fecha, String tipoEvento) throws IOException {
        Map<String, Object> sofkianoEventData = new HashMap<>();
        sofkianoEventData.put("sofkianoId", sofkianoId);
        sofkianoEventData.put("nombre", nombreSofkiano);
        
        // Información del cliente y del evento
        Map<String, Object> clienteData = new HashMap<>();
        clienteData.put("clienteId", clienteId);
        clienteData.put("nombreCliente", nombreCliente);
        clienteData.put("fecha", fecha);  // puedes usar un formato de fecha estándar
        
        sofkianoEventData.put("cliente", clienteData);
        sofkianoEventData.put("tipoEvento", tipoEvento);

        // Convertimos el mapa a JSON
        String jsonMessage = objectMapper.writeValueAsString(sofkianoEventData);
        return jsonMessage.getBytes();
    }

    private void createQueue() {
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");
            System.out.println(" [*] Queue '" + QUEUE_NAME + "' and exchange '" + EXCHANGE_NAME + "' created successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


// @Component
// public class SofkianoEventProducer {

//     private static final String EXCHANGE_NAME = "sofkiano-exchange";
//     private static final String QUEUE_NAME = "sofkiano-event-queue";

//     private final ConnectionFactory connectionFactory;
//     private final ObjectMapper objectMapper;

//     public SofkianoEventProducer(ConnectionFactory connectionFactory) {
//         this.objectMapper = new ObjectMapper();
//         this.connectionFactory = connectionFactory;
//         createQueue();
//     }

//     public void sendSofkianoChangeEvent(String sofkianoId, String clienteAsociadoId, String fechaAsociado, String tipoEvento) {
//         try (Connection connection = connectionFactory.newConnection();
//              Channel channel = connection.createChannel()) {
//             channel.basicPublish(EXCHANGE_NAME, "", MessageProperties.PERSISTENT_TEXT_PLAIN, serializeMessage(sofkianoId, clienteAsociadoId, fechaAsociado, tipoEvento));
//             System.out.println(" [x] Sent sofkiano change event for sofkianoId: " + sofkianoId);
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }

//     private byte[] serializeMessage(String sofkianoId, String clienteAsociadoId, String fechaAsociado, String tipoEvento) throws IOException {
//         Map<String, Object> sofkianoEventData = new HashMap<>();
//         sofkianoEventData.put("sofkianoId", sofkianoId);
//         sofkianoEventData.put("clienteAsociadoId", clienteAsociadoId);
//         sofkianoEventData.put("fechaAsociado", fechaAsociado);
//         sofkianoEventData.put("tipoEvento", tipoEvento);

//         String jsonMessage = objectMapper.writeValueAsString(sofkianoEventData);
       
//         return jsonMessage.getBytes();
//     }

//     private void createQueue() {
//         try (Connection connection = connectionFactory.newConnection();
//              Channel channel = connection.createChannel()) {
//             channel.queueDeclare(QUEUE_NAME, true, false, false, null);
//             channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
//             channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");
//             System.out.println(" [*] Queue '" + QUEUE_NAME + "' and exchange '" + EXCHANGE_NAME + "' created successfully.");
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }
// }
