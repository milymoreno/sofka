package com.prueba.sofkaestadisticas.infraestructure.web.controller;

import com.prueba.sofkaestadisticas.aplicattion.service.IEstadisticaService;
import com.prueba.sofkaestadisticas.domain.model.entity.Cliente;
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

    // Crear listas de clientes de ingreso y egreso con fechas dentro del rango
    List<Cliente> clientesIngreso1 = Arrays.asList(
        new Cliente("1", "Cliente A", "2023-01-10"),
        new Cliente("2", "Cliente B", "2023-01-15")
    );
    List<Cliente> clientesEgreso1 = Arrays.asList(
        new Cliente("3", "Cliente C", "2023-01-12")
    );

    List<Cliente> clientesIngreso2 = Arrays.asList(
        new Cliente("4", "Cliente D", "2023-01-18")
    );
    List<Cliente> clientesEgreso2 = Arrays.asList(
        new Cliente("5", "Cliente E", "2023-01-22"),
        new Cliente("6", "Cliente F", "2023-01-25")
    );

    // Crear las instancias de EstadisticaCambioCliente con listas de clientes
    List<EstadisticaCambioCliente> mockEstadisticas = Arrays.asList(
            new EstadisticaCambioCliente("123", "Sofkiano 1", clientesIngreso1, clientesEgreso1),
            new EstadisticaCambioCliente("456", "Sofkiano 2", clientesIngreso2, clientesEgreso2)
    );

    // Mockear el servicio para devolver los datos simulados
    when(estadisticaService.obtenerEstadisticasPorRangoFechas(fechaInicio, fechaFin)).thenReturn(mockEstadisticas);

    // Llamar al método del controlador
    ResponseEntity<List<EstadisticaCambioCliente>> response = estadisticaController.obtenerEstadisticasPorRangoFechas(fechaInicio, fechaFin);

    // Verificar el código de estado y la respuesta
    assertEquals(200, response.getStatusCodeValue());
    assertEquals(mockEstadisticas, response.getBody());
}


    // @Test
    // public void testObtenerEstadisticasPorRangoFechas() {
    //     String fechaInicio = "2023-01-01";
    //     String fechaFin = "2023-01-31";
    //     List<EstadisticaCambioCliente> mockEstadisticas = Arrays.asList(
    //         new EstadisticaCambioCliente("123", "Sofkiano 1"),
    //         new EstadisticaCambioCliente("456", "Sofkiano 2")
    //     );

    //     when(estadisticaService.obtenerEstadisticasPorRangoFechas(fechaInicio, fechaFin)).thenReturn(mockEstadisticas);

    //     ResponseEntity<List<EstadisticaCambioCliente>> response = estadisticaController.obtenerEstadisticasPorRangoFechas(fechaInicio, fechaFin);

    //     assertEquals(200, response.getStatusCodeValue());
    //     assertEquals(mockEstadisticas, response.getBody());
    // }

    @Test
    public void testObtenerIngresosYSalidas() {
        String fechaInicio = "2023-01-01";
        String fechaFin = "2023-01-31";
        Map<String, Integer> mockResultado = new HashMap<>();
        mockResultado.put("ingresos", 100);
        mockResultado.put("salidas", 50);

        when(estadisticaService.obtenerIngresosYSalidas(fechaInicio, fechaFin)).thenReturn(mockResultado);

        ResponseEntity<Map<String, Integer>> response = estadisticaController.obtenerIngresosYSalidas(fechaInicio, fechaFin);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResultado, response.getBody());
    }
}