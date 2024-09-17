package com.prueba.sofka.infrastructure.persistence.repository;

import com.prueba.sofka.domain.model.entity.ExperienciaCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExperienciaClienteRepository extends JpaRepository<ExperienciaCliente, Long> {
}

