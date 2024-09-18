package com.prueba.sofka.domain.service;

import com.prueba.sofka.domain.model.entity.Sofkiano;
import com.prueba.sofka.domain.model.enums.TipoIdentificacion;
import com.prueba.sofka.domain.service.SofkianoService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.ZoneId;
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
            if (row.getRowNum() == 0) {// la primera fila contiene los encabezados
                continue;
            }
            
            Sofkiano sofkiano = new Sofkiano();
            sofkiano.setTipoIdentificacion(TipoIdentificacion.valueOf(row.getCell(1).getStringCellValue().toUpperCase()));
            sofkiano.setNumeroIdentificacion(row.getCell(1).getStringCellValue());
            sofkiano.setNombres(row.getCell(2).getStringCellValue());
            sofkiano.setApellidos(row.getCell(3).getStringCellValue());
            sofkiano.setDireccion(row.getCell(4).getStringCellValue());
            sofkiano.setActivo(row.getCell(5).getBooleanCellValue());
            sofkiano.setEmail(row.getCell(6).getStringCellValue());
            sofkiano.setPerfil(row.getCell(7).getStringCellValue());
            sofkiano.setCantidadAniosExperiencia((int) row.getCell(8).getNumericCellValue());            
            // Para fechas, convierte la celda de tipo Date
            if (row.getCell(9).getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(row.getCell(9))) {
                    sofkiano.setFechaNacimiento(row.getCell(9).getDateCellValue().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDateTime());
            }
            
            if (row.getCell(10).getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(row.getCell(10))) {
                sofkiano.setFechaCreacion(row.getCell(10).getDateCellValue().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime());
            }
            
            if (row.getCell(11).getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(row.getCell(11))) {
                sofkiano.setFechaModificacion(row.getCell(11).getDateCellValue().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime());
            }

            sofkianos.add(sofkiano);
        }
    }

    sofkianoService.saveAll(sofkianos);  
}

}
