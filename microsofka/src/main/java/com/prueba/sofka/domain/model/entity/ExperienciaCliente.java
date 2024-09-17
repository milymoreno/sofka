package com.prueba.sofka.domain.model.entity;

import com.prueba.sofka.domain.model.entity.Cliente;
import com.prueba.sofka.domain.model.entity.Sofkiano;

import java.time.LocalDateTime;
import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "experiencia_cliente")
public class ExperienciaCliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sofkiano_id", nullable = false)
    private Sofkiano sofkiano;

    
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(nullable = false)
    private LocalDateTime fechaInicio;

    @Column(nullable = true) // Puede ser null si la experiencia aún está en curso
    private LocalDateTime fechaFin;

    private String rol;

    private String descripcion;

   
}
