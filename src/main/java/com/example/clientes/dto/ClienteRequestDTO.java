package com.example.clientes.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteRequestDTO {

    @NotBlank(message = "El nombre es requerido")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "El género es requerido")
    private String genero;

    @NotNull(message = "La edad es requerida")
    @Positive(message = "La edad debe ser un número positivo")
    private Integer edad;

    @NotBlank(message = "La identificación es requerida")
    @Size(min = 6, max = 20, message = "La identificación debe tener entre 6 y 20 caracteres")
    private String identificacion;

    @NotBlank(message = "La dirección es requerida")
    @Size(min = 5, max = 255, message = "La dirección debe tener entre 5 y 255 caracteres")
    private String direccion;

    @NotBlank(message = "El teléfono es requerido")
    @Size(min = 7, max = 15, message = "El teléfono debe tener entre 7 y 15 caracteres")
    private String telefono;

    @NotBlank(message = "La contraseña es requerida")
    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
    private String contrasena;

    @NotBlank(message = "El estado es requerido")
    private String estado;
}
