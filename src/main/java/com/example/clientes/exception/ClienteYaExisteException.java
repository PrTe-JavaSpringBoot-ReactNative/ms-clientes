package com.example.clientes.exception;


public class ClienteYaExisteException extends RuntimeException {

    public ClienteYaExisteException(String message) {
        super(message);
    }

    public ClienteYaExisteException(String message, Throwable cause) {
        super(message, cause);
    }
}
