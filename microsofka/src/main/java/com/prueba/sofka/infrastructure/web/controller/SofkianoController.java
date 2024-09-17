package com.prueba.sofka.infrastructure.web.controller;

//import com.prueba.sofka.application.service.IExcelService;
import com.prueba.sofka.application.service.ISofkianoService;
import com.prueba.sofka.domain.model.entity.Sofkiano;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/sofkianos")
public class SofkianoController {

    @Autowired
    private ISofkianoService sofkianoService;

    /*@Autowired
    private IExcelService excelService;
    */
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
        sofkianoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /*@PostMapping("/upload")
    public ResponseEntity<String> uploadSofkianos(@RequestParam("file") MultipartFile file) {
        try {
            excelService.uploadSofkianos(file);
            return ResponseEntity.ok("Carga exitosa!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al cargar el archivo: " + e.getMessage());
        }
    }*/
}
