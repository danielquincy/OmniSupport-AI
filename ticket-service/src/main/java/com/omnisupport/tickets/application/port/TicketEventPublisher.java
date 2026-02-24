package com.omnisupport.tickets.application.port;

import com.omnisupport.tickets.domain.Ticket;

public interface TicketEventPublisher {

    void publishTicketCreated(Ticket ticket);
}
