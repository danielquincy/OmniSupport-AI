package com.omnisupport.tickets.infrastructure.messaging;

import java.time.Instant;

public record TicketCreatedEvent(
        Long ticketId,
        String title,
        String description,
        String contactEmail,
        Instant createdAt
) {
}
