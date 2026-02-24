# OmniSupport AI

Plataforma asíncrona de resolución de tickets de soporte con IA, basada en arquitectura event-driven.

## Stack Tecnológico

- **Java 21** (Virtual Threads, Records)
- **Spring Boot 4.0.x** + Jakarta EE 11
- **Spring AI** + Apache Kafka
- **PostgreSQL** (tickets) + **Oracle 23ai** (RAG/Vector Search)
- **Docker Compose** (Kafka KRaft, PostgreSQL, Oracle, Prometheus, Grafana)

## Estructura del Proyecto

```
omnisupport-ai/
├── api-gateway/          # Puerto 8080 - API Gateway + CORS
├── ticket-service/       # Puerto 8081 - Creación de tickets + Kafka
├── ai-analyzer-service/  # Puerto 8082 - Consumer Kafka + RAG + Circuit Breaker
├── docker-compose.yml
├── prometheus/
└── grafana/provisioning/
```

## Ejecución

### 1. Levantar infraestructura

```bash
docker compose up -d
```

Esperar a que Oracle termine de iniciar (2-3 minutos).

### 2. Compilar y ejecutar microservicios

```bash
mvn clean install
mvn -pl api-gateway spring-boot:run &
mvn -pl ticket-service spring-boot:run &
mvn -pl ai-analyzer-service spring-boot:run &
```

### 3. Crear un ticket

```bash
curl -X POST http://localhost:8080/api/tickets \
  -H "Content-Type: application/json" \
  -d '{"title":"Error al iniciar sesión","description":"No puedo acceder con mi contraseña","contactEmail":"usuario@ejemplo.com"}'
```

### 4. Requisitos adicionales para AI

- **Ollama** ejecutándose localmente (`ollama run llama3.2` y `ollama run nomic-embed-text`)
- Base de conocimientos poblada en Oracle 23ai para el RAG

## Puertos

| Servicio    | Puerto |
|-------------|--------|
| API Gateway | 8080   |
| Ticket Service | 8081 |
| AI Analyzer | 8082   |
| Kafka       | 9092   |
| PostgreSQL  | 5432   |
| Oracle 23ai | 1521   |
| Prometheus  | 9090   |
| Grafana     | 3000   |
