/**
 * Paquete de mensajería asíncrona con RabbitMQ.
 *
 * Clases a implementar en este paquete:
 *
 * ┌─────────────────────────────────────────────────────────────────────┐
 * │  ClienteEventoPublisher  (Producer)                                 │
 * │                                                                     │
 * │  Publica eventos cuando el estado de un cliente cambia.             │
 * │  Ejemplo: cliente desactivado → ms-cuentas bloquea sus cuentas.    │
 * │                                                                     │
 * │  Exchange : clientes.events                                         │
 * │  Routing  : cliente.evento.estado  /  cliente.evento.eliminado      │
 * │                                                                     │
 * │  @Component                                                         │
 * │  public class ClienteEventoPublisher {                              │
 * │      @Autowired RabbitTemplate rabbitTemplate;                      │
 * │      public void publicarCambioEstado(ClienteEventoDTO evento) {    │
 * │          rabbitTemplate.convertAndSend(                             │
 * │              RabbitMQConfig.CLIENTES_EXCHANGE,                      │
 * │              "cliente.evento.estado",                               │
 * │              evento);                                               │
 * │      }                                                              │
 * │  }                                                                  │
 * └─────────────────────────────────────────────────────────────────────┘
 *
 * ┌─────────────────────────────────────────────────────────────────────┐
 * │  ClienteValidacionConsumer  (Consumer)                              │
 * │                                                                     │
 * │  Escucha solicitudes de ms-cuentas para validar si un clienteId    │
 * │  existe antes de permitir la creación de una cuenta.               │
 * │                                                                     │
 * │  Queue : clientes.solicitudes.validacion                            │
 * │                                                                     │
 * │  @Component                                                         │
 * │  public class ClienteValidacionConsumer {                           │
 * │      @RabbitListener(queues =                                       │
 * │          RabbitMQConfig.CLIENTES_VALIDACION_QUEUE)                  │
 * │      public void validarCliente(ValidacionRequestDTO request) {     │
 * │          // buscar clienteId en repo y responder                    │
 * │      }                                                              │
 * │  }                                                                  │
 * └─────────────────────────────────────────────────────────────────────┘
 */
package com.example.clientes.messaging;
