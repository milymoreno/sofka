package com.prueba.sofkaestadisticas.domain.service;

import com.prueba.sofkaestadisticas.domain.model.entity.Cliente;
import com.prueba.sofkaestadisticas.domain.model.entity.EstadisticaCambioCliente;
import com.prueba.sofkaestadisticas.infraestructure.persistencia.repository.EstadisticaCambioClienteRepository;
import org.springframework.stereotype.Service;
import com.prueba.sofkaestadisticas.aplicattion.service.IEstadisticaService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EstadisticaService implements IEstadisticaService {

    private final EstadisticaCambioClienteRepository repository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public EstadisticaService(EstadisticaCambioClienteRepository repository) {
        this.repository = repository;
    }

    //@Override
    // public void procesarEvento(Map<String, Object> evento) {
    //     String tipoEvento = (String) evento.get("tipoEvento");
    //     String sofkianoId = (String) evento.get("sofkianoId");
    //     String nombreSofkiano = (String) evento.get("nombre");

    //     // Extraer la información del cliente asociado al evento
    //     Map<String, Object> clienteInfo = (Map<String, Object>) evento.get("cliente");
    //     String clienteId = (String) clienteInfo.get("clienteId");
    //     String nombreCliente = (String) clienteInfo.get("nombreCliente");
    //     String fecha = (String) clienteInfo.get("fecha");

    //     // Crear el objeto Cliente
    //     Cliente cliente = new Cliente(clienteId, nombreCliente, fecha);

    //     // Obtener o crear la estadística del sofkiano
    //     EstadisticaCambioCliente estadistica = repository.findBySofkianoId(sofkianoId)
    //         .orElse(new EstadisticaCambioCliente(sofkianoId, nombreSofkiano));

    //     // Dependiendo del tipo de evento, lo guardamos en ingreso o egreso
    //     if (tipoEvento.equalsIgnoreCase("INGRESO")) {
    //         estadistica.getClientesIngreso().add(cliente);
    //     } else if (tipoEvento.equalsIgnoreCase("EGRESO")) {
    //         estadistica.getClientesEgreso().add(cliente);
    //     }

    //     // Guardar la estadística actualizada en DynamoDB
    //     repository.save(estadistica);
    // }

    @Override
    public void procesarEvento(Map<String, Object> evento) {
        String tipoEvento = (String) evento.get("tipoEvento");
        String sofkianoId = (String) evento.get("sofkianoId");
        String nombreSofkiano = (String) evento.get("nombre");

        // Extraer la información del cliente asociado al evento
        Map<String, Object> clienteInfo = (Map<String, Object>) evento.get("cliente");

        // Validar clienteInfo
        if (clienteInfo == null) {
            // Manejar el caso en que clienteInfo es null
            throw new IllegalArgumentException("clienteInfo no puede ser nulo en el evento: " + evento);
        }

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
        repository.save(estadistica);
    }


    @Override
    public List<EstadisticaCambioCliente> obtenerEstadisticasPorRangoFechas(String fechaInicio, String fechaFin) {
        try {
            // Convertir las fechas de String a LocalDateTime
            LocalDateTime inicio = LocalDateTime.parse(fechaInicio, formatter);
            LocalDateTime fin = LocalDateTime.parse(fechaFin, formatter);

            // Verificar que la fecha de inicio no sea posterior a la fecha de fin
            if (inicio.isAfter(fin)) {
                throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin.");
            }

            // Obtener todas las estadísticas
            List<EstadisticaCambioCliente> estadisticas = (List<EstadisticaCambioCliente>) repository.findAll();

            // Filtrar por fecha dentro de los clientes de ingreso y egreso
            return estadisticas.stream()
                .filter(estadistica -> 
                    hayClientesEnRango(estadistica.getClientesIngreso(), inicio, fin) ||
                    hayClientesEnRango(estadistica.getClientesEgreso(), inicio, fin)
                )
                .collect(Collectors.toList());

        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de fecha inválido: " + e.getMessage());
        }
    }    

    @Override
    public Map<String, Integer> obtenerIngresosYSalidas(String fechaInicio, String fechaFin) {
        // Convertir las fechas de String a LocalDateTime
        LocalDateTime inicio = LocalDateTime.parse(fechaInicio, formatter);
        LocalDateTime fin = LocalDateTime.parse(fechaFin, formatter);

        // Obtener todas las estadísticas
        List<EstadisticaCambioCliente> estadisticas = (List<EstadisticaCambioCliente>) repository.findAll();

        // Contar ingresos y egresos en el rango de fechas
        int ingresos = estadisticas.stream()
            .mapToInt(e -> (int) e.getClientesIngreso().stream()
                .filter(cliente -> estaEnRango(cliente.getFecha(), inicio, fin))
                .count())
            .sum();

        int egresos = estadisticas.stream()
            .mapToInt(e -> (int) e.getClientesEgreso().stream()
                .filter(cliente -> estaEnRango(cliente.getFecha(), inicio, fin))
                .count())
            .sum();

        return Map.of("ingresos", ingresos, "egresos", egresos);
    }

    // Método auxiliar para verificar si una fecha está en el rango especificado
    private boolean hayClientesEnRango(List<Cliente> clientes, LocalDateTime inicio, LocalDateTime fin) {
        return clientes.stream().anyMatch(cliente -> estaEnRango(cliente.getFecha(), inicio, fin));
    }
    
    private boolean estaEnRango(String fecha, LocalDateTime inicio, LocalDateTime fin) {
        LocalDateTime fechaEvento = LocalDateTime.parse(fecha, formatter);
        return (fechaEvento.isEqual(inicio) || fechaEvento.isAfter(inicio)) &&
               (fechaEvento.isEqual(fin) || fechaEvento.isBefore(fin));
    }
}
