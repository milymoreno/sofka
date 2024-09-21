package com.prueba.sofka.domain.service;

import com.prueba.sofka.domain.model.entity.Cliente;
import com.prueba.sofka.infrastructure.persistence.repository.ClienteRepository;
import com.prueba.sofka.application.service.IClienteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService implements IClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente save(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente findById(Long id) {
        return clienteRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        clienteRepository.deleteById(id);
    }
}
