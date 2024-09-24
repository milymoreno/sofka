package com.prueba.sofkaestadisticas.aplicattion.components;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.sofkaestadisticas.aplicattion.service.IEstadisticaService;
import com.prueba.sofkaestadisticas.config.RabbitMQConfig;


public class SofkianoEventListenerRouteTest {

    @Mock
    private IEstadisticaService estadisticaService;

    @Mock
    private RabbitMQConfig rabbitMQConfig;

    @Mock
    private CamelContext camelContext;

    @Mock
    private ConsumerTemplate consumerTemplate;

    @InjectMocks
    private SofkianoEventListenerRoute sofkianoEventListenerRoute;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        sofkianoEventListenerRoute = new SofkianoEventListenerRoute(estadisticaService, rabbitMQConfig, camelContext, consumerTemplate);
    }

    @Test
    public void testStart() throws Exception {
        when(camelContext.isStarted()).thenReturn(false);
        doNothing().when(camelContext).addRoutes(any(RouteBuilder.class));
        doNothing().when(camelContext).start();

        sofkianoEventListenerRoute.start();

        verify(camelContext, times(1)).addRoutes(any(RouteBuilder.class));
        verify(camelContext, times(1)).start();
    }

    @Test
    public void testConfigure() throws Exception {
        when(rabbitMQConfig.getHost()).thenReturn("localhost");
        when(rabbitMQConfig.getPort()).thenReturn(5672);
        when(rabbitMQConfig.getExchange()).thenReturn("exchange");
        when(rabbitMQConfig.getQueue()).thenReturn("queue");
        when(rabbitMQConfig.getExchangeType()).thenReturn("direct");

        sofkianoEventListenerRoute.configure();

        // Add assertions or verifications as needed
    }

    @Test
    public void testProcessMessage() throws Exception {
        String jsonMessage = "{\"key\":\"value\"}";
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("key", "value");

        ObjectMapper objectMapper = mock(ObjectMapper.class); // Mock the ObjectMapper
        when(objectMapper.readValue(jsonMessage, Map.class)).thenReturn(eventData);

        
    }
}