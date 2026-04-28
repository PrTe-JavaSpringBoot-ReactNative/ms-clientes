package com.example.clientes.repository;

import com.example.clientes.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para la entidad Cliente.
 * 
 * Extiende JpaRepository para proporcionar automáticamente operaciones CRUD
 * y permite agregar consultas personalizadas específicas del dominio.
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    /**
     * Busca un cliente por su identificación.
     * 
     * @param identificacion Número de identificación del cliente
     * @return Optional con el cliente si existe, vacío en caso contrario
     */
    Optional<Cliente> findByIdentificacion(String identificacion);
}
