package com.example.clientes;

import com.example.clientes.dto.ClienteRequestDTO;
import com.example.clientes.repository.ClienteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class ClientesIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClienteRepository clienteRepository;

    @BeforeEach
    public void setUp() {
        clienteRepository.deleteAll();
    }

    @Test
    public void testCrearClienteExitosamente() throws Exception {
        ClienteRequestDTO nuevoCliente = ClienteRequestDTO.builder()
                .nombre("Jose Lema")
                .genero("M")
                .edad(30)
                .identificacion("1234567890")
                .direccion("Otavalo sn y principal")
                .telefono("098254785")
                .contrasena("password123")
                .estado("ACTIVO")
                .build();

        mockMvc.perform(post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoCliente)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clienteId").exists())
                .andExpect(jsonPath("$.nombre", is("Jose Lema")))
                .andExpect(jsonPath("$.genero", is("M")))
                .andExpect(jsonPath("$.edad", is(30)))
                .andExpect(jsonPath("$.identificacion", is("1234567890")))
                .andExpect(jsonPath("$.direccion", is("Otavalo sn y principal")))
                .andExpect(jsonPath("$.telefono", is("098254785")))
                .andExpect(jsonPath("$.estado", is("ACTIVO")));

        assert clienteRepository.count() == 1;
    }
}
