package com.prueba.sofka.domain.service;

import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.sofka.config.RabbitMQConfig;

import jakarta.annotation.PostConstruct;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

import java.util.Map;

@Service
public class SofkianoEventProducerRoute extends RouteBuilder {
   
    private final RabbitMQConfig rabbitMQConfig; 
    private final ProducerTemplate producerTemplate; 
    private final CamelContext camelContext;

    @Autowired
    public SofkianoEventProducerRoute(RabbitMQConfig rabbitMQConfig, ProducerTemplate producerTemplate, 
                                    CamelContext camelContext) {
        this.rabbitMQConfig = rabbitMQConfig;
        this.producerTemplate = producerTemplate;
        this.camelContext = camelContext;
    }

    @PostConstruct
    public void start() {
        try {
            if (!camelContext.isStarted()) {
                camelContext.addRoutes(this);  // Registrar la ruta manualmente
                camelContext.start();
                System.out.println("Rutas registradas: " + camelContext.getRoutes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    

    @Override
    public void configure() throws Exception {
        // Construir la URI de RabbitMQ usando los valores configurados
        String rabbitmqUri = String.format(
            "rabbitmq://%s:%d/%s?autoDelete=false&exchangeType=%s",
            rabbitMQConfig.getHost(),
            rabbitMQConfig.getPort(),
            rabbitMQConfig.getExchange(),
            rabbitMQConfig.getExchangeType()
        );
        System.out.println("RabbitMQ URIiiiiiiiii: " + rabbitmqUri);

        from("direct:sofkianoChangeEvent")
        .routeId("sofkianoChangeEvent") // ID de la ruta
            .doTry()
                .log("Evento a enviar: ${body}")
                .to(rabbitmqUri) // Tu URI de RabbitMQ
                //.to("rabbitmq://localhost:5672/sofkiano-exchange?autoDelete=false&exchangeType=fanout")
                .log("Evento enviado exitosamente: ${body}")
            .doCatch(Exception.class)
                .log("Error al enviar el evento: ${exception.message}");
    }

    public void sendEvent(Map<String, Object> event) {
        try {
            
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonEvent = objectMapper.writeValueAsString(event);

            System.out.println("Json del evento: " + jsonEvent);

            
            if (!camelContext.isStarted()) {
                camelContext.start();
            }

            if (camelContext.getRouteController().getRouteStatus("sofkianoChangeEvent") != null) {
                if (camelContext.getRouteController().getRouteStatus("sofkianoChangeEvent").isStarted()) {
                    producerTemplate.sendBody("direct:sofkianoChangeEvent", jsonEvent);
                } else {
                    System.err.println("La ruta 'sofkianoChangeEvent' no está disponible.");
                }
            } else {
                System.err.println("La ruta 'sofkianoChangeEvent' no está registrada en el contexto de Camel.");
            }
            
           
        } catch (Exception e) {
            System.err.println("Error al enviar el evento: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
