package com.omnisupport.tickets.domain;

import java.time.Instant;

public record Ticket(
        Long id,
        String title,
        String description,
        String contactEmail,
        TicketStatus status,
        Instant createdAt
) {
    public static Ticket create(String title, String description, String contactEmail) {
        return new Ticket(null, title, description, contactEmail, TicketStatus.PENDING, Instant.now());
    }
}
