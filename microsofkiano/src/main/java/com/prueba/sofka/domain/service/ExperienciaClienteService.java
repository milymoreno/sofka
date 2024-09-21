package com.prueba.sofka.domain.service;

import com.prueba.sofka.domain.model.entity.ExperienciaCliente;
import com.prueba.sofka.infrastructure.persistence.repository.ExperienciaClienteRepository;
import com.prueba.sofka.application.service.IExperienciaClienteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExperienciaClienteService implements IExperienciaClienteService {

    @Autowired
    private ExperienciaClienteRepository experienciaClienteRepository;

    @Override
    public List<ExperienciaCliente> findAll() {
        return experienciaClienteRepository.findAll();
    }

    @Override
    public ExperienciaCliente save(ExperienciaCliente experienciaCliente) {
        return experienciaClienteRepository.save(experienciaCliente);
    }

    @Override
    public ExperienciaCliente findById(Long id) {
        return experienciaClienteRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        experienciaClienteRepository.deleteById(id);
    }
}

