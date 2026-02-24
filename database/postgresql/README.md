# PostgreSQL - OmniSupport AI

Scripts de creación de esquema para la base de datos **PostgreSQL** del ticket-service.

## Configuración Docker

- **Base de datos:** tickets_db  
- **Usuario:** tickets_user  
- **Contraseña:** tickets_secret  
- **Puerto:** 5432  

## Ejecución

### Opción 1: Desde línea de comandos (psql)

```bash
# Conectar y ejecutar
psql -h localhost -U tickets_user -d tickets_db -f 001_create_tickets_table.sql
```

### Opción 2: Copiar scripts al contenedor

```bash
# Copiar scripts al contenedor
docker cp database/postgresql/ omnisupport-postgres:/tmp/

# Ejecutar dentro del contenedor
docker exec -i omnisupport-postgres psql -U tickets_user -d tickets_db -f /tmp/postgresql/001_create_tickets_table.sql
```

### Opción 3: Montar como volumen en docker-compose

Agregar al servicio postgres:

```yaml
volumes:
  - postgres-data:/var/lib/postgresql/data
  - ./database/postgresql:/docker-entrypoint-initdb.d
```

Los scripts `.sql` en `/docker-entrypoint-initdb.d` se ejecutan automáticamente al crear el contenedor por primera vez.

## Orden de ejecución

1. `001_create_tickets_table.sql` - Crea la tabla tickets
2. `002_sample_data.sql` - (Opcional) Datos de prueba

## Nota sobre JPA

Si `spring.jpa.hibernate.ddl-auto` está en `create`, `create-drop` o `update`, Hibernate puede crear/actualizar el esquema automáticamente. Estos scripts son útiles para:

- Entornos sin auto-DDL
- Documentación del esquema
- Migraciones controladas (Flyway/Liquibase)
- Instalación manual
