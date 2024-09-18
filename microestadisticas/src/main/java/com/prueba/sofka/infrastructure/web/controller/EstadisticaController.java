package com.prueba.sofka.infrastructure.web.controller;

import com.prueba.sofka.domain.service.EstadisticaService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/estadisticas")
public class EstadisticaController {

    private final EstadisticaService estadisticaService;

    public EstadisticaController(EstadisticaService estadisticaService) {
        this.estadisticaService = estadisticaService;
    }

    // // Endpoint para procesar un evento de ingreso o egreso de cliente
    // @PostMapping("/procesar-evento")
    // public void procesarEvento(@RequestBody Map<String, Object> evento) {
    //     estadisticaService.procesarEvento(evento);
    // }

    // Endpoint para obtener estadísticas de cambios de clientes por un sofkiano en un rango de fechas
    @GetMapping("/cambios-cliente")
    public Map<String, Integer> obtenerCambiosDeCliente(
            @RequestParam String sofkianoId,
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin) {
        return estadisticaService.obtenerIngresosYSalidas(fechaInicio, fechaFin);
    }

    // Endpoint para obtener el total de ingresos vs egresos de clientes en un rango de fechas
    @GetMapping("/ingresos-vs-egresos")
    public Map<String, Integer> obtenerIngresosVsEgresos(
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin) {
        return estadisticaService.obtenerIngresosYSalidas(fechaInicio, fechaFin);
    }
}
