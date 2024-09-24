package com.prueba.sofka.domain.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.sofka.config.RabbitMQConfig;



public class SofkianoEventProducerRouteTest {

    @Mock
    private RabbitMQConfig rabbitMQConfig;

    @Mock
    private ProducerTemplate producerTemplate;

    @Mock
    private CamelContext camelContext;

    @InjectMocks
    private SofkianoEventProducerRoute sofkianoEventProducerRoute;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testStart() throws Exception {
        when(camelContext.isStarted()).thenReturn(false);

        sofkianoEventProducerRoute.start();

        verify(camelContext, times(1)).addRoutes(any(RouteBuilder.class));
        verify(camelContext, times(1)).start();
    }

    @Test
    public void testConfigure() throws Exception {
        when(rabbitMQConfig.getHost()).thenReturn("localhost");
        when(rabbitMQConfig.getPort()).thenReturn(5672);
        when(rabbitMQConfig.getExchange()).thenReturn("sofkiano-exchange");
        when(rabbitMQConfig.getExchangeType()).thenReturn("fanout");

        sofkianoEventProducerRoute.configure();

        // Additional assertions can be added here to verify the route configuration
    }

    @Test
    public void testSendEvent() throws Exception {
        Map<String, Object> event = new HashMap<>();
        event.put("key", "value");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonEvent = objectMapper.writeValueAsString(event);

        when(camelContext.isStarted()).thenReturn(true);
        when(camelContext.getRouteController().getRouteStatus("sofkianoChangeEvent")).thenReturn(org.apache.camel.ServiceStatus.Started);

        sofkianoEventProducerRoute.sendEvent(event);

        verify(producerTemplate, times(1)).sendBody("direct:sofkianoChangeEvent", jsonEvent);
    }

    @Test
    public void testSendEventWhenRouteNotStarted() throws Exception {
        Map<String, Object> event = new HashMap<>();
        event.put("key", "value");

        when(camelContext.isStarted()).thenReturn(true);
        when(camelContext.getRouteController().getRouteStatus("sofkianoChangeEvent")).thenReturn(org.apache.camel.ServiceStatus.Stopped);

        sofkianoEventProducerRoute.sendEvent(event);

        verify(producerTemplate, times(0)).sendBody(anyString(), anyString());
    }

    @Test
    public void testSendEventWhenRouteNotRegistered() throws Exception {
        Map<String, Object> event = new HashMap<>();
        event.put("key", "value");

        when(camelContext.isStarted()).thenReturn(true);
        when(camelContext.getRouteController().getRouteStatus("sofkianoChangeEvent")).thenReturn(null);

        sofkianoEventProducerRoute.sendEvent(event);

        verify(producerTemplate, times(0)).sendBody(anyString(), anyString());
    }
}