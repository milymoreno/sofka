package com.prueba.sofkaestadisticas.infraestructure.persistencia.repository;

import com.prueba.sofkaestadisticas.domain.model.entity.EstadisticaCambioCliente;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;

@EnableScan
public interface EstadisticaCambioClienteRepository extends CrudRepository<EstadisticaCambioCliente, String> {

    Optional<EstadisticaCambioCliente> findBySofkianoId(String sofkianoId);

    //List<EstadisticaCambioCliente> findByFechaBetween(String fechaInicio, String fechaFin);
}
