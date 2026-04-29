package com.example.clientes.exception;


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
