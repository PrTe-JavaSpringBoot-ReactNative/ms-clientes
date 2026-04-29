package com.example.clientes.service;

import com.example.clientes.dto.ClienteRequestDTO;
import com.example.clientes.dto.ClienteResponseDTO;
import com.example.clientes.entity.Cliente;
import com.example.clientes.exception.ClienteNotFoundException;
import com.example.clientes.exception.ClienteYaExisteException;
import com.example.clientes.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClienteService {

    private final ClienteRepository clienteRepository;


    public List<ClienteResponseDTO> obtenerTodosLosClientes() {
        log.info("Obteniendo todos los clientes");
        return clienteRepository.findAll().stream()
                .map(this::entityToResponseDTO)
                .collect(Collectors.toList());
    }

    public ClienteResponseDTO obtenerClientePorId(Long clienteId) {
        log.info("Obteniendo cliente con ID: {}", clienteId);
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> {
                    log.warn("Cliente con ID {} no encontrado", clienteId);
                    return new ClienteNotFoundException(clienteId);
                });
        return entityToResponseDTO(cliente);
    }

    @Transactional
    public ClienteResponseDTO crearCliente(ClienteRequestDTO clienteRequestDTO) {
        log.info("Creando nuevo cliente con identificación: {}", clienteRequestDTO.getIdentificacion());

        // Validar que no exista cliente con esa identificación
        if (clienteRepository.findByIdentificacion(clienteRequestDTO.getIdentificacion()).isPresent()) {
            log.warn("Intento de crear cliente con identificación duplicada: {}", 
                    clienteRequestDTO.getIdentificacion());
            throw new ClienteYaExisteException("Cliente con identificación " + clienteRequestDTO.getIdentificacion() + " ya existe");
        }

        Cliente cliente = requestDTOToEntity(clienteRequestDTO);
        Cliente clienteGuardado = clienteRepository.save(cliente);

        log.info("Cliente creado exitosamente con ID: {}", clienteGuardado.getClienteId());
        return entityToResponseDTO(clienteGuardado);
    }

    @Transactional
    public ClienteResponseDTO actualizarCliente(Long clienteId, ClienteRequestDTO clienteRequestDTO) {
        log.info("Actualizando cliente con ID: {}", clienteId);

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> {
                    log.warn("Cliente con ID {} no encontrado para actualizar", clienteId);
                    return new ClienteNotFoundException(clienteId);
                });

        cliente.setNombre(clienteRequestDTO.getNombre());
        cliente.setGenero(clienteRequestDTO.getGenero());
        cliente.setEdad(clienteRequestDTO.getEdad());
        cliente.setDireccion(clienteRequestDTO.getDireccion());
        cliente.setTelefono(clienteRequestDTO.getTelefono());

        cliente.setContrasena(clienteRequestDTO.getContrasena());
        cliente.setEstado(clienteRequestDTO.getEstado());

        Cliente clienteActualizado = clienteRepository.save(cliente);

        log.info("Cliente con ID {} actualizado exitosamente", clienteId);
        return entityToResponseDTO(clienteActualizado);
    }


    @Transactional
    public void eliminarCliente(Long clienteId) {
        log.info("Eliminando cliente con ID: {}", clienteId);

        if (!clienteRepository.existsById(clienteId)) {
            log.warn("Cliente con ID {} no encontrado para eliminar", clienteId);
            throw new ClienteNotFoundException(clienteId);
        }

        clienteRepository.deleteById(clienteId);
        log.info("Cliente con ID {} eliminado exitosamente", clienteId);
    }

    private ClienteResponseDTO entityToResponseDTO(Cliente cliente) {
        return ClienteResponseDTO.builder()
                .clienteId(cliente.getClienteId())
                .nombre(cliente.getNombre())
                .genero(cliente.getGenero())
                .edad(cliente.getEdad())
                .identificacion(cliente.getIdentificacion())
                .direccion(cliente.getDireccion())
                .telefono(cliente.getTelefono())
                .estado(cliente.getEstado())
                .build();
    }


    private Cliente requestDTOToEntity(ClienteRequestDTO requestDTO) {
        return Cliente.builder()
                .nombre(requestDTO.getNombre())
                .genero(requestDTO.getGenero())
                .edad(requestDTO.getEdad())
                .identificacion(requestDTO.getIdentificacion())
                .direccion(requestDTO.getDireccion())
                .telefono(requestDTO.getTelefono())
                .contrasena(requestDTO.getContrasena())
                .estado(requestDTO.getEstado())
                .build();
    }
}
