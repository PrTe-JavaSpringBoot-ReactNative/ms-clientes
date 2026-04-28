# ms-clientes — Microservicio de Clientes y Personas

Microservicio responsable del dominio **Persona** y **Cliente** dentro de la arquitectura de la Prueba Técnica de Microservicios (2023).

---

## 🎯 Responsabilidad de este microservicio

Este servicio es el dueño exclusivo de los datos de personas y clientes. Ningún otro microservicio accede directamente a su base de datos. La comunicación inter-servicio se realiza únicamente a través de RabbitMQ.

**Entidades de dominio a implementar:**
- `Persona` → nombre, género, edad, identificación, dirección, teléfono
- `Cliente` → hereda de `Persona`, agrega: clienteId, contraseña, estado

---

## 🛠️ Tecnologías

| Tecnología | Versión | Rol |
|---|---|---|
| **Java** | 17 (LTS) | Lenguaje principal |
| **Spring Boot** | 3.2.5 | Framework |
| **Spring Data JPA / Hibernate** | (incluido) | ORM / acceso a datos |
| **Spring AMQP** | (incluido) | Cliente RabbitMQ |
| **PostgreSQL** | 15 | Base de datos (producción) |
| **H2** | (incluido) | Base de datos en tests |
| **Lombok** | (incluido) | Reducción de boilerplate |
| **JUnit 5 + Mockito + MockMvc** | (incluido) | Testing |

---

## 📁 Estructura del proyecto

```
ms-clientes/
├── src/
│   ├── main/
│   │   ├── java/com/example/clientes/
│   │   │   ├── MsClientesApplication.java       # Entry point
│   │   │   ├── controller/
│   │   │   │   └── HealthController.java         # ✅ GET /api/health
│   │   │   │   # TODO: ClienteController         → /api/clientes
│   │   │   ├── service/
│   │   │   │   # TODO: ClienteService            → lógica de negocio
│   │   │   ├── repository/
│   │   │   │   # TODO: ClienteRepository         → extends JpaRepository
│   │   │   ├── entity/
│   │   │   │   # TODO: Persona.java              → @MappedSuperclass
│   │   │   │   # TODO: Cliente.java              → @Entity, hereda Persona
│   │   │   ├── dto/
│   │   │   │   # TODO: ClienteRequestDTO
│   │   │   │   # TODO: ClienteResponseDTO
│   │   │   ├── exception/
│   │   │   │   └── GlobalExceptionHandler.java   # ✅ Manejador global
│   │   │   ├── config/
│   │   │   │   └── RabbitMQConfig.java           # ✅ Exchange, queues, bindings
│   │   │   └── messaging/
│   │   │       # TODO: ClienteEventoPublisher    → publica eventos de cliente
│   │   │       # TODO: ClienteValidacionConsumer → responde solicitudes de validación
│   │   └── resources/
│   │       └── application.yml                   # ✅ Config PostgreSQL + RabbitMQ
│   └── test/
│       ├── java/com/example/clientes/
│       │   ├── controller/
│       │   │   └── HealthControllerTest.java     # ✅ Tests del /health
│       │   └── service/
│       │       # TODO: ClienteServiceTest
│       └── resources/
│           └── application.yml                   # ✅ Config H2 para tests
├── .github/
│   └── README.md                                 # Este archivo
├── Dockerfile                                    # ✅ Multi-stage build
├── pom.xml                                       # ✅ Dependencias Maven
└── .gitignore
```

---

## 🏗️ Decisiones de arquitectura

### Herencia en JPA: `@MappedSuperclass`

`Persona` debe implementarse como `@MappedSuperclass` (no como `@Entity`). Esto significa que `Persona` no tiene tabla propia en la base de datos. Sus campos son heredados por `Cliente` que sí es una entidad con su propia tabla.

```java
// Enfoque recomendado
@MappedSuperclass
public abstract class Persona { ... }

@Entity
@Table(name = "clientes")
public class Cliente extends Persona { ... }
```

Alternativa: `@Inheritance(strategy = InheritanceType.JOINED)` si en el futuro se necesitan más subtipos de `Persona`. Para este ejercicio, `@MappedSuperclass` es más simple y eficiente.

### Separación de base de datos

Este microservicio tiene su **propia base de datos** (`clientes_db`). Esto es fundamental para el principio de autonomía de microservicios: ningún otro servicio accede a esta DB directamente.

### Patrón Repository

Todos los accesos a datos pasan por la interfaz de repositorio:
```java
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByClienteId(String clienteId);
}
```

---

## 📨 Mensajería asíncrona (RabbitMQ)

Este microservicio participa en la mensajería de la siguiente forma:

```
ms-clientes (PRODUCER) ──▶ Exchange: clientes.events
                                 │
                                 ├──▶ [cliente.evento.estado]
                                 │         └──▶ Queue: clientes.eventos.cuentas
                                 │                   └── Consumer: ms-cuentas
                                 │
                                 └──▶ [cliente.validacion.solicitud]
                                           └──▶ Queue: clientes.solicitudes.validacion
                                                     └── Consumer: ms-clientes (este ms)

ms-clientes (CONSUMER) ◀── Queue: clientes.solicitudes.validacion
                               Responde si un clienteId existe
```

**Clases a implementar en `messaging/`:**
- `ClienteEventoPublisher` → publica cuando un cliente cambia de estado
- `ClienteValidacionConsumer` → escucha y responde si un clienteId es válido

---

## 🚀 Levantar solo este microservicio (desarrollo local)

Requiere PostgreSQL y RabbitMQ corriendo localmente:

```bash
# Desde la raíz del monorepo, levantar solo las dependencias
docker compose up postgres-clientes rabbitmq -d

# Luego correr el microservicio desde el IDE o:
cd ms-clientes
mvn spring-boot:run
```

La app estará en: `http://localhost:8080/api`

---

## 🧪 Ejecutar tests

```bash
cd ms-clientes
mvn test
```

No requiere Docker (usa H2 en memoria).

---

## 🔌 Endpoints

| Método | Endpoint | Descripción | Estado |
|---|---|---|---|
| GET | `/api/health` | Verificación de vida | ✅ Implementado |
| GET | `/api/clientes` | Listar clientes | 🔲 Pendiente |
| GET | `/api/clientes/{id}` | Obtener cliente | 🔲 Pendiente |
| POST | `/api/clientes` | Crear cliente | 🔲 Pendiente |
| PUT | `/api/clientes/{id}` | Actualizar cliente | 🔲 Pendiente |
| DELETE | `/api/clientes/{id}` | Eliminar cliente | 🔲 Pendiente |

---

## ⚙️ Variables de entorno

| Variable | Default | Descripción |
|---|---|---|
| `DB_HOST` | `postgres-clientes` | Host PostgreSQL |
| `DB_PORT` | `5432` | Puerto PostgreSQL |
| `DB_NAME` | `clientes_db` | Nombre de la DB |
| `DB_USER` | `postgres` | Usuario DB |
| `DB_PASSWORD` | `postgres` | Contraseña DB |
| `RABBITMQ_HOST` | `rabbitmq` | Host RabbitMQ |
| `RABBITMQ_PORT` | `5672` | Puerto AMQP |
| `RABBITMQ_USER` | `guest` | Usuario RabbitMQ |
| `RABBITMQ_PASSWORD` | `guest` | Contraseña RabbitMQ |
| `SERVER_PORT` | `8080` | Puerto HTTP |
