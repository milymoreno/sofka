package com.prueba.sofka.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
//import com.prueba.sofka.application.components.SofkianoEventProducer;
//import com.prueba.sofka.domain.service.SofkianoEventProducerRoute;

import com.prueba.sofka.application.service.ISofkianoService;
import com.prueba.sofka.domain.model.entity.Cliente;
import com.prueba.sofka.domain.model.entity.ExperienciaCliente;
import com.prueba.sofka.domain.model.entity.Sofkiano;
import com.prueba.sofka.infrastructure.persistence.repository.ClienteRepository;
import com.prueba.sofka.infrastructure.persistence.repository.ExperienciaClienteRepository;
import com.prueba.sofka.infrastructure.persistence.repository.SofkianoRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.List;

@Service
public class SofkianoService implements ISofkianoService {

    private final SofkianoRepository sofkianoRepository;
    private final ClienteRepository clienteRepository;
    private final ExperienciaClienteRepository experienciaClienteRepository;
    //private final SofkianoEventProducer sofkianoEventProducer;
    private final SofkianoEventProducerRoute sofkianoEventProducer;
    
    public SofkianoService(SofkianoRepository sofkianoRepository, ClienteRepository clienteRepository, 
                           ExperienciaClienteRepository experienciaClienteRepository, 
                           SofkianoEventProducerRoute sofkianoEventProducer) {
        this.sofkianoRepository = sofkianoRepository;
        this.clienteRepository = clienteRepository;
        this.experienciaClienteRepository = experienciaClienteRepository;
        this.sofkianoEventProducer = sofkianoEventProducer;
        
    }

    @Override
    public List<Sofkiano> findAll() {
        return sofkianoRepository.findAll();
    }

    @Override
    public Sofkiano save(Sofkiano sofkiano) {
        return sofkianoRepository.save(sofkiano);
    }

    @Override
    public Sofkiano findById(Long id) {
        return sofkianoRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        sofkianoRepository.deleteById(id);
    }

    @Override
    public void saveAll(List<Sofkiano> sofkianos) {
        sofkianoRepository.saveAll(sofkianos);
    }

    @Override
    @Transactional
    public ExperienciaCliente asociarCliente(Long sofkianoId, Long clienteId, String rol) {
        Sofkiano sofkiano = sofkianoRepository.findById(sofkianoId)
            .orElseThrow(() -> new IllegalArgumentException("Sofkiano no encontrado"));
        
        Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

        // Buscar experiencia activa con el cliente específico
        Optional<ExperienciaCliente> experienciaExistente = experienciaClienteRepository.findBySofkianoIdAndClienteIdAndFechaFinIsNull(sofkianoId, clienteId);

        if (experienciaExistente.isPresent()) {
            ExperienciaCliente experiencia = experienciaExistente.get();
            String mensaje = String.format("El sofkiano %s ya está asociado al cliente %s con el rol: %s.", 
                                            sofkiano.getNombres(), 
                                            cliente.getNombre(), 
                                            experiencia.getRol());
            throw new IllegalArgumentException(mensaje);
        }
        
        ExperienciaCliente experienciaCliente = new ExperienciaCliente();
        experienciaCliente.setSofkiano(sofkiano);
        experienciaCliente.setCliente(cliente);
        experienciaCliente.setRol(rol);

        ExperienciaCliente savedExperiencia = experienciaClienteRepository.save(experienciaCliente);

        // Publicar evento de ingreso en RabbitMQ
        publicarEvento(
            sofkianoId.toString(),
            sofkiano.getNombres(),
            clienteId.toString(),
            cliente.getNombre(),
            "INGRESO"
        );
        
        return savedExperiencia;
    }



    @Override
    @Transactional
    public void desasociarCliente(Long sofkianoId, Long clienteId, String descripcion) {
        
        Sofkiano sofkiano = sofkianoRepository.findById(sofkianoId)
            .orElseThrow(() -> new IllegalArgumentException("Sofkiano no encontrado"));
        
        Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

        ExperienciaCliente experienciaCliente = experienciaClienteRepository
            .findBySofkianoIdAndClienteIdAndFechaFinIsNull(sofkianoId, clienteId)
            .orElseThrow(() -> new IllegalArgumentException("No existe asociación activa entre el Sofkiano y el Cliente"));

        experienciaCliente.setFechaFin(LocalDateTime.now());
        experienciaCliente.setDescripcion(descripcion);

        experienciaClienteRepository.save(experienciaCliente);

        // Publicar evento de egreso en RabbitMQ
        // sofkianoEventProducer.sendSofkianoChangeEvent(
        //     sofkianoId.toString(),
        //     sofkiano.getNombres(),
        //     clienteId.toString(),
        //     cliente.getNombre(),
        //     LocalDateTime.now().toString(),
        //     "EGRESO"
        // );

        publicarEvento(
            sofkianoId.toString(),
            sofkiano.getNombres(),
            clienteId.toString(),
            cliente.getNombre(),
            "EGRESO"
        );
    } 
    
    public void publicarEvento(String sofkianoId, String nombreSofkiano, String clienteId, String nombreCliente, String tipoEvento) {
        Map<String, Object> evento = new HashMap<>();
        evento.put("sofkianoId", sofkianoId);
        evento.put("nombre", nombreSofkiano);
        Map<String, Object> clienteInfo = new HashMap<>();
        clienteInfo.put("clienteId", clienteId);
        clienteInfo.put("nombreCliente", nombreCliente);
        clienteInfo.put("fecha", LocalDateTime.now().toString());
        evento.put("cliente", clienteInfo);
        evento.put("tipoEvento", tipoEvento);
        // Envía el evento utilizando el productor
        sofkianoEventProducer.sendEvent(evento);
    }
       
}
