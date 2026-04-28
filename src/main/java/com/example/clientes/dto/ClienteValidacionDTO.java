package com.example.clientes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO usado como mensaje en RabbitMQ para la validación de clienteId.
 *
 * Este DTO es idéntico al de ms-cuentas pero en el package de ms-clientes.
 * Como los microservicios no comparten código, cada uno define su propia copia.
 * Lo que sí debe ser idéntico es la estructura de campos, porque RabbitMQ
 * serializa/deserializa el mismo JSON en ambos extremos.
 *
 * ms-clientes solo lee: clienteId, cuentaId, numeroCuenta
 * ms-clientes solo escribe: clienteExiste (true o false)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteValidacionDTO {

    private Long clienteId;
    private Long cuentaId;
    private String numeroCuenta;
    private Boolean clienteExiste;
}
