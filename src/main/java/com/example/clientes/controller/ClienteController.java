package com.example.clientes.controller;

import com.example.clientes.dto.ClienteRequestDTO;
import com.example.clientes.dto.ClienteResponseDTO;
import com.example.clientes.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para el dominio Cliente.
 * 
 * Expone los siguientes endpoints:
 * - GET    /api/clientes              → Obtener todos los clientes
 * - GET    /api/clientes/{id}         → Obtener cliente por ID
 * - POST   /api/clientes              → Crear nuevo cliente
 * - PUT    /api/clientes/{id}         → Actualizar cliente
 * - DELETE /api/clientes/{id}         → Eliminar cliente
 * 
 * Utiliza DTOs para desacoplar la API interna de la representación externa.
 * Las validaciones se aplican a través de @Valid en los parámetros de solicitud.
 */
@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    /**
     * GET /api/clientes
     * Obtiene la lista de todos los clientes registrados.
     * 
     * @return Lista de ClienteResponseDTO
     */
    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> obtenerTodos() {
        List<ClienteResponseDTO> clientes = clienteService.obtenerTodosLosClientes();
        return ResponseEntity.ok(clientes);
    }

    /**
     * GET /api/clientes/{id}
     * Obtiene un cliente específico por su ID.
     * 
     * @param id ID del cliente a obtener
     * @return ClienteResponseDTO del cliente encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> obtenerPorId(@PathVariable Long id) {
        ClienteResponseDTO cliente = clienteService.obtenerClientePorId(id);
        return ResponseEntity.ok(cliente);
    }

    /**
     * POST /api/clientes
     * Crea un nuevo cliente en el sistema.
     * 
     * @param clienteRequestDTO Datos del cliente a crear (validado con @Valid)
     * @return ClienteResponseDTO del cliente creado con status 201 CREATED
     */
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> crearCliente(
            @Valid @RequestBody ClienteRequestDTO clienteRequestDTO) {
        ClienteResponseDTO clienteCreado = clienteService.crearCliente(clienteRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteCreado);
    }

    /**
     * PUT /api/clientes/{id}
     * Actualiza los datos de un cliente existente.
     * 
     * @param id ID del cliente a actualizar
     * @param clienteRequestDTO Nuevos datos del cliente (validado con @Valid)
     * @return ClienteResponseDTO del cliente actualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> actualizarCliente(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequestDTO clienteRequestDTO) {
        ClienteResponseDTO clienteActualizado = clienteService.actualizarCliente(id, clienteRequestDTO);
        return ResponseEntity.ok(clienteActualizado);
    }

    /**
     * DELETE /api/clientes/{id}
     * Elimina un cliente del sistema.
     * 
     * @param id ID del cliente a eliminar
     * @return Status 204 NO_CONTENT si la eliminación fue exitosa
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        clienteService.eliminarCliente(id);
        return ResponseEntity.noContent().build();
    }
}
