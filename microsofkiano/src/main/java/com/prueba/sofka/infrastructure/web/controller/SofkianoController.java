package com.prueba.sofka.infrastructure.web.controller;

import com.prueba.sofka.application.service.ISofkianoService;
import com.prueba.sofka.domain.model.entity.ExperienciaCliente;
import com.prueba.sofka.domain.model.entity.Sofkiano;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/sofkianos")
public class SofkianoController {

    @Autowired
    private ISofkianoService sofkianoService;

    @GetMapping
    public List<Sofkiano> getAllSofkianos() {
        return sofkianoService.findAll();
    }

    @PostMapping
    public ResponseEntity<Sofkiano> createSofkiano(@RequestBody Sofkiano sofkiano) {
        Sofkiano savedSofkiano = sofkianoService.save(sofkiano);
        return ResponseEntity.ok(savedSofkiano);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sofkiano> getSofkianoById(@PathVariable Long id) {
        Sofkiano sofkiano = sofkianoService.findById(id);
        if (sofkiano != null) {
            return ResponseEntity.ok(sofkiano);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sofkiano> updateSofkiano(@PathVariable Long id, @RequestBody Sofkiano sofkiano) {
        sofkiano.setId(id);
        Sofkiano updatedSofkiano = sofkianoService.save(sofkiano);
        return ResponseEntity.ok(updatedSofkiano);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSofkiano(@PathVariable Long id) {
        return sofkianoService.deleteSofkiano(id);
    }

    @PostMapping("/{sofkianoId}/clientes/{clienteId}/asociar")
    public ResponseEntity<ExperienciaCliente> asociarCliente(
            @PathVariable Long sofkianoId, 
            @PathVariable Long clienteId,
            @RequestParam String rol) {
        ExperienciaCliente experienciaCliente = sofkianoService.asociarCliente(sofkianoId, clienteId, rol);
        return ResponseEntity.ok(experienciaCliente);
    }

    @PostMapping("/{sofkianoId}/clientes/{clienteId}/desasociar")
    public ResponseEntity<Void> desasociarCliente(
            @PathVariable Long sofkianoId, 
            @PathVariable Long clienteId, 
            @RequestParam String descripcion) {
        sofkianoService.desasociarCliente(sofkianoId, clienteId, descripcion);
        return ResponseEntity.ok().build();
    }
    
}
