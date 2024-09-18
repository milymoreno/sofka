package com.prueba.sofka.domain.service;


import com.prueba.sofka.application.service.ISofkianoService;
import com.prueba.sofka.domain.model.entity.Cliente;
import com.prueba.sofka.domain.model.entity.ExperienciaCliente;
import com.prueba.sofka.domain.model.entity.Sofkiano;
import com.prueba.sofka.infrastructure.persistence.repository.ClienteRepository;
import com.prueba.sofka.infrastructure.persistence.repository.ExperienciaClienteRepository;
import com.prueba.sofka.infrastructure.persistence.repository.SofkianoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SofkianoService implements ISofkianoService {

    private final SofkianoRepository sofkianoRepository;
    private final ClienteRepository clienteRepository;
    private final ExperienciaClienteRepository experienciaClienteRepository;

    public SofkianoService(SofkianoRepository sofkianoRepository, ClienteRepository clienteRepository, ExperienciaClienteRepository experienciaClienteRepository) {
        this.sofkianoRepository = sofkianoRepository;
        this.clienteRepository = clienteRepository;
        this.experienciaClienteRepository = experienciaClienteRepository;
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

        ExperienciaCliente experienciaCliente = new ExperienciaCliente();
        experienciaCliente.setSofkiano(sofkiano);
        experienciaCliente.setCliente(cliente);
        experienciaCliente.setRol(rol);

        return experienciaClienteRepository.save(experienciaCliente); // Guarda la experiencia
    }

    @Override
    @Transactional
    public void desasociarCliente(Long sofkianoId, Long clienteId, String descripcion) {
        ExperienciaCliente experienciaCliente = experienciaClienteRepository
            .findBySofkianoIdAndClienteIdAndFechaFinIsNull(sofkianoId, clienteId)
            .orElseThrow(() -> new IllegalArgumentException("No existe asociación activa entre el Sofkiano y el Cliente"));

        experienciaCliente.setFechaFin(LocalDateTime.now());
        experienciaCliente.setDescripcion(descripcion);

        experienciaClienteRepository.save(experienciaCliente); // Actualiza la experiencia con fecha de fin
    }
}
