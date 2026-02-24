-- =============================================================================
-- OmniSupport AI - PostgreSQL Schema
-- Base de datos: tickets_db
-- Usuario: tickets_user
-- =============================================================================
-- Ejecutar como: tickets_user sobre tickets_db
-- =============================================================================

-- Tabla de tickets (ticket-service)
-- Compatible con TicketEntity (JPA)
-- =============================================================================

CREATE TABLE IF NOT EXISTS tickets (
    id              BIGSERIAL PRIMARY KEY,
    title           VARCHAR(255) NOT NULL,
    description     VARCHAR(4096) NOT NULL,
    contact_email   VARCHAR(255) NOT NULL,
    status          VARCHAR(50) NOT NULL,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT chk_ticket_status CHECK (status IN ('PENDING', 'IN_PROGRESS', 'RESOLVED', 'CLOSED'))
);

-- Índices para consultas frecuentes
CREATE INDEX IF NOT EXISTS idx_tickets_status ON tickets(status);
CREATE INDEX IF NOT EXISTS idx_tickets_created_at ON tickets(created_at DESC);
CREATE INDEX IF NOT EXISTS idx_tickets_contact_email ON tickets(contact_email);

-- Comentarios de documentación
COMMENT ON TABLE tickets IS 'Tickets de soporte - OmniSupport AI';
COMMENT ON COLUMN tickets.status IS 'Estados: PENDING, IN_PROGRESS, RESOLVED, CLOSED';
