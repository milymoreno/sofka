package com.prueba.sofka.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.prueba.sofka.domain.model.entity.ExperienciaCliente;
import com.prueba.sofka.infrastructure.persistence.repository.ExperienciaClienteRepository;


class ExperienciaClienteServiceTest {

    @Mock
    private ExperienciaClienteRepository experienciaClienteRepository;

    @InjectMocks
    private ExperienciaClienteService experienciaClienteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        ExperienciaCliente experiencia1 = new ExperienciaCliente();
        ExperienciaCliente experiencia2 = new ExperienciaCliente();
        List<ExperienciaCliente> experiencias = Arrays.asList(experiencia1, experiencia2);

        when(experienciaClienteRepository.findAll()).thenReturn(experiencias);

        List<ExperienciaCliente> result = experienciaClienteService.findAll();

        assertEquals(2, result.size());
        verify(experienciaClienteRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        ExperienciaCliente experiencia = new ExperienciaCliente();
        when(experienciaClienteRepository.findById(1L)).thenReturn(Optional.of(experiencia));

        ExperienciaCliente result = experienciaClienteService.findById(1L);

        assertNotNull(result);
        verify(experienciaClienteRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(experienciaClienteRepository.findById(1L)).thenReturn(Optional.empty());

        ExperienciaCliente result = experienciaClienteService.findById(1L);

        assertNull(result);
        verify(experienciaClienteRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteById() {
        doNothing().when(experienciaClienteRepository).deleteById(1L);

        experienciaClienteService.deleteById(1L);

        verify(experienciaClienteRepository, times(1)).deleteById(1L);
    }
}