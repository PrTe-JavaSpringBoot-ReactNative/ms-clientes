package com.example.clientes.exception;

/**
 * Excepción lanzada cuando se intenta buscar un Cliente que no existe en la BD.
 */
public class ClienteNotFoundException extends RuntimeException {

    public ClienteNotFoundException(String message) {
        super(message);
    }

    public ClienteNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClienteNotFoundException(Long clienteId) {
        super("Cliente con ID " + clienteId + " no encontrado");
    }
}
