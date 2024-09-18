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

import com.prueba.sofka.domain.model.entity.Cliente;
import com.prueba.sofka.infrastructure.persistence.repository.ClienteRepository;

class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        Cliente cliente1 = new Cliente();
        Cliente cliente2 = new Cliente();
        List<Cliente> clientes = Arrays.asList(cliente1, cliente2);

        when(clienteRepository.findAll()).thenReturn(clientes);

        List<Cliente> result = clienteService.findAll();

        assertEquals(2, result.size());
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void testSave() {
        Cliente cliente = new Cliente();
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        Cliente result = clienteService.save(cliente);

        assertNotNull(result);
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    void testFindById() {
        Cliente cliente = new Cliente();
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        Cliente result = clienteService.findById(1L);

        assertNotNull(result);
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        Cliente result = clienteService.findById(1L);

        assertNull(result);
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteById() {
        doNothing().when(clienteRepository).deleteById(1L);

        clienteService.deleteById(1L);

        verify(clienteRepository, times(1)).deleteById(1L);
    }
}