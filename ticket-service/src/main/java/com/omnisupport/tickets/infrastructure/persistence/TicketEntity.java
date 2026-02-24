package com.omnisupport.tickets.infrastructure.persistence;

import com.omnisupport.tickets.domain.Ticket;
import com.omnisupport.tickets.domain.TicketStatus;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "tickets")
public class TicketEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 4096)
    private String description;

    @Column(name = "contact_email", nullable = false)
    private String contactEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected TicketEntity() {
    }

    public TicketEntity(String title, String description, String contactEmail, TicketStatus status, Instant createdAt) {
        this.title = title;
        this.description = description;
        this.contactEmail = contactEmail;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Ticket toDomain() {
        return new Ticket(id, title, description, contactEmail, status, createdAt);
    }

    public static TicketEntity fromDomain(Ticket ticket) {
        TicketEntity entity = new TicketEntity(
                ticket.title(),
                ticket.description(),
                ticket.contactEmail(),
                ticket.status(),
                ticket.createdAt()
        );
        if (ticket.id() != null) {
            entity.id = ticket.id();
        }
        return entity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
