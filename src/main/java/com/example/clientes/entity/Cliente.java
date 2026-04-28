package com.example.clientes.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Entidad Cliente que hereda de Persona.
 * 
 * Representa un cliente del sistema que extiende los atributos de una persona
 * con información de acceso y estado de la cuenta.
 * 
 * Atributos adicionales:
 * - clienteId: Identificador único del cliente (clave primaria)
 * - contrasena: Contraseña encriptada para acceso al sistema
 * - estado: Estado del cliente (ACTIVO, INACTIVO, SUSPENDIDO, etc.)
 */
@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Cliente extends Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clienteId;

    @Column(nullable = false, length = 255)
    private String contrasena;

    @Column(nullable = false, length = 50)
    private String estado;
}
