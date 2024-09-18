package com.prueba.sofka.infrastructure.web.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.prueba.sofka.application.service.ISofkianoService;
import com.prueba.sofka.domain.model.entity.Sofkiano;


class SofkianoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ISofkianoService sofkianoService;

    @InjectMocks
    private SofkianoController sofkianoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(sofkianoController).build();
    }

    @Test
    void createSofkiano() throws Exception {
        Sofkiano sofkiano = new Sofkiano();
        sofkiano.setId(1L);
        sofkiano.setNombres("Mildred Maria");

        when(sofkianoService.save(any(Sofkiano.class))).thenReturn(sofkiano);

        mockMvc.perform(post("/api/sofkianos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Test Name\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"name\":\"Test Name\"}"));
    }
}