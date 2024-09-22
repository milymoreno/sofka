package com.prueba.sofkaestadisticas.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.prueba.sofkaestadisticas.domain.model.entity.EstadisticaCambioCliente;
import com.prueba.sofkaestadisticas.infraestructure.persistencia.repository.EstadisticaCambioClienteRepository;


class EstadisticaServiceTest {

    @Mock
    private EstadisticaCambioClienteRepository repository;

    @InjectMocks
    private EstadisticaService estadisticaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void procesarEventoIngreso() {
        // Arrange
        Map<String, Object> evento = new HashMap<>();
        evento.put("tipoEvento", "INGRESO");
        evento.put("sofkianoId", "123");
        evento.put("nombre", "John Doe");

        Map<String, Object> clienteInfo = new HashMap<>();
        clienteInfo.put("clienteId", "456");
        clienteInfo.put("nombreCliente", "Cliente A");
        clienteInfo.put("fecha", "2023-10-01T10:00:00");
        evento.put("cliente", clienteInfo);

        EstadisticaCambioCliente estadistica = new EstadisticaCambioCliente("123", "John Doe");
        when(repository.findBySofkianoId("123")).thenReturn(Optional.of(estadistica));

        // Act
        estadisticaService.procesarEvento(evento);

        // Assert
        ArgumentCaptor<EstadisticaCambioCliente> captor = ArgumentCaptor.forClass(EstadisticaCambioCliente.class);
        verify(repository).save(captor.capture());
        EstadisticaCambioCliente savedEstadistica = captor.getValue();

        assertEquals(1, savedEstadistica.getClientesIngreso().size());
        assertEquals("456", savedEstadistica.getClientesIngreso().get(0).getClienteId());
    }

    @Test
    void procesarEventoEgreso() {
        // Arrange
        Map<String, Object> evento = new HashMap<>();
        evento.put("tipoEvento", "EGRESO");
        evento.put("sofkianoId", "123");
        evento.put("nombre", "John Doe");

        Map<String, Object> clienteInfo = new HashMap<>();
        clienteInfo.put("clienteId", "456");
        clienteInfo.put("nombreCliente", "Cliente A");
        clienteInfo.put("fecha", "2023-10-01T10:00:00");
        evento.put("cliente", clienteInfo);

        EstadisticaCambioCliente estadistica = new EstadisticaCambioCliente("123", "John Doe");
        when(repository.findBySofkianoId("123")).thenReturn(Optional.of(estadistica));

        // Act
        estadisticaService.procesarEvento(evento);

        // Assert
        ArgumentCaptor<EstadisticaCambioCliente> captor = ArgumentCaptor.forClass(EstadisticaCambioCliente.class);
        verify(repository).save(captor.capture());
        EstadisticaCambioCliente savedEstadistica = captor.getValue();

        assertEquals(1, savedEstadistica.getClientesEgreso().size());
        assertEquals("456", savedEstadistica.getClientesEgreso().get(0).getClienteId());
    }

    @Test
    void procesarEventoSofkianoIdNulo() {
        // Arrange
        Map<String, Object> evento = new HashMap<>();
        evento.put("tipoEvento", "INGRESO");
        evento.put("sofkianoId", null);
        evento.put("nombre", "John Doe");

        Map<String, Object> clienteInfo = new HashMap<>();
        clienteInfo.put("clienteId", "456");
        clienteInfo.put("nombreCliente", "Cliente A");
        clienteInfo.put("fecha", "2023-10-01T10:00:00");
        evento.put("cliente", clienteInfo);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            estadisticaService.procesarEvento(evento);
        });

        assertEquals("El sofkianoId no puede ser nulo o vacío.", exception.getMessage());
    }

    @Test
    void procesarEventoClienteInfoNulo() {
        // Arrange
        Map<String, Object> evento = new HashMap<>();
        evento.put("tipoEvento", "INGRESO");
        evento.put("sofkianoId", "123");
        evento.put("nombre", "John Doe");
        evento.put("cliente", null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            estadisticaService.procesarEvento(evento);
        });

        assertEquals("clienteInfo no puede ser nulo en el evento: " + evento, exception.getMessage());
    }
}