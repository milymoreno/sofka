// package com.prueba.sofkaestadisticas.domain.service;
package com.prueba.sofkaestadisticas.domain.service;

import com.prueba.sofkaestadisticas.domain.model.entity.Cliente;
import com.prueba.sofkaestadisticas.domain.model.entity.EstadisticaCambioCliente;
import com.prueba.sofkaestadisticas.infraestructure.persistencia.repository.EstadisticaCambioClienteRepository;
import org.springframework.stereotype.Service;

import com.prueba.sofkaestadisticas.aplicattion.response.ClienteInfo;
import com.prueba.sofkaestadisticas.aplicattion.response.EstadisticaCambioClienteResponse;
import com.prueba.sofkaestadisticas.aplicattion.service.IEstadisticaService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Set;
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

    @Override
    public void procesarEvento(Map<String, Object> evento) {
        String tipoEvento = (String) evento.get("tipoEvento");
        String sofkianoId = (String) evento.get("sofkianoId");
        String nombreSofkiano = (String) evento.get("nombre");

        // Validar que el sofkianoId no esté vacío o nulo
        if (sofkianoId == null || sofkianoId.trim().isEmpty()) {
            throw new IllegalArgumentException("El sofkianoId no puede ser nulo o vacío.");
        }

        // Extraer la información del cliente asociado al evento
        Map<String, Object> clienteInfo = (Map<String, Object>) evento.get("cliente");

        // Validar clienteInfo
        if (clienteInfo == null) {
            throw new IllegalArgumentException("clienteInfo no puede ser nulo en el evento: " + evento);
        }

        String clienteId = (String) clienteInfo.get("clienteId");
        String nombreCliente = (String) clienteInfo.get("nombreCliente");
        String fecha = (String) clienteInfo.get("fecha");

        // Crear el objeto Cliente
        Cliente cliente = new Cliente(clienteId, nombreCliente, fecha);

        // Obtener la estadística del sofkiano o crear una nueva si no existe
        EstadisticaCambioCliente estadistica = repository.findBySofkianoId(sofkianoId)
            .orElseGet(() -> {
                EstadisticaCambioCliente nuevaEstadistica = new EstadisticaCambioCliente(sofkianoId, nombreSofkiano);
                return nuevaEstadistica;
            });

        // Dependiendo del tipo de evento, agregar a las listas de ingresos o egresos
        if (tipoEvento.equalsIgnoreCase("INGRESO")) {
            // Verificar que el cliente no esté ya en la lista de ingresos
            if (estadistica.getClientesIngreso().stream().noneMatch(c -> c.getClienteId().equals(clienteId))) {
                estadistica.getClientesIngreso().add(cliente);
            }
        } else if (tipoEvento.equalsIgnoreCase("EGRESO")) {
            // Verificar que el cliente no esté ya en la lista de egresos
            if (estadistica.getClientesEgreso().stream().noneMatch(c -> c.getClienteId().equals(clienteId))) {
                estadistica.getClientesEgreso().add(cliente);
            }
        }
        repository.save(estadistica);
    }


    // @Override
    // public void procesarEvento(Map<String, Object> evento) {
    //     String tipoEvento = (String) evento.get("tipoEvento");
    //     String sofkianoId = (String) evento.get("sofkianoId");
    //     String nombreSofkiano = (String) evento.get("nombre");

    //     // Extraer la información del cliente asociado al evento
    //     Map<String, Object> clienteInfo = (Map<String, Object>) evento.get("cliente");

    //     // Validar clienteInfo
    //     if (clienteInfo == null) {
    //         throw new IllegalArgumentException("clienteInfo no puede ser nulo en el evento: " + evento);
    //     }

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
    //     repository.save(estadistica);
    // }

    @Override
    public List<EstadisticaCambioClienteResponse> obtenerEstadisticasPorRangoFechas(String fechaInicio, String fechaFin) {
        try {
            LocalDateTime inicio = LocalDateTime.parse(fechaInicio, formatter);
            LocalDateTime fin = LocalDateTime.parse(fechaFin, formatter);
    
            // Verificar que la fecha de inicio no sea posterior a la fecha de fin
            if (inicio.isAfter(fin)) {
                throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin.");
            }
            List<EstadisticaCambioCliente> estadisticas = (List<EstadisticaCambioCliente>) repository.findAll();
    
            return estadisticas.stream()
                .filter(estadistica -> 
                    hayClientesEnRango(estadistica.getClientesIngreso(), inicio, fin) && 
                    hayClientesEnRango(estadistica.getClientesEgreso(), inicio, fin) &&
                    tieneCambioValido(estadistica))
                .map(estadistica -> {
                    String sofkianoId = estadistica.getSofkianoId();
                    String nombreSofkiano = estadistica.getNombre();
                    
                    // Crear listas de ClienteInfo para ingreso y egreso
                    List<ClienteInfo> clientesIngreso = estadistica.getClientesIngreso().stream()
                        .filter(cliente -> estaEnRango(cliente.getFecha(), inicio, fin)) // Filtrar ingresos en rango
                        .map(cliente -> new ClienteInfo(cliente.getClienteId(), cliente.getNombreCliente(), 
                                                          LocalDateTime.parse(cliente.getFecha(), formatter)))
                        .collect(Collectors.toList());
    
                    List<ClienteInfo> clientesEgreso = estadistica.getClientesEgreso().stream()
                        .filter(cliente -> estaEnRango(cliente.getFecha(), inicio, fin)) // Filtrar egresos en rango
                        .map(cliente -> new ClienteInfo(cliente.getClienteId(), cliente.getNombreCliente(), 
                                                        LocalDateTime.parse(cliente.getFecha(), formatter)))
                        .collect(Collectors.toList());

                    // Validar que haya cambios de cliente distintos
                    if (!clientesIngreso.isEmpty() && !clientesEgreso.isEmpty()) {
                        boolean cambioValido = clientesIngreso.stream().anyMatch(ingreso -> 
                            clientesEgreso.stream().anyMatch(egreso -> 
                                !ingreso.getClienteId().equals(egreso.getClienteId())
                            )
                        );

                        if (cambioValido) {
                            return new EstadisticaCambioClienteResponse(sofkianoId, nombreSofkiano, 
                                                                        clientesEgreso, clientesIngreso);
                        }
                    }

                    return null; // Ignorar si no hay cambio válido
    
                    // Retornar solo si hay al menos un cliente de ingreso y de egreso
                    // if (!clientesIngreso.isEmpty() && !clientesEgreso.isEmpty()) {
                    //     return new EstadisticaCambioClienteResponse(sofkianoId, nombreSofkiano, 
                    //                                                 clientesIngreso, clientesEgreso);
                    // } else {
                    //     return null; // Ignorar si no hay cambio válido
                    // }
                })
                .filter(response -> response != null) // Filtrar nulos
                .collect(Collectors.toList());
    
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de fecha inválido: " + e.getMessage());
        }
    }



    private boolean tieneCambioValido(EstadisticaCambioCliente estadistica) {
        return !estadistica.getClientesIngreso().isEmpty() && !estadistica.getClientesEgreso().isEmpty();
    }

    
    // @Override
    // public Map<String, Integer> obtenerIngresosYSalidas(String fechaInicio, String fechaFin) {
    //     LocalDateTime inicio = LocalDateTime.parse(fechaInicio, formatter);
    //     LocalDateTime fin = LocalDateTime.parse(fechaFin, formatter);

    //     List<EstadisticaCambioCliente> estadisticas = (List<EstadisticaCambioCliente>) repository.findAll();

    //     int ingresos = estadisticas.stream()
    //         .mapToInt(e -> (int) e.getClientesIngreso().stream()
    //             .filter(cliente -> estaEnRango(cliente.getFecha(), inicio, fin))
    //             .count())
    //         .sum();

    //     int egresos = estadisticas.stream()
    //         .mapToInt(e -> (int) e.getClientesEgreso().stream()
    //             .filter(cliente -> estaEnRango(cliente.getFecha(), inicio, fin))
    //             .count())
    //         .sum();

    //     return Map.of("ingresos", ingresos, "egresos", egresos);
    // }

    @Override
    public Map<String, Integer> obtenerIngresosYSalidas(String fechaInicio, String fechaFin) {
        LocalDateTime inicio = LocalDateTime.parse(fechaInicio, formatter);
        LocalDateTime fin = LocalDateTime.parse(fechaFin, formatter);

        List<EstadisticaCambioCliente> estadisticas = (List<EstadisticaCambioCliente>) repository.findAll();

        // Contar ingresos únicos
        Set<String> ingresosUnicos = new HashSet<>();
        estadisticas.forEach(e -> 
            e.getClientesIngreso().stream()
                .filter(cliente -> estaEnRango(cliente.getFecha(), inicio, fin))
                .forEach(cliente -> ingresosUnicos.add(cliente.getClienteId()))
        );

        // Contar egresos únicos
        Set<String> egresosUnicos = new HashSet<>();
        estadisticas.forEach(e -> 
            e.getClientesEgreso().stream()
                .filter(cliente -> estaEnRango(cliente.getFecha(), inicio, fin))
                .forEach(cliente -> egresosUnicos.add(cliente.getClienteId()))
        );

        int ingresos = ingresosUnicos.size();
        int egresos = egresosUnicos.size();

        return Map.of("Ingresos", ingresos, "Egresos", egresos);
    }


    @Override
    public List<EstadisticaCambioCliente> getAllEstadisticas() {
        return (List<EstadisticaCambioCliente>) repository.findAll(); 
    }

    private boolean hayClientesEnRango(List<Cliente> clientes, LocalDateTime inicio, LocalDateTime fin) {
        return clientes.stream().anyMatch(cliente -> {
            LocalDateTime fechaCliente = LocalDateTime.parse(cliente.getFecha(), formatter);
            return (fechaCliente.isAfter(inicio) || fechaCliente.isEqual(inicio)) &&
                   (fechaCliente.isBefore(fin) || fechaCliente.isEqual(fin));
        });
    }

    private boolean estaEnRango(String fecha, LocalDateTime inicio, LocalDateTime fin) {
        LocalDateTime fechaEvento = LocalDateTime.parse(fecha, formatter);
        return (fechaEvento.isEqual(inicio) || fechaEvento.isAfter(inicio)) &&
               (fechaEvento.isEqual(fin) || fechaEvento.isBefore(fin));
    }
}

