package com.prueba.sofkaestadisticas.aplicattion.service;

import com.prueba.sofkaestadisticas.aplicattion.response.EstadisticaCambioClienteResponse;
import com.prueba.sofkaestadisticas.domain.model.entity.EstadisticaCambioCliente;

import java.util.List;
import java.util.Map;

public interface IEstadisticaService {
    void procesarEvento(Map<String, Object> evento);
    //List<EstadisticaCambioCliente> obtenerEstadisticasPorRangoFechas(String fechaInicio, String fechaFin);
    List<EstadisticaCambioClienteResponse> obtenerEstadisticasPorRangoFechas(String fechaInicio, String fechaFin);

    Map<String, Integer> obtenerIngresosYSalidas(String fechaInicio, String fechaFin);
	Map<String, Map<String, Integer>> obtenerIngresosYSalidasPorCliente(String fechaInicio, String fechaFin);
	
    List<EstadisticaCambioCliente> getAllEstadisticas();
}

