package com.prueba.sofka.application.service;

import com.prueba.sofka.domain.model.entity.Cliente;

import java.util.List;

public interface IClienteService {
    List<Cliente> findAll();
    Cliente save(Cliente cliente);
    Cliente findById(Long id);
    void deleteById(Long id);
}
