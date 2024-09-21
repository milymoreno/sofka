package com.prueba.sofkaestadisticas.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.prueba.sofkaestadisticas.domain.model.entity.Cliente;
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
    void testProcesarEventoIngreso() {
        Map<String, Object> evento = Map.of(
            "tipoEvento", "INGRESO",
            "sofkianoId", "123",
            "nombre", "Ariana Mendoza",
            "cliente", Map.of(
                "clienteId", "456",
                "nombreCliente", "Bancolombia",
                "fecha", "2023-10-01T10:00:00"
            )
        );

        EstadisticaCambioCliente estadistica = new EstadisticaCambioCliente("123", "Ariana Mendoza");
        when(repository.findBySofkianoId("123")).thenReturn(Optional.of(estadistica));

        estadisticaService.procesarEvento(evento);

        assertEquals(1, estadistica.getClientesIngreso().size());
        verify(repository).save(estadistica);
    }

    @Test
    void testProcesarEventoEgreso() {
        Map<String, Object> evento = Map.of(
            "tipoEvento", "EGRESO",
            "sofkianoId", "123",
            "nombre", "Ariana Mendoza",
            "cliente", Map.of(
                "clienteId", "456",
                "nombreCliente", "Bancolombia",
                "fecha", "2023-10-01T10:00:00"
            )
        );

        EstadisticaCambioCliente estadistica = new EstadisticaCambioCliente("123", "Ariana Mendoza");
        when(repository.findBySofkianoId("123")).thenReturn(Optional.of(estadistica));

        estadisticaService.procesarEvento(evento);

        assertEquals(1, estadistica.getClientesEgreso().size());
        verify(repository).save(estadistica);
    }

    @Test
    void testObtenerEstadisticasPorRangoFechas() {
        EstadisticaCambioCliente estadistica = new EstadisticaCambioCliente("123", "Ariana Mendoza");
        estadistica.getClientesIngreso().add(new Cliente("456", "Bancolombia", "2023-10-01T10:00:00"));
        when(repository.findAll()).thenReturn(List.of(estadistica));

        List<EstadisticaCambioCliente> result = estadisticaService.obtenerEstadisticasPorRangoFechas("2023-10-01T00:00:00", "2023-10-02T00:00:00");

        assertEquals(1, result.size());
    }

    @Test
    void testObtenerIngresosYSalidas() {
        EstadisticaCambioCliente estadistica = new EstadisticaCambioCliente("123", "Ariana Mendoza");
        estadistica.getClientesIngreso().add(new Cliente("456", "Bancolombia", "2023-10-01T10:00:00"));
        estadistica.getClientesEgreso().add(new Cliente("789", "Seguros Libertador", "2023-10-01T12:00:00"));
        when(repository.findAll()).thenReturn(List.of(estadistica));

        Map<String, Integer> result = estadisticaService.obtenerIngresosYSalidas("2023-10-01T00:00:00", "2023-10-02T00:00:00");

        assertEquals(1, result.get("ingresos"));
        assertEquals(1, result.get("egresos"));
    }
}