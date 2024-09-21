package com.prueba.sofkaestadisticas.infraestructure.web.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.util.List;
import com.prueba.sofkaestadisticas.aplicattion.service.IEstadisticaService;
import com.prueba.sofkaestadisticas.domain.model.entity.EstadisticaCambioCliente;

@RestController
@RequestMapping("/api/estadisticas")
public class EstadisticaController {

    private final IEstadisticaService estadisticaService;

    public EstadisticaController(IEstadisticaService estadisticaService) {
        this.estadisticaService = estadisticaService;
    }

    @GetMapping("/cambioClientes")
    public ResponseEntity<List<EstadisticaCambioCliente>> obtenerEstadisticasPorRangoFechas(
            @RequestParam String fechaInicio, 
            @RequestParam String fechaFin) {
        List<EstadisticaCambioCliente> estadisticas = estadisticaService.obtenerEstadisticasPorRangoFechas(fechaInicio, fechaFin);
        return ResponseEntity.ok(estadisticas);
    }

    @GetMapping("/ingresosVsSalidas")
    public ResponseEntity<Map<String, Integer>> obtenerIngresosYSalidas(
            @RequestParam String fechaInicio, 
            @RequestParam String fechaFin) {
        Map<String, Integer> resultado = estadisticaService.obtenerIngresosYSalidas(fechaInicio, fechaFin);
        return ResponseEntity.ok(resultado);
    }
}
