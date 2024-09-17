package com.prueba.sofka.infrastructure.web.controller;

import com.prueba.sofka.domain.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/excel")
public class ExcelController {

    @Autowired
    private ExcelService excelService;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            excelService.uploadSofkianos(file);
            return "Cargue exitoso!";
        } catch (Exception e) {
            return "Error al cargar el archivo: " + e.getMessage();
        }
    }
}

