package com.example.clientes.messaging;

import com.example.clientes.config.RabbitMQConfig;
import com.example.clientes.dto.ClienteValidacionDTO;
import com.example.clientes.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * Consumer de ms-clientes.
 *
 * Responsabilidad: escuchar solicitudes de validación enviadas por ms-cuentas,
 * verificar si el clienteId existe en la base de datos propia,
 * y publicar la respuesta de vuelta para que ms-cuentas pueda actuar.
 *
 * Flujo:
 *   RabbitMQ [clientes.solicitudes.validacion]
 *       └──▶ ClienteValidacionConsumer.validarCliente()
 *                ├── busca clienteId en ClienteRepository
 *                ├── completa el campo clienteExiste (true/false)
 *                └──▶ RabbitMQ [cuentas.validacion.respuesta]
 *                         └──▶ ms-cuentas (ClienteValidacionRespuestaConsumer)
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ClienteValidacionConsumer {

    private final ClienteRepository clienteRepository;
    private final RabbitTemplate rabbitTemplate;

    /**
     * Escucha solicitudes de validación de ms-cuentas.
     * Verifica si el clienteId existe y publica la respuesta.
     *
     * @param solicitud DTO con clienteId a validar y datos de la cuenta origen
     */
    @RabbitListener(queues = RabbitMQConfig.CLIENTES_VALIDACION_QUEUE)
    public void validarCliente(ClienteValidacionDTO solicitud) {

        log.info("Solicitud de validación recibida para clienteId={}, cuentaId={}",
                solicitud.getClienteId(),
                solicitud.getCuentaId());

        // Verificar si el clienteId existe en nuestra base de datos
        boolean existe = clienteRepository.existsById(solicitud.getClienteId());

        // Completar el DTO con el resultado y devolver la respuesta
        ClienteValidacionDTO respuesta = ClienteValidacionDTO.builder()
                .clienteId(solicitud.getClienteId())
                .cuentaId(solicitud.getCuentaId())
                .numeroCuenta(solicitud.getNumeroCuenta())
                .clienteExiste(existe)
                .build();

        log.info("Respondiendo validación: clienteId={} existe={}",
                solicitud.getClienteId(), existe);

        // Publicar la respuesta en la queue que escucha ms-cuentas
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.CUENTAS_EXCHANGE,
                RabbitMQConfig.CUENTA_VALIDACION_RESPUESTA_ROUTING_KEY,
                respuesta
        );
    }
}
