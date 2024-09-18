package com.prueba.sofka.infrastructure.web.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.prueba.sofka.application.service.IClienteService;
import com.prueba.sofka.domain.model.entity.Cliente;


class ClienteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(clienteController).build();
    }

    @Test
    void getAllClientes() throws Exception {
        List<Cliente> clientes = Arrays.asList(new Cliente(), new Cliente());
        when(clienteService.findAll()).thenReturn(clientes);

        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(clientes.size()));

        verify(clienteService, times(1)).findAll();
    }

    @Test
    void createCliente() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        when(clienteService.save(any(Cliente.class))).thenReturn(cliente);

        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Test Cliente\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cliente.getId()));

        verify(clienteService, times(1)).save(any(Cliente.class));
    }

    @Test
    void getClienteById() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        when(clienteService.findById(anyLong())).thenReturn(cliente);

        mockMvc.perform(get("/api/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cliente.getId()));

        verify(clienteService, times(1)).findById(anyLong());
    }

    @Test
    void getClienteById_NotFound() throws Exception {
        when(clienteService.findById(anyLong())).thenReturn(null);

        mockMvc.perform(get("/api/clientes/1"))
                .andExpect(status().isNotFound());

        verify(clienteService, times(1)).findById(anyLong());
    }

    @Test
    void updateCliente() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        when(clienteService.save(any(Cliente.class))).thenReturn(cliente);

        mockMvc.perform(put("/api/clientes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated Cliente\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cliente.getId()));

        verify(clienteService, times(1)).save(any(Cliente.class));
    }

    @Test
    void deleteCliente() throws Exception {
        doNothing().when(clienteService).deleteById(anyLong());

        mockMvc.perform(delete("/api/clientes/1"))
                .andExpect(status().isNoContent());

        verify(clienteService, times(1)).deleteById(anyLong());
    }
}



