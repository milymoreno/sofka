package com.prueba.sofka.service;

import com.prueba.sofka.domain.model.entity.Sofkiano;
import com.prueba.sofka.infrastructure.persistence.repository.SofkianoRepository;
import com.prueba.sofka.domain.service.SofkianoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class SofkianoServiceTest {

    @Mock
    private SofkianoRepository sofkianoRepository;

    @InjectMocks
    private SofkianoService sofkianoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        // Configurar el comportamiento del mock
        Sofkiano sofkiano1 = new Sofkiano();
        Sofkiano sofkiano2 = new Sofkiano();
        List<Sofkiano> expectedList = Arrays.asList(sofkiano1, sofkiano2);
        when(sofkianoRepository.findAll()).thenReturn(expectedList);

        // Llamar al método a probar
        List<Sofkiano> result = sofkianoService.findAll();

        // Verificar el resultado
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedList, result);
        verify(sofkianoRepository).findAll();
    }

    @Test
    public void testSave() {
        // Configurar el comportamiento del mock
        Sofkiano sofkiano = new Sofkiano();
        when(sofkianoRepository.save(sofkiano)).thenReturn(sofkiano);

        // Llamar al método a probar
        Sofkiano result = sofkianoService.save(sofkiano);

        // Verificar el resultado
        assertNotNull(result);
        assertEquals(sofkiano, result);
        verify(sofkianoRepository).save(sofkiano);
    }

    @Test
    public void testFindById() {
        // Configurar el comportamiento del mock
        Sofkiano sofkiano = new Sofkiano();
        when(sofkianoRepository.findById(1L)).thenReturn(Optional.of(sofkiano));

        // Llamar al método a probar
        Sofkiano result = sofkianoService.findById(1L);

        // Verificar el resultado
        assertNotNull(result);
        assertEquals(sofkiano, result);
        verify(sofkianoRepository).findById(1L);
    }

    @Test
    public void testFindById_NotFound() {
        // Configurar el comportamiento del mock
        when(sofkianoRepository.findById(1L)).thenReturn(Optional.empty());

        // Llamar al método a probar
        Sofkiano result = sofkianoService.findById(1L);

        // Verificar el resultado
        assertNull(result);
        verify(sofkianoRepository).findById(1L);
    }
}