package com.example.clientes.exception;

/**
 * Excepción lanzada cuando se intenta crear un Cliente con una identificación que ya existe.
 */
public class ClienteYaExisteException extends RuntimeException {

    public ClienteYaExisteException(String message) {
        super(message);
    }

    public ClienteYaExisteException(String message, Throwable cause) {
        super(message, cause);
    }
}
