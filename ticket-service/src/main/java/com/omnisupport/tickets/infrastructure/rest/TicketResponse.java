package com.omnisupport.tickets.infrastructure.rest;

import com.omnisupport.tickets.domain.Ticket;

import java.time.Instant;

public record TicketResponse(
        Long id,
        String title,
        String description,
        String contactEmail,
        String status,
        Instant createdAt
) {
    public static TicketResponse from(Ticket ticket) {
        return new TicketResponse(
                ticket.id(),
                ticket.title(),
                ticket.description(),
                ticket.contactEmail(),
                ticket.status().name(),
                ticket.createdAt()
        );
    }
}
