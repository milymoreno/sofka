package com.prueba.sofka.domain.service;

import com.prueba.sofka.domain.model.entity.Cliente;
import com.prueba.sofka.domain.model.entity.EstadisticaCambioCliente;
import com.prueba.sofka.infrastructure.persistence.repository.EstadisticaCambioClienteRepository;
import org.springframework.stereotype.Service;
import com.prueba.sofka.application.service.IEstadisticaService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class EstadisticaService implements IEstadisticaService {

    private final EstadisticaCambioClienteRepository repository;

    public EstadisticaService(EstadisticaCambioClienteRepository repository) {
        this.repository = repository;
    }

    @Override
    public void procesarEvento(Map<String, Object> evento) {
        String tipoEvento = (String) evento.get("tipoEvento");
        String sofkianoId = (String) evento.get("sofkianoId");
        String nombreSofkiano = (String) evento.get("nombre");

        // Extraer la información del cliente asociado al evento
        Map<String, Object> clienteInfo = (Map<String, Object>) evento.get("cliente");
        String clienteId = (String) clienteInfo.get("clienteId");
        String nombreCliente = (String) clienteInfo.get("nombreCliente");
        String fecha = (String) clienteInfo.get("fecha");

        // Crear el objeto Cliente
        Cliente cliente = new Cliente(clienteId, nombreCliente, fecha);

        // Obtener o crear la estadística del sofkiano
        EstadisticaCambioCliente estadistica = repository.findBySofkianoId(sofkianoId)
            .orElse(new EstadisticaCambioCliente(sofkianoId, nombreSofkiano));

        // Dependiendo del tipo de evento, lo guardamos en ingreso o egreso
        if (tipoEvento.equalsIgnoreCase("INGRESO")) {
            estadistica.getClientesIngreso().add(cliente);
        } else if (tipoEvento.equalsIgnoreCase("EGRESO")) {
            estadistica.getClientesEgreso().add(cliente);
        }

        // Guardar la estadística actualizada en DynamoDB
        repository.save(estadistica);
    }

    @Override
    public List<EstadisticaCambioCliente> obtenerEstadisticasPorRangoFechas(String fechaInicio, String fechaFin) {
        // Lógica para obtener las estadísticas de DynamoDB según el rango de fechas
        return repository.findByFechaBetween(fechaInicio, fechaFin);
    }

    @Override
    public Map<String, Integer> obtenerIngresosYSalidas(String fechaInicio, String fechaFin) {
        // Lógica para obtener las estadísticas de ingresos y egresos en un rango de fechas
        List<EstadisticaCambioCliente> estadisticas = repository.findByFechaBetween(fechaInicio, fechaFin);

        int ingresos = estadisticas.stream().mapToInt(e -> e.getClientesIngreso().size()).sum();
        int egresos = estadisticas.stream().mapToInt(e -> e.getClientesEgreso().size()).sum();

        return Map.of("ingresos", ingresos, "egresos", egresos);
    }
}

