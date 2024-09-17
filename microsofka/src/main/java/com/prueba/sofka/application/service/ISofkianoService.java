package com.prueba.sofka.application.service;

import com.prueba.sofka.domain.model.entity.Sofkiano;

import java.util.List;

public interface ISofkianoService {
    List<Sofkiano> findAll();
    Sofkiano save(Sofkiano sofkiano);
    Sofkiano findById(Long id);
    void deleteById(Long id);
    void saveAll(List<Sofkiano> sofkianos);
}
