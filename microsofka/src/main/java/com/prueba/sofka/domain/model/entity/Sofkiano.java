package com.prueba.sofka.domain.model.entity;

import java.time.LocalDateTime;

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
    //@NotBlank(message = "El tipo de identificación es obligatorio")
    private TipoIdentificacion tipoIdentificacion;

    @NotBlank(message = "El número de identificación es obligatorio")
    @Column(nullable = false)
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

    @NotBlank(message = "La descripciòn del perfil es obligatrorio")
    @Column(nullable = false)
    private String perfil;

    //@NotBlank(message = "la cantidad de años de expericia es obligatoria")
    @Column(nullable = false)
    private Integer cantidadAniosExperiencia;

    //@NotBlank(message = "La fecha de nacimiento el obligatoria")
    @Column(nullable = false)
    private LocalDateTime fechaNacimiento;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaModificacion;

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.fechaModificacion = LocalDateTime.now();
    }
}
