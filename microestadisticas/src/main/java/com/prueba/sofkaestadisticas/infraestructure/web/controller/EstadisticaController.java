package com.prueba.sofkaestadisticas.infraestructure.web.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.util.List;

import com.prueba.sofkaestadisticas.aplicattion.response.EstadisticaCambioClienteResponse;
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
    public ResponseEntity<List<EstadisticaCambioClienteResponse>> obtenerEstadisticasPorRangoFechas(
            @RequestParam String fechaInicio, 
            @RequestParam String fechaFin) {
        List<EstadisticaCambioClienteResponse> estadisticas = estadisticaService.obtenerEstadisticasPorRangoFechas(fechaInicio, fechaFin);
        return ResponseEntity.ok(estadisticas);
    }
    
    @GetMapping("/ingresosVsSalidas")
    public ResponseEntity<Map<String, Integer>> obtenerIngresosYSalidas(
            @RequestParam String fechaInicio, 
            @RequestParam String fechaFin) {
        Map<String, Integer> resultado = estadisticaService.obtenerIngresosYSalidas(fechaInicio, fechaFin);
        return ResponseEntity.ok(resultado);
    }
	
	@GetMapping("/ingresosVsSalidasPorCliente")
    public ResponseEntity<Map<String, Map<String, Integer>>> obtenerIngresosYSalidasPorClente(
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin) {
        
        Map<String, Map<String, Integer>> resultado = estadisticaService.obtenerIngresosYSalidasPorCliente(fechaInicio, fechaFin);
        
        return ResponseEntity.ok(resultado);
    }

    @GetMapping
    public List<EstadisticaCambioCliente> getAllEstadisticas() {
        return estadisticaService.getAllEstadisticas(); 
    }
}
