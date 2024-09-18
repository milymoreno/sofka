package com.prueba.sofka.application.service;

import com.prueba.sofka.domain.model.entity.EstadisticaCambioCliente;

import java.util.List;
import java.util.Map;

public interface IEstadisticaService {
    //void guardarEvento(Map<String, Object> evento);
    void procesarEvento(Map<String, Object> evento);
    List<EstadisticaCambioCliente> obtenerEstadisticasPorRangoFechas(String fechaInicio, String fechaFin);
    Map<String, Integer> obtenerIngresosYSalidas(String fechaInicio, String fechaFin);
}
