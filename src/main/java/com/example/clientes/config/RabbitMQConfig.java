package com.example.clientes.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de RabbitMQ para ms-clientes.
 *
 * Topología de mensajería:
 *
 *   ms-clientes  ──(producer)──▶  Exchange: clientes.events
 *                                      │
 *                                      └──▶  Queue: clientes.eventos.cuentas
 *                                                 │
 *                                           ms-cuentas (consumer)
 *                                           escucha cambios de estado del cliente
 *                                           (ej: cliente desactivado → bloquear cuentas)
 *
 *   ms-clientes  ◀─(consumer)──  Queue: clientes.solicitudes.validacion
 *                                      │
 *                                 ms-cuentas (producer)
 *                                 solicita validar que un clienteId existe
 *                                 antes de crear una cuenta
 *
 * Patrón utilizado: Topic Exchange
 *   Permite enrutar mensajes por routing key con wildcards (* y #),
 *   facilitando extensiones futuras sin cambiar la configuración base.
 */
@Configuration
public class RabbitMQConfig {

    // ── Nombres de Exchange ────────────────────────────────────────────────

    /** Exchange principal del microservicio de clientes */
    public static final String CLIENTES_EXCHANGE = "clientes.events";

    // ── Nombres de Queues ──────────────────────────────────────────────────

    /**
     * Cola donde ms-clientes publica eventos de cambio de estado.
     * ms-cuentas consume esta cola para reaccionar a cambios en clientes.
     */
    public static final String CLIENTES_EVENTOS_CUENTAS_QUEUE = "clientes.eventos.cuentas";

    /**
     * Cola donde ms-clientes recibe solicitudes de validación de clienteId
     * enviadas por ms-cuentas antes de crear una cuenta.
     */
    public static final String CLIENTES_VALIDACION_QUEUE = "clientes.solicitudes.validacion";

    // ── Routing Keys ───────────────────────────────────────────────────────

    public static final String CLIENTE_EVENTO_ROUTING_KEY = "cliente.evento.#";
    public static final String CLIENTE_VALIDACION_ROUTING_KEY = "cliente.validacion.solicitud";

    // ── Beans: Exchange ────────────────────────────────────────────────────

    @Bean
    public TopicExchange clientesExchange() {
        return ExchangeBuilder
                .topicExchange(CLIENTES_EXCHANGE)
                .durable(true)
                .build();
    }

    // ── Beans: Queues ──────────────────────────────────────────────────────

    @Bean
    public Queue clientesEventosCuentasQueue() {
        return QueueBuilder
                .durable(CLIENTES_EVENTOS_CUENTAS_QUEUE)
                .build();
    }

    @Bean
    public Queue clientesValidacionQueue() {
        return QueueBuilder
                .durable(CLIENTES_VALIDACION_QUEUE)
                .build();
    }

    // ── Beans: Bindings ────────────────────────────────────────────────────

    @Bean
    public Binding bindingClientesEventosCuentas() {
        return BindingBuilder
                .bind(clientesEventosCuentasQueue())
                .to(clientesExchange())
                .with(CLIENTE_EVENTO_ROUTING_KEY);
    }

    @Bean
    public Binding bindingClientesValidacion() {
        return BindingBuilder
                .bind(clientesValidacionQueue())
                .to(clientesExchange())
                .with(CLIENTE_VALIDACION_ROUTING_KEY);
    }

    // ── Beans: Serialización ───────────────────────────────────────────────

    /**
     * Convierte los mensajes a/desde JSON automáticamente.
     * Permite enviar objetos Java directamente sin serializar manualmente.
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * RabbitTemplate configurado con el converter JSON.
     * Es el bean que se usa para publicar mensajes (producer).
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
