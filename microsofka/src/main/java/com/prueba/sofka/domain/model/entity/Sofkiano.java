package com.prueba.sofka.domain.model.entity;

import java.time.LocalDateTime;
import java.util.Set;

import com.prueba.sofka.domain.model.enums.TipoIdentificacion;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "sofkiano")
public class Sofkiano {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotBlank(message = "El tipo de identificación es obligatorio")
    private TipoIdentificacion tipoIdentificacion;

    @NotBlank(message = "El número de identificación es obligatorio")
    @Column(nullable = false, unique = true)
    private String numeroIdentificacion;

    @NotBlank(message = "Los nombres son obligatorios")
    @Length(min = 2, message = "Los nombres deben tener al menos 2 caracteres")
    @Column(nullable = false)
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Length(min = 2, message = "Los apellidos deben tener al menos 2 caracteres")
    @Column(nullable = false)
    private String apellidos;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false)
    private Boolean activo;

    @Email
    @NotBlank(message = "El email es obligatorio")
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private LocalDateTime fechaNacimiento;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaModificacion;

    

    @ManyToMany
    @JoinTable(
        name = "cliente_sofkiano", 
        joinColumns = @JoinColumn(name = "sofkiano_id"),
        inverseJoinColumns = @JoinColumn(name = "cliente_id")
    )
    private Set<Cliente> clientes; // Relación muchos a muchos con Cliente

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.fechaModificacion = LocalDateTime.now();
    }
}
