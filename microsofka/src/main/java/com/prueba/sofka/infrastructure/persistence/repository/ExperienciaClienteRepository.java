package com.prueba.sofka.infrastructure.persistence.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.prueba.sofka.domain.model.entity.ExperienciaCliente;

public interface ExperienciaClienteRepository extends JpaRepository<ExperienciaCliente, Long> {

    Optional<ExperienciaCliente> findBySofkianoIdAndClienteIdAndFechaFinIsNull(Long sofkianoId, Long clienteId);
}


