package com.prueba.sofka.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.prueba.sofka.domain.model.entity.Cliente;
import com.prueba.sofka.domain.model.entity.ExperienciaCliente;
import com.prueba.sofka.domain.model.entity.Sofkiano;
import com.prueba.sofka.infrastructure.persistence.repository.ClienteRepository;
import com.prueba.sofka.infrastructure.persistence.repository.ExperienciaClienteRepository;
import com.prueba.sofka.infrastructure.persistence.repository.SofkianoRepository;


public class SofkianoServiceTest {

    @Mock
    private SofkianoRepository sofkianoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ExperienciaClienteRepository experienciaClienteRepository;

    @Mock
    private SofkianoEventProducerRoute sofkianoEventProducer;

    @InjectMocks
    private SofkianoService sofkianoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAsociarCliente_Success() {
        Long sofkianoId = 1L;
        Long clienteId = 1L;
        String rol = "Developer";

        Sofkiano sofkiano = new Sofkiano();
        sofkiano.setId(sofkianoId);
        sofkiano.setNombres("John Doe");

        Cliente cliente = new Cliente();
        cliente.setId(clienteId);
        cliente.setNombre("Acme Corp");

        when(sofkianoRepository.findById(sofkianoId)).thenReturn(Optional.of(sofkiano));
        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
        when(experienciaClienteRepository.findBySofkianoIdAndClienteIdAndFechaFinIsNull(sofkianoId, clienteId)).thenReturn(Optional.empty());
        when(experienciaClienteRepository.save(any(ExperienciaCliente.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ExperienciaCliente result = sofkianoService.asociarCliente(sofkianoId, clienteId, rol);

        assertNotNull(result);
        assertEquals(sofkiano, result.getSofkiano());
        assertEquals(cliente, result.getCliente());
        assertEquals(rol, result.getRol());

        verify(sofkianoRepository).findById(sofkianoId);
        verify(clienteRepository).findById(clienteId);
        verify(experienciaClienteRepository).findBySofkianoIdAndClienteIdAndFechaFinIsNull(sofkianoId, clienteId);
        verify(experienciaClienteRepository).save(any(ExperienciaCliente.class));
        verify(sofkianoEventProducer).sendEvent(anyMap());
    }

    @Test
    public void testAsociarCliente_SofkianoNotFound() {
        Long sofkianoId = 1L;
        Long clienteId = 1L;
        String rol = "Developer";

        when(sofkianoRepository.findById(sofkianoId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            sofkianoService.asociarCliente(sofkianoId, clienteId, rol);
        });

        assertEquals("Sofkiano no encontrado", exception.getMessage());

        verify(sofkianoRepository).findById(sofkianoId);
        verify(clienteRepository, never()).findById(clienteId);
        verify(experienciaClienteRepository, never()).findBySofkianoIdAndClienteIdAndFechaFinIsNull(sofkianoId, clienteId);
        verify(experienciaClienteRepository, never()).save(any(ExperienciaCliente.class));
        verify(sofkianoEventProducer, never()).sendEvent(anyMap());
    }

    @Test
    public void testAsociarCliente_ClienteNotFound() {
        Long sofkianoId = 1L;
        Long clienteId = 1L;
        String rol = "Developer";

        Sofkiano sofkiano = new Sofkiano();
        sofkiano.setId(sofkianoId);
        sofkiano.setNombres("John Doe");

        when(sofkianoRepository.findById(sofkianoId)).thenReturn(Optional.of(sofkiano));
        when(clienteRepository.findById(clienteId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            sofkianoService.asociarCliente(sofkianoId, clienteId, rol);
        });

        assertEquals("Cliente no encontrado", exception.getMessage());

        verify(sofkianoRepository).findById(sofkianoId);
        verify(clienteRepository).findById(clienteId);
        verify(experienciaClienteRepository, never()).findBySofkianoIdAndClienteIdAndFechaFinIsNull(sofkianoId, clienteId);
        verify(experienciaClienteRepository, never()).save(any(ExperienciaCliente.class));
        verify(sofkianoEventProducer, never()).sendEvent(anyMap());
    }

    @Test
    public void testAsociarCliente_ExperienciaExistente() {
        Long sofkianoId = 1L;
        Long clienteId = 1L;
        String rol = "Developer";

        Sofkiano sofkiano = new Sofkiano();
        sofkiano.setId(sofkianoId);
        sofkiano.setNombres("John Doe");

        Cliente cliente = new Cliente();
        cliente.setId(clienteId);
        cliente.setNombre("Acme Corp");

        ExperienciaCliente experienciaExistente = new ExperienciaCliente();
        experienciaExistente.setSofkiano(sofkiano);
        experienciaExistente.setCliente(cliente);
        experienciaExistente.setRol("Tester");

        when(sofkianoRepository.findById(sofkianoId)).thenReturn(Optional.of(sofkiano));
        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
        when(experienciaClienteRepository.findBySofkianoIdAndClienteIdAndFechaFinIsNull(sofkianoId, clienteId)).thenReturn(Optional.of(experienciaExistente));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            sofkianoService.asociarCliente(sofkianoId, clienteId, rol);
        });

        String expectedMessage = String.format("El sofkiano %s ya está asociado al cliente %s con el rol: %s.", 
                                                sofkiano.getNombres(), 
                                                cliente.getNombre(), 
                                                experienciaExistente.getRol());
        assertEquals(expectedMessage, exception.getMessage());

        verify(sofkianoRepository).findById(sofkianoId);
        verify(clienteRepository).findById(clienteId);
        verify(experienciaClienteRepository).findBySofkianoIdAndClienteIdAndFechaFinIsNull(sofkianoId, clienteId);
        verify(experienciaClienteRepository, never()).save(any(ExperienciaCliente.class));
        verify(sofkianoEventProducer, never()).sendEvent(anyMap());
    }
}