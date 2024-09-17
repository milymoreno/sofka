package com.prueba.sofka.domain.service;

import com.prueba.sofka.application.service.ISofkianoService;
import com.prueba.sofka.domain.model.entity.Sofkiano;
import com.prueba.sofka.infrastructure.persistence.repository.SofkianoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
// Implementar pruebas unitarias para esta clase con junit y mockito

// Implement unit tests for this class with junit and mockito

@Service
public class SofkianoService implements ISofkianoService {

    @Autowired
    private SofkianoRepository sofkianoRepository;

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
}
