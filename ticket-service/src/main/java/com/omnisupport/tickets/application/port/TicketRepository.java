package com.omnisupport.tickets.application.port;

import com.omnisupport.tickets.domain.Ticket;

public interface TicketRepository {

    Ticket save(Ticket ticket);
}
