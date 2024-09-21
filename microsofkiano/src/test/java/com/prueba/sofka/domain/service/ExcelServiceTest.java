package com.prueba.sofka.domain.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import com.prueba.sofka.application.service.ISofkianoService;

public class ExcelServiceTest {

    @Mock
    private ISofkianoService sofkianoService;

    @InjectMocks
    private ExcelService excelService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUploadSofkianos() throws IOException {
        // Create a mock MultipartFile
        MultipartFile file = mock(MultipartFile.class);

        // Create a sample Excel file in memory
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XSSFWorkbook workbook = new XSSFWorkbook();
        var sheet = workbook.createSheet();
        var headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("TipoIdentificacion");
        headerRow.createCell(1).setCellValue("NumeroIdentificacion");
        headerRow.createCell(2).setCellValue("Nombres");
        headerRow.createCell(3).setCellValue("Apellidos");
        headerRow.createCell(4).setCellValue("Direccion");
        headerRow.createCell(5).setCellValue("Activo");
        headerRow.createCell(6).setCellValue("Email");
        headerRow.createCell(7).setCellValue("Perfil");
        headerRow.createCell(8).setCellValue("CantidadAniosExperiencia");
        headerRow.createCell(9).setCellValue("FechaNacimiento");
        headerRow.createCell(10).setCellValue("FechaCreacion");
        headerRow.createCell(11).setCellValue("FechaModificacion");

        var dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue("CC");
        dataRow.createCell(1).setCellValue("123456789");
        dataRow.createCell(2).setCellValue("John");
        dataRow.createCell(3).setCellValue("Doe");
        dataRow.createCell(4).setCellValue("123 Main St");
        dataRow.createCell(5).setCellValue(true);
        dataRow.createCell(6).setCellValue("john.doe@example.com");
        dataRow.createCell(7).setCellValue("Developer");
        dataRow.createCell(8).setCellValue(5);
        dataRow.createCell(9).setCellValue(LocalDateTime.now().toString());
        dataRow.createCell(10).setCellValue(LocalDateTime.now().toString());
        dataRow.createCell(11).setCellValue(LocalDateTime.now().toString());

        workbook.write(out);
        workbook.close();

        // Mock the behavior of the MultipartFile
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream(out.toByteArray()));

        // Call the method to test
        excelService.uploadSofkianos(file);

        // Verify that the saveAll method was called with the expected list of Sofkiano objects
        verify(sofkianoService, times(1)).saveAll(anyList());
    }
}