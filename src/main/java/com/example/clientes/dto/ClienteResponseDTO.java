package com.example.clientes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para retornar datos de Cliente en las respuestas HTTP.
 * 
 * Separa la representación interna de la entidad Cliente de lo que se expone
 * en la API, permitiendo controlar qué campos son visibles y modificables.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteResponseDTO {

    private Long clienteId;
    private String nombre;
    private String genero;
    private Integer edad;
    private String identificacion;
    private String direccion;
    private String telefono;
    private String estado;
    // Nota: contrasena NO se devuelve en la respuesta por seguridad
}
