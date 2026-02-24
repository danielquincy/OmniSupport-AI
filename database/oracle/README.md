# Oracle 23ai - OmniSupport AI

Scripts de creación de esquema para **Oracle 23ai** del ai-analyzer-service (Vector Store RAG).

## Configuración Docker

- **Imagen:** gvenzl/oracle-free:23  
- **PDB:** FREEPDB1  
- **Usuario:** system  
- **Contraseña:** OraclePass123  
- **Puerto:** 1521  

## Conexión JDBC

```
jdbc:oracle:thin:@localhost:1521/FREEPDB1
```

## Ejecución

### Opción 1: SQL*Plus / SQLcl

```bash
sqlplus system/OraclePass123@localhost:1521/FREEPDB1 @002_create_vector_store_table.sql
```

### Opción 2: Dentro del contenedor Docker

```bash
# Copiar scripts
docker cp database/oracle/ omnisupport-oracle:/tmp/

# Ejecutar (ejemplo con sqlplus)
docker exec -it omnisupport-oracle sqlplus system/OraclePass123@//localhost/FREEPDB1 @/tmp/oracle/002_create_vector_store_table.sql
```

### Opción 3: Herramientas gráficas

- SQL Developer  
- DBeaver  
- Oracle SQLcl  

Conectar a `localhost:1521`, servicio `FREEPDB1`, usuario `system`.

## Orden de ejecución

1. `001_create_user_schema.sql` – (Opcional) Usuario dedicado  
2. `002_create_vector_store_table.sql` – Tabla e índice vectorial  
3. `003_sample_knowledge_base.sql` – Referencia (carga vía API)  

## Compatibilidad con Spring AI

Configuración en `application.yml`:

```yaml
spring:
  ai:
    vectorstore:
      oracle:
        initialize-schema: true   # false si usa estos scripts
        index-type: IVF
        distance-type: COSINE
        dimensions: 768
```

- Si `initialize-schema: true`: Spring AI crea la tabla al arrancar.  
- Si `initialize-schema: false`: Ejecute `002_create_vector_store_table.sql` antes de iniciar la app.  

**Nota:** El nombre de tabla por defecto de Spring AI puede variar. Si usa un `tableName` distinto, ajuste el script o la propiedad correspondiente.
