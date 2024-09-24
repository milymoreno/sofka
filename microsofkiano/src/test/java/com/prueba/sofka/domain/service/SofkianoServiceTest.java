package com.prueba.sofka.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

//import com.prueba.sofka.application.components.SofkianoEventProducer;
import org.apache.camel.ProducerTemplate;
import com.prueba.sofka.domain.service.SofkianoEventProducerRoute;

import com.prueba.sofka.domain.model.entity.Cliente;
import com.prueba.sofka.domain.model.entity.ExperienciaCliente;
import com.prueba.sofka.domain.model.entity.Sofkiano;
import com.prueba.sofka.infrastructure.persistence.repository.ClienteRepository;
import com.prueba.sofka.infrastructure.persistence.repository.ExperienciaClienteRepository;
import com.prueba.sofka.infrastructure.persistence.repository.SofkianoRepository;

class SofkianoServiceTest {

    @Mock
    private SofkianoRepository sofkianoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private SofkianoEventProducerRoute sofkianoEventProducer;

    @Mock
    private ExperienciaClienteRepository experienciaClienteRepository;

    @InjectMocks
    private SofkianoService sofkianoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        Sofkiano sofkiano1 = new Sofkiano();
        Sofkiano sofkiano2 = new Sofkiano();
        List<Sofkiano> sofkianos = Arrays.asList(sofkiano1, sofkiano2);

        when(sofkianoRepository.findAll()).thenReturn(sofkianos);

        List<Sofkiano> result = sofkianoService.findAll();

        assertEquals(2, result.size());
        verify(sofkianoRepository, times(1)).findAll();
    }

    @Test
    void testSave() {
        Sofkiano sofkiano = new Sofkiano();

        when(sofkianoRepository.save(sofkiano)).thenReturn(sofkiano);

        Sofkiano result = sofkianoService.save(sofkiano);

        assertNotNull(result);
        verify(sofkianoRepository, times(1)).save(sofkiano);
    }

    @Test
    void testFindById() {
        Sofkiano sofkiano = new Sofkiano();
        Long id = 1L;

        when(sofkianoRepository.findById(id)).thenReturn(Optional.of(sofkiano));

        Sofkiano result = sofkianoService.findById(id);

        assertNotNull(result);
        verify(sofkianoRepository, times(1)).findById(id);
    }

    @Test
    void testDeleteById() {
        Long id = 1L;

        doNothing().when(sofkianoRepository).deleteById(id);

        sofkianoService.deleteById(id);

        verify(sofkianoRepository, times(1)).deleteById(id);
    }

    @Test
    void testSaveAll() {
        Sofkiano sofkiano1 = new Sofkiano();
        Sofkiano sofkiano2 = new Sofkiano();
        List<Sofkiano> sofkianos = Arrays.asList(sofkiano1, sofkiano2);

        when(sofkianoRepository.saveAll(sofkianos)).thenReturn(sofkianos);

        sofkianoService.saveAll(sofkianos);

        verify(sofkianoRepository, times(1)).saveAll(sofkianos);
    }

    @Test
    void testAsociarCliente() {
        Long sofkianoId = 1L;
        Long clienteId = 1L;
        String rol = "CyberSecurity";
        Sofkiano sofkiano = new Sofkiano();
        sofkiano.setId(sofkianoId);
        sofkiano.setNombres("Ariana ");
        sofkiano.setApellidos("Mendoza Moreno");
        Cliente cliente = new Cliente();
        cliente.setId(clienteId);
        ExperienciaCliente experienciaCliente = new ExperienciaCliente();
    
        when(sofkianoRepository.findById(sofkianoId)).thenReturn(Optional.of(sofkiano));
        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
        when(experienciaClienteRepository.save(any(ExperienciaCliente.class))).thenReturn(experienciaCliente);
    
        ExperienciaCliente result = sofkianoService.asociarCliente(sofkianoId, clienteId, rol);
    
        assertNotNull(result);
        verify(sofkianoRepository, times(1)).findById(sofkianoId);
        verify(clienteRepository, times(1)).findById(clienteId);
        verify(experienciaClienteRepository, times(1)).save(any(ExperienciaCliente.class));
        //verify(sofkianoEventProducer, times(1)).sendSofkianoChangeEvent(
        verify(sofkianoService, times(1)).publicarEvento(
            eq(sofkianoId.toString()), 
                eq(sofkiano.getNombres()), 
                eq(clienteId.toString()), 
                eq(cliente.getNombre()), 
                //anyString(), 
                eq("INGRESO")
        );
    }

    @Test
        void testDesasociarCliente() {
            Long sofkianoId = 1L;
            Long clienteId = 1L;
            String descripcion = "Desasociación por finalización de proyecto";
            Sofkiano sofkiano = new Sofkiano();
            sofkiano.setId(sofkianoId);
            sofkiano.setNombres("Angel Dairam");
            sofkiano.setApellidos("Mendoza Moreno");
            Cliente cliente = new Cliente();
            cliente.setId(clienteId);
            cliente.setNombre("Cliente Test");
            ExperienciaCliente experienciaCliente = new ExperienciaCliente();
            experienciaCliente.setSofkiano(sofkiano);
            experienciaCliente.setCliente(cliente);
    
            when(sofkianoRepository.findById(sofkianoId)).thenReturn(Optional.of(sofkiano));
            when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
            when(experienciaClienteRepository.findBySofkianoIdAndClienteIdAndFechaFinIsNull(sofkianoId, clienteId))
                .thenReturn(Optional.of(experienciaCliente));
    
            sofkianoService.desasociarCliente(sofkianoId, clienteId, descripcion);
    
            assertNotNull(experienciaCliente.getFechaFin());
            assertEquals(descripcion, experienciaCliente.getDescripcion());
            verify(sofkianoRepository, times(1)).findById(sofkianoId);
            verify(clienteRepository, times(1)).findById(clienteId);
            verify(experienciaClienteRepository, times(1)).findBySofkianoIdAndClienteIdAndFechaFinIsNull(sofkianoId, clienteId);
            verify(experienciaClienteRepository, times(1)).save(experienciaCliente);
            //verify(sofkianoEventProducer, times(1)).sendSofkianoChangeEvent(
                verify(sofkianoService, times(1)).publicarEvento(
                eq(sofkianoId.toString()), 
                eq(sofkiano.getNombres()), 
                eq(clienteId.toString()), 
                eq(cliente.getNombre()), 
                //anyString(), 
                eq("EGRESO")
            );
        }
}