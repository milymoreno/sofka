package com.prueba.sofka.domain.model.entity;

import lombok.Data;

import jakarta.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @ManyToMany
    @JoinTable(
        name = "cliente_sofkiano", // Nombre de la tabla intermedia
        joinColumns = @JoinColumn(name = "cliente_id"),
        inverseJoinColumns = @JoinColumn(name = "sofkiano_id")
    )
    private Set<Sofkiano> sofkianos; // Relación muchos a muchos con Sofkiano
}



