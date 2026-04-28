# ─── Stage 1: Build ─────────────────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /build

# Copiar pom.xml primero para cachear dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar fuente y compilar (sin tests - corren por separado)
COPY src ./src
RUN mvn clean package -DskipTests -B

# ─── Stage 2: Runtime ────────────────────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Usuario no-root por seguridad
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

COPY --from=builder /build/target/*.jar app.jar

USER appuser

EXPOSE 8080

ENV DB_HOST=postgres-clientes \
    DB_PORT=5432 \
    DB_NAME=clientes_db \
    DB_USER=postgres \
    DB_PASSWORD=postgres \
    RABBITMQ_HOST=rabbitmq \
    RABBITMQ_PORT=5672 \
    RABBITMQ_USER=guest \
    RABBITMQ_PASSWORD=guest \
    SERVER_PORT=8080

ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-jar", "app.jar"]
