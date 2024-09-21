package com.prueba.sofka.application.service;

import com.prueba.sofka.domain.model.entity.ExperienciaCliente;
import com.prueba.sofka.domain.model.entity.Sofkiano;

import java.util.List;

public interface ISofkianoService {
    List<Sofkiano> findAll();
    Sofkiano save(Sofkiano sofkiano);
    Sofkiano findById(Long id);
    void deleteById(Long id);
    void saveAll(List<Sofkiano> sofkianos);
    ExperienciaCliente asociarCliente(Long sofkianoId, Long clienteId, String rol);
    void desasociarCliente(Long sofkianoId, Long clienteId, String descripcion);
}
