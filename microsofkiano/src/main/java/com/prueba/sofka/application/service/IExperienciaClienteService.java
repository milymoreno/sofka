package com.prueba.sofka.application.service;

import com.prueba.sofka.domain.model.entity.ExperienciaCliente;

import java.util.List;

public interface IExperienciaClienteService {
    List<ExperienciaCliente> findAll();
    ExperienciaCliente save(ExperienciaCliente experienciaCliente);
    ExperienciaCliente findById(Long id);
    void deleteById(Long id);
}
