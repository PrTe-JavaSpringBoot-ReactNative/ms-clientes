package com.example.clientes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Manejador global de excepciones para ms-clientes.
 *
 * Centraliza el tratamiento de errores y retorna respuestas JSON estandarizadas.
 * Maneja las siguientes situaciones:
 *   - ClienteNotFoundException   → 404
 *   - ClienteYaExisteException   → 409
 *   - MethodArgumentNotValidException → 400 (validaciones de @Valid)
 *   - Exception genérica          → 500
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja la excepción ClienteNotFoundException.
     * Se lanza cuando se intenta acceder a un cliente inexistente.
     * 
     * Retorna 404 Not Found.
     */
    @ExceptionHandler(ClienteNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleClienteNotFound(ClienteNotFoundException ex) {
        Map<String, Object> body = buildErrorResponse(
                HttpStatus.NOT_FOUND,
                "Recurso no encontrado",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    /**
     * Maneja la excepción ClienteYaExisteException.
     * Se lanza cuando se intenta crear un cliente con una identificación duplicada.
     * 
     * Retorna 409 Conflict.
     */
    @ExceptionHandler(ClienteYaExisteException.class)
    public ResponseEntity<Map<String, Object>> handleClienteYaExiste(ClienteYaExisteException ex) {
        Map<String, Object> body = buildErrorResponse(
                HttpStatus.CONFLICT,
                "Conflicto - Recurso duplicado",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    /**
     * Maneja la excepción MethodArgumentNotValidException.
     * Se lanza cuando las validaciones de @Valid fallan.
     * 
     * Retorna 400 Bad Request con los detalles de los campos inválidos.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex) {
        String errores = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        Map<String, Object> body = buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Validación fallida",
                errores
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /**
     * Maneja cualquier otra excepción no controlada.
     * 
     * Retorna 500 Internal Server Error.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> body = buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                ex.getMessage() != null ? ex.getMessage() : "Error desconocido"
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    /**
     * Constructor auxiliar para construir una respuesta de error estandarizada.
     */
    private Map<String, Object> buildErrorResponse(
            HttpStatus status,
            String error,
            String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        return body;
    }
}
