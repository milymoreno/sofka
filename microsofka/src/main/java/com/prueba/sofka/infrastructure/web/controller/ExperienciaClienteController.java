package com.prueba.sofka.infrastructure.web.controller;

import com.prueba.sofka.application.service.IExperienciaClienteService;
import com.prueba.sofka.domain.model.entity.ExperienciaCliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/experiencias")
public class ExperienciaClienteController {

    @Autowired
    private IExperienciaClienteService experienciaClienteService;

    @GetMapping
    public List<ExperienciaCliente> getAllExperiencias() {
        return experienciaClienteService.findAll();
    }

    @PostMapping
    public ResponseEntity<ExperienciaCliente> createExperiencia(@RequestBody ExperienciaCliente experienciaCliente) {
        ExperienciaCliente savedExperiencia = experienciaClienteService.save(experienciaCliente);
        return ResponseEntity.ok(savedExperiencia);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExperienciaCliente> getExperienciaById(@PathVariable Long id) {
        ExperienciaCliente experienciaCliente = experienciaClienteService.findById(id);
        if (experienciaCliente != null) {
            return ResponseEntity.ok(experienciaCliente);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExperienciaCliente> updateExperiencia(@PathVariable Long id, @RequestBody ExperienciaCliente experienciaCliente) {
        experienciaCliente.setId(id);
        ExperienciaCliente updatedExperiencia = experienciaClienteService.save(experienciaCliente);
        return ResponseEntity.ok(updatedExperiencia);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExperiencia(@PathVariable Long id) {
        experienciaClienteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
