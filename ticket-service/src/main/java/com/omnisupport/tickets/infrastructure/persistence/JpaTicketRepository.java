package com.omnisupport.tickets.infrastructure.persistence;

import com.omnisupport.tickets.application.port.TicketRepository;
import com.omnisupport.tickets.domain.Ticket;
import org.springframework.stereotype.Component;

@Component
public class JpaTicketRepository implements TicketRepository {

    private final SpringDataTicketRepository springDataRepository;

    public JpaTicketRepository(SpringDataTicketRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public Ticket save(Ticket ticket) {
        TicketEntity entity = TicketEntity.fromDomain(ticket);
        TicketEntity saved = springDataRepository.save(entity);
        return saved.toDomain();
    }
}
