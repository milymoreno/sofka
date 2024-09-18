package com.prueba.sofka.infrastructure.web.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.prueba.sofka.domain.service.ExcelService;


public class ExcelControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ExcelService excelService;

    @InjectMocks
    private ExcelController excelController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(excelController).build();
    }

    @Test
    public void testUploadFileSuccess() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.xlsx", MediaType.MULTIPART_FORM_DATA_VALUE, "test content".getBytes());

        mockMvc.perform(multipart("/api/excel/upload").file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("Cargue exitoso!"));

        verify(excelService).uploadSofkianos(file);
    }

    @Test
    public void testUploadFileFailure() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.xlsx", MediaType.MULTIPART_FORM_DATA_VALUE, "test content".getBytes());
        doThrow(new RuntimeException("Test Exception")).when(excelService).uploadSofkianos(file);

        mockMvc.perform(multipart("/api/excel/upload").file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("Error al cargar el archivo: Test Exception"));
    }
}