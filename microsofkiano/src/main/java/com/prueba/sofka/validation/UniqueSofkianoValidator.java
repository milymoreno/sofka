package com.prueba.sofka.validation;

import java.util.Optional;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.prueba.sofka.domain.model.entity.Sofkiano;
import com.prueba.sofka.infrastructure.persistence.repository.SofkianoRepository;

public class UniqueSofkianoValidator implements ConstraintValidator<UniqueSofkiano, Sofkiano> {

    @Autowired
    private SofkianoRepository sofkianoRepository;

    @Override
    public void initialize(UniqueSofkiano constraintAnnotation) {
    }

    @Override
    public boolean isValid(Sofkiano sofkiano, ConstraintValidatorContext context) {
        if (sofkiano == null) {
            return true; 
        }
        // Verifica si ya existe un sofkiano con el mismo tipo y número de identificación
        Optional<Sofkiano> existingSofkiano = sofkianoRepository.findByTipoIdentificacionAndNumeroIdentificacion(
                sofkiano.getTipoIdentificacion(), sofkiano.getNumeroIdentificacion());

        if (existingSofkiano.isEmpty()) {
            return true;
        }    
        return existingSofkiano.get().getId().equals(sofkiano.getId());
    }

}

