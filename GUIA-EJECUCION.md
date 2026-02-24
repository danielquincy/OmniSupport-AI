# Guía de Ejecución - OmniSupport AI

## Requisitos Previos

- **Java 21** instalado
- **Maven 3.9+** en el PATH
- **Docker Desktop** (o Docker + Docker Compose)
- **Ollama** (solo para el AI Analyzer)

---

## Paso 1: Levantar la Infraestructura

En la raíz del proyecto:

```bash
cd c:\Desarrollo\Proyectos Kafka\omnisupport-ai
docker compose up -d
```

### Verificar que los contenedores estén en marcha

```bash
docker compose ps
```

Deben aparecer: `kafka`, `postgres`, `oracle`, `prometheus`, `grafana`.

### Importante: Tiempo de arranque de Oracle

Oracle tarda **2-5 minutos** en estar listo. Para comprobarlo:

```bash
docker logs omnisupport-oracle -f
```

Cuando veas el mensaje de que la base de datos está lista, continúa.

---

## Paso 2: Configurar Ollama (para AI Analyzer)

1. Instala Ollama desde [ollama.ai](https://ollama.ai).
2. En una terminal:

```bash
ollama pull llama3.2
ollama pull nomic-embed-text
ollama serve
```

Ollama queda por defecto en `http://localhost:11434` (coincide con `application.yml`).

---

## Paso 3: Compilar el Proyecto

```bash
cd c:\Desarrollo\Proyectos Kafka\omnisupport-ai
mvn clean install -DskipTests
```

Si hay fallos de dependencias, prueba sin `-DskipTests` para ver errores de tests.

---

## Paso 4: Ejecutar los Microservicios

Abre **3 terminales** distintas.

### Terminal 1 - API Gateway (puerto 8080)

```bash
cd c:\Desarrollo\Proyectos Kafka\omnisupport-ai
mvn -pl api-gateway spring-boot:run
```

Espera hasta ver: `Started OmniSupportApiGatewayApplication`.

### Terminal 2 - Ticket Service (puerto 8081)

```bash
cd c:\Desarrollo\Proyectos Kafka\omnisupport-ai
mvn -pl ticket-service spring-boot:run
```

Espera hasta ver: `Started OmniSupportTicketServiceApplication`.

### Terminal 3 - AI Analyzer Service (puerto 8082)

```bash
cd c:\Desarrollo\Proyectos Kafka\omnisupport-ai
mvn -pl ai-analyzer-service spring-boot:run
```

Espera hasta ver: `Started OmniSupportAiAnalyzerApplication`.

**Nota:** El AI Analyzer necesita Oracle y Ollama. Si falla por Oracle, comprueba que `omnisupport-oracle` esté listo. Si falla por Ollama, verifica que esté en ejecución.

---

## Paso 5: Probar el Flujo

### Crear un ticket

```bash
curl -X POST http://localhost:8080/api/tickets ^
  -H "Content-Type: application/json" ^
  -d "{\"title\":\"Error al iniciar sesión\",\"description\":\"No puedo acceder con mi contraseña\",\"contactEmail\":\"usuario@ejemplo.com\"}"
```

En PowerShell (si `^` da problemas):

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/tickets" -Method POST -ContentType "application/json" -Body '{"title":"Error al iniciar sesión","description":"No puedo acceder con mi contraseña","contactEmail":"usuario@ejemplo.com"}'
```

### Respuesta esperada

```json
{
  "id": 1,
  "title": "Error al iniciar sesión",
  "description": "No puedo acceder con mi contraseña",
  "contactEmail": "usuario@ejemplo.com",
  "status": "PENDING",
  "createdAt": "2025-02-23T..."
}
```

### Verificar el flujo completo

1. El ticket se guarda en PostgreSQL.
2. Se publica un evento en Kafka (`ticket.created`).
3. El AI Analyzer consume el evento y procesa el ticket con RAG + LLM.
4. En los logs del AI Analyzer deberías ver: `Analysis completed for ticket id=1: category=..., suggestedResponse=...`

---

## Paso 6: Monitoreo (opcional)

| Servicio   | URL                | Credenciales       |
|-----------|--------------------|--------------------|
| Grafana   | http://localhost:3000 | admin / admin   |
| Prometheus| http://localhost:9090 | -               |
| Health Gateway | http://localhost:8080/actuator/health | - |
| Health Ticket  | http://localhost:8081/actuator/health | - |
| Health AI      | http://localhost:8082/actuator/health | - |

---

## Orden Sugerido

1. `docker compose up -d`
2. Esperar Oracle (2-5 min)
3. Arrancar Ollama
4. `mvn clean install`
5. API Gateway
6. Ticket Service
7. AI Analyzer Service
8. Probar creación de ticket

---

## Solución de Problemas

### Kafka no conecta
- Confirma que el contenedor `omnisupport-kafka` está en ejecución.
- En Windows, `localhost:9092` debe ser accesible.

### PostgreSQL rechaza conexión
- Comprueba credenciales en `application.yml`: `tickets_user` / `tickets_secret`.
- Puerto 5432 libre.

### Oracle rechaza conexión
- Espera al menos 3-5 minutos tras `docker compose up`.
- URL de conexión: `jdbc:oracle:thin:@localhost:1521/FREEPDB1`
- Usuario: `system` / Contraseña: `OraclePass123`

### AI Analyzer falla al arrancar
- Si es por Oracle: espera a que Oracle esté listo.
- Si es por Ollama: comprueba que Ollama responde en `http://localhost:11434`.
- Base de conocimientos vacía: el RAG puede devolver categoría/respuesta por defecto; es normal si no has cargado documentos en Oracle.

### Puertos ocupados
Si 8080, 8081, 8082 o 9092 están en uso, cambia los puertos en cada `application.yml`.

---

## Ejecución en Entorno Docker (avanzado)

Para ejecutar los microservicios dentro de Docker, habría que:
1. Crear Dockerfiles por servicio.
2. Ajustar las URLs en `application.yml` (por ejemplo `kafka:9092` en lugar de `localhost:9092`).
3. Añadir los servicios al `docker-compose.yml`.

Esta guía asume ejecución local de los microservicios con la infraestructura en Docker.
