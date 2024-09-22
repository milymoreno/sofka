
package com.prueba.sofkaestadisticas.infraestructure.web.controller;

import com.prueba.sofkaestadisticas.aplicattion.response.EstadisticaCambioClienteResponse;
import com.prueba.sofkaestadisticas.aplicattion.service.IEstadisticaService;
import com.prueba.sofkaestadisticas.domain.model.entity.EstadisticaCambioCliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


public class EstadisticaControllerTest {

    @Mock
    private IEstadisticaService estadisticaService;

    @InjectMocks
    private EstadisticaController estadisticaController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testObtenerEstadisticasPorRangoFechas() {
        String fechaInicio = "2023-01-01";
        String fechaFin = "2023-01-31";
        List<EstadisticaCambioClienteResponse> mockResponse = Arrays.asList(
                new EstadisticaCambioClienteResponse(/* initialize with test data */)
        );

        when(estadisticaService.obtenerEstadisticasPorRangoFechas(fechaInicio, fechaFin)).thenReturn(mockResponse);

        ResponseEntity<List<EstadisticaCambioClienteResponse>> response = estadisticaController.obtenerEstadisticasPorRangoFechas(fechaInicio, fechaFin);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
    }

    @Test
    public void testObtenerIngresosYSalidas() {
        String fechaInicio = "2023-01-01";
        String fechaFin = "2023-01-31";
        Map<String, Integer> mockResponse = new HashMap<>();
        mockResponse.put("ingresos", 10);
        mockResponse.put("salidas", 5);

        when(estadisticaService.obtenerIngresosYSalidas(fechaInicio, fechaFin)).thenReturn(mockResponse);

        ResponseEntity<Map<String, Integer>> response = estadisticaController.obtenerIngresosYSalidas(fechaInicio, fechaFin);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
    }

    @Test
    public void testGetAllEstadisticas() {
        List<EstadisticaCambioCliente> mockResponse = Arrays.asList(
                new EstadisticaCambioCliente(/* initialize with test data */)
        );

        when(estadisticaService.getAllEstadisticas()).thenReturn(mockResponse);

        List<EstadisticaCambioCliente> response = estadisticaController.getAllEstadisticas();

        assertEquals(mockResponse, response);
    }
}