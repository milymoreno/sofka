package com.prueba.sofkaestadisticas.aplicattion.components;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;

import org.apache.camel.ConsumerTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.sofkaestadisticas.config.RabbitMQConfig;
import com.prueba.sofkaestadisticas.aplicattion.service.IEstadisticaService;

import jakarta.annotation.PostConstruct;

import java.util.Map;

@Component
public class SofkianoEventListenerRoute extends RouteBuilder {

    private final IEstadisticaService estadisticaService;
    private final ObjectMapper objectMapper;
    private final RabbitMQConfig rabbitMQConfig;
    private final CamelContext camelContext;
    private final ConsumerTemplate consumerTemplate;
    

    
    @Autowired
    public SofkianoEventListenerRoute(IEstadisticaService estadisticaService, RabbitMQConfig rabbitMQConfig, 
                                      CamelContext camelContext, ConsumerTemplate consumerTemplate) {
        this.estadisticaService = estadisticaService;
        this.objectMapper = new ObjectMapper();
        this.rabbitMQConfig = rabbitMQConfig;
        this.camelContext = camelContext;
        this.consumerTemplate = consumerTemplate;
        
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
        String rabbitmqUri = String.format(
          
            "rabbitmq://%s:%d/%s?queue=%s&autoAck=true&autoDelete=false&exchangeType=%s",
            rabbitMQConfig.getHost(),
            rabbitMQConfig.getPort(),
            rabbitMQConfig.getExchange(),
            rabbitMQConfig.getQueue(),
            rabbitMQConfig.getExchangeType()
        );

        System.out.println("RabbitMQ URI del listener: " + rabbitmqUri);

        from(rabbitmqUri)
            .routeId("sofkianoEventListenerRoute")
            .doTry()
                .log("Recibido mensaje de Sofkiano: ${body}")
                .process(exchange -> {
                    String message = exchange.getIn().getBody(String.class);
                    // Convertimos el JSON a un Map<String, Object>
                    Map<String, Object> eventData = objectMapper.readValue(message, Map.class);
                    // Procesar el evento recibido
                    estadisticaService.procesarEvento(eventData);                    
                })
            .doCatch(Exception.class)
                .log("Error al procesar el mensaje: ${exception.message}")
            .end();
    }

	

    public String receiveEvent() {
        try {
            if (camelContext.getRouteController().getRouteStatus("sofkianoEventListenerRoute").isStarted()) {
                String message = consumerTemplate.receiveBody("direct:sofkianoEventListenerRoute", String.class);
                System.out.println("Mensaje recibido manualmente: " + message);
                return message;
            } else {
                System.err.println("La ruta 'sofkianoEventListenerRoute' no está disponible.");
            }
        } catch (Exception e) {
            System.err.println("Error al recibir el evento: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
