-- =============================================================================
-- OmniSupport AI - Oracle 23ai - Tabla Vector Store (RAG)
-- Compatible con Spring AI OracleVectorStore
-- Modelo embedding: nomic-embed-text (768 dimensiones)
-- =============================================================================
-- Ejecutar como: SYSTEM (o usuario con permisos) conectado a FREEPDB1
-- =============================================================================

-- Tabla para almacenar documentos y embeddings (base de conocimientos RAG)
-- Spring AI con initialize-schema=true crea una tabla similar automáticamente.
-- Este script es para creación manual o referencia.

-- Nota: Si usa spring.ai.vectorstore.oracle.initialize-schema=true,
-- Spring AI creará la tabla automáticamente. Use este script para:
-- - Entornos sin auto-inicialización
-- - Documentación
-- - Migraciones controladas

CREATE TABLE vector_store (
    id          VARCHAR2(36) PRIMARY KEY,
    content     CLOB NOT NULL,
    metadata    CLOB,
    embedding   VECTOR(768, FLOAT32)
);

-- Índice IVF para búsqueda por similitud (COSINE)
-- Compatible con spring.ai.vectorstore.oracle: index-type=IVF, distance-type=COSINE
-- Oracle usa por defecto sqrt(dataset_size) particiones si no se especifica
CREATE VECTOR INDEX idx_vector_store_embedding
    ON vector_store (embedding)
    ORGANIZATION NEIGHBOR PARTITIONS
    DISTANCE COSINE;

-- Comentarios
COMMENT ON TABLE vector_store IS 'Documentos y embeddings para RAG - Spring AI OracleVectorStore';
COMMENT ON COLUMN vector_store.embedding IS 'Vector de 768 dimensiones (nomic-embed-text)';
COMMENT ON COLUMN vector_store.metadata IS 'Metadatos en formato JSON';
