package com.prueba.sofka.domain.model.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del cliente es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "La direccion es obligatoria")
    @Column(nullable = false)
    private String direccion;

    @NotBlank(message = "El telèfono es obligatorio")
    @Column(nullable = false)
    private String telefono;
}



