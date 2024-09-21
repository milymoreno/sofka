package com.prueba.sofka.infrastructure.persistence.repository;

import com.prueba.sofka.domain.model.enums.TipoIdentificacion;
import com.prueba.sofka.domain.model.entity.Sofkiano;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SofkianoRepository extends JpaRepository<Sofkiano, Long> {
    Optional<Sofkiano> findByTipoIdentificacionAndNumeroIdentificacion(TipoIdentificacion tipoIdentificacion, String numeroIdentificacion);
}
