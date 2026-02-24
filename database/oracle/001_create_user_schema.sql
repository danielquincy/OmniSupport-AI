-- =============================================================================
-- OmniSupport AI - Oracle 23ai - Creación de usuario/schema
-- Base de datos: FREEPDB1 (pluggable database)
-- =============================================================================
-- Ejecutar como SYS o SYSTEM conectado a FREEPDB1
-- =============================================================================

-- Crear usuario para la aplicación (opcional si se usa SYSTEM)
-- Descomentar si se prefiere un usuario dedicado:
/*
CREATE USER omnisupport_ai IDENTIFIED BY OraclePass123
    DEFAULT TABLESPACE USERS
    TEMPORARY TABLESPACE TEMP
    QUOTA UNLIMITED ON USERS;

GRANT CONNECT, RESOURCE TO omnisupport_ai;
GRANT CREATE VIEW TO omnisupport_ai;
*/
