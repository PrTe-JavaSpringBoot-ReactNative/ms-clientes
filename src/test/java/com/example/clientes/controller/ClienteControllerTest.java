package com.example.clientes.controller;

import com.example.clientes.dto.ClienteRequestDTO;
import com.example.clientes.dto.ClienteResponseDTO;
import com.example.clientes.service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ClienteController.class)
@ExtendWith(MockitoExtension.class)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @Autowired
    private ObjectMapper objectMapper;

    private ClienteResponseDTO clienteResponse;
    private ClienteRequestDTO clienteRequest;

    @BeforeEach
    public void setUp() {
        clienteResponse = ClienteResponseDTO.builder()
                .clienteId(1L)
                .nombre("Juan Pérez")
                .genero("M")
                .edad(30)
                .identificacion("1234567890")
                .direccion("Calle 123, Apartamento 4B")
                .telefono("1234567890")
                .estado("ACTIVO")
                .build();

        clienteRequest = ClienteRequestDTO.builder()
                .nombre("Juan Pérez")
                .genero("M")
                .edad(30)
                .identificacion("1234567890")
                .direccion("Calle 123, Apartamento 4B")
                .telefono("1234567890")
                .contrasena("password123")
                .estado("ACTIVO")
                .build();
    }

    @Test
    public void testCrearClienteExitosamente() throws Exception {
        when(clienteService.crearCliente(any(ClienteRequestDTO.class)))
                .thenReturn(clienteResponse);

        mockMvc.perform(post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clienteId", is(1)))
                .andExpect(jsonPath("$.nombre", is("Juan Pérez")))
                .andExpect(jsonPath("$.genero", is("M")))
                .andExpect(jsonPath("$.edad", is(30)))
                .andExpect(jsonPath("$.identificacion", is("1234567890")))
                .andExpect(jsonPath("$.estado", is("ACTIVO")));

        verify(clienteService, times(1)).crearCliente(any(ClienteRequestDTO.class));
    }

    @Test
    public void testCrearClienteConValidacionFallida() throws Exception {
        ClienteRequestDTO clienteInvalido = ClienteRequestDTO.builder()
                .nombre("") 
                .genero("M")
                .edad(30)
                .identificacion("1234567890")
                .direccion("Calle 123")
                .telefono("1234567890")
                .contrasena("password123")
                .estado("ACTIVO")
                .build();

        mockMvc.perform(post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", containsString("Validación")));

        verify(clienteService, never()).crearCliente(any(ClienteRequestDTO.class));
    }
}
