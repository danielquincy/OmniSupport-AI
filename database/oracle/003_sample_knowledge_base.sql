-- =============================================================================
-- OmniSupport AI - Oracle 23ai - Datos de ejemplo para base de conocimientos
-- =============================================================================
-- IMPORTANTE: Los embeddings deben generarse con el mismo modelo (nomic-embed-text)
-- que usa la aplicación. Este script inserta SOLO texto; los embeddings los
-- genera Spring AI al llamar a vectorStore.add(documents).
--
-- Para cargar documentos desde la app, use el endpoint o el VectorStore.add()
-- con org.springframework.ai.document.Document.
--
-- Este script es solo referencia de estructura. La carga real se hace vía API.
-- =============================================================================

-- Ejemplo de estructura (NO ejecutar directamente - los embeddings requieren el modelo):
/*
-- Los documentos se insertan típicamente vía Spring AI:
-- vectorStore.add(List.of(
--     new Document("Cómo resetear contraseña: Ir a configuración > Seguridad > Restablecer"),
--     new Document("Error 404: Verificar que la URL sea correcta y el recurso exista"),
--     new Document("Facturación: Las facturas se generan el primer día de cada mes")
-- ));
*/
