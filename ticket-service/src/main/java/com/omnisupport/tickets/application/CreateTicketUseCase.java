package com.omnisupport.tickets.application;

import com.omnisupport.tickets.application.port.TicketEventPublisher;
import com.omnisupport.tickets.application.port.TicketRepository;
import com.omnisupport.tickets.domain.Ticket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CreateTicketUseCase {

    private static final Logger log = LoggerFactory.getLogger(CreateTicketUseCase.class);

    private final TicketRepository ticketRepository;
    private final TicketEventPublisher ticketEventPublisher;

    public CreateTicketUseCase(TicketRepository ticketRepository, TicketEventPublisher ticketEventPublisher) {
        this.ticketRepository = ticketRepository;
        this.ticketEventPublisher = ticketEventPublisher;
    }

    public Ticket execute(String title, String description, String contactEmail) {
        Ticket ticket = Ticket.create(title, description, contactEmail);
        Ticket saved = ticketRepository.save(ticket);
        ticketEventPublisher.publishTicketCreated(saved);
        log.info("Ticket created with id={}", saved.id());
        return saved;
    }
}
