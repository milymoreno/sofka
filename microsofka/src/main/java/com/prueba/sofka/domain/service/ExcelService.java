package com.prueba.sofka.domain.service;

import com.prueba.sofka.domain.model.entity.Sofkiano;
import com.prueba.sofka.domain.service.SofkianoService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelService {

    @Autowired
    private SofkianoService sofkianoService;

    public void uploadSofkianos(MultipartFile file) throws IOException {
        List<Sofkiano> sofkianos = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                Sofkiano sofkiano = new Sofkiano();
                sofkiano.setNombres(row.getCell(0).getStringCellValue());
                // Configura los demás atributos de Sofkiano según las columnas del archivo
                sofkianos.add(sofkiano);
            }
        }
        sofkianoService.saveAll(sofkianos);
    }
}
