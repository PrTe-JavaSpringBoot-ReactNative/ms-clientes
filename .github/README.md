# ms-clientes — Microservicio de Clientes y Personas

Microservicio responsable del dominio **Persona** y **Cliente** dentro de la arquitectura de la Prueba Técnica de Microservicios (2023).

---

## Responsabilidad de este microservicio

Este servicio es el dueño exclusivo de los datos de personas y clientes. Ningún otro microservicio accede directamente a su base de datos. La comunicación inter-servicio se realiza únicamente a través de RabbitMQ.

**Entidades de dominio a implementar:**
- `Persona` → nombre, género, edad, identificación, dirección, teléfono
- `Cliente` → hereda de `Persona`, agrega: clienteId, contraseña, estado

---

## Tecnologías

| Tecnología | Versión | Rol |
|---|---|---|
| **Java** | 17 (LTS) | Lenguaje principal |
| **Spring Boot** | 3.2.5 | Framework |
| **Spring Data JPA / Hibernate** | (incluido) | ORM / acceso a datos |
| **PostgreSQL** | 15 | Base de datos (producción) |
| **Lombok** | (incluido) | Reducción de boilerplate |
| **JUnit 5 + Mockito + MockMvc** | (incluido) | Testing |

---

## Estructura del proyecto

```
ms-clientes/
├── src/
│   ├── main/
│   │   ├── java/com/example/clientes/
│   │   │   ├── MsClientesApplication.java       # Entry point
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   ├── repository/
│   │   │   ├── entity/
│   │   │   ├── dto/
│   │   │   └── exception/
│   │   └── resources/
│   │       └── application.yml                   # Config PostgreSQL + RabbitMQ
├── .github/
│   └── README.md                                
├── Dockerfile                                   
├── pom.xml                                      
└── .gitignore
```

---

## Decisiones de arquitectura

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

## Para ejecutar requiere PostgreSQL funcionando:

```bash
# Luego correr el microservicio desde el IDE o:
cd ms-clientes
mvn spring-boot:run
```

La app estará en: `http://localhost:8081/api`

### Ejecutar tests

```bash
cd ms-cuentas
mvn test
```

---

## Endpoints

| Método | Endpoint | Descripción |
|---|---|---|---|
| GET | `/api/health` | Verificación de vida |
| GET | `/api/clientes` | Listar clientes 
| GET | `/api/clientes/{id}` | Obtener cliente |
| POST | `/api/clientes` | Crear cliente | 
| PUT | `/api/clientes/{id}` | Actualizar cliente | 
| DELETE | `/api/clientes/{id}` | Eliminar cliente |

---

## Variables de entorno

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
