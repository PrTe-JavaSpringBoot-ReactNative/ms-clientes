package com.example.clientes.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Clase base que representa a una Persona.
 * 
 * Se implementa como @MappedSuperclass en lugar de @Entity porque:
 * - No necesita su propia tabla en la BD
 * - Evita complejidad de herencia (JOINED, SINGLE_TABLE)
 * - Sus campos son copiados directamente a la tabla de Cliente
 * 
 * Atributos:
 * - nombre: Nombre completo de la persona
 * - genero: Género de la persona
 * - edad: Edad en años
 * - identificacion: Número de identificación (cédula, pasaporte, etc.)
 * - direccion: Dirección física de residencia
 * - telefono: Número de teléfono de contacto
 */
@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class Persona {

    private String nombre;
    private String genero;
    private Integer edad;
    private String identificacion;
    private String direccion;
    private String telefono;
}
