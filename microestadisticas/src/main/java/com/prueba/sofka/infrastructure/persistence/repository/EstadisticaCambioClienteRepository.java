package com.prueba.sofka.infrastructure.persistence.repository;

import com.prueba.sofka.domain.model.entity.EstadisticaCambioCliente;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;

@EnableScan
public interface EstadisticaCambioClienteRepository extends CrudRepository<EstadisticaCambioCliente, String> {

    Optional<EstadisticaCambioCliente> findBySofkianoId(String sofkianoId);

    List<EstadisticaCambioCliente> findByFechaBetween(String fechaInicio, String fechaFin);
}
