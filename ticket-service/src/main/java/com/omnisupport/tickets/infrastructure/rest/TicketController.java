package com.omnisupport.tickets.infrastructure.rest;

import com.omnisupport.tickets.application.CreateTicketUseCase;
import com.omnisupport.tickets.domain.Ticket;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final CreateTicketUseCase createTicketUseCase;

    public TicketController(CreateTicketUseCase createTicketUseCase) {
        this.createTicketUseCase = createTicketUseCase;
    }

    @PostMapping
    public ResponseEntity<TicketResponse> createTicket(@Valid @RequestBody CreateTicketRequest request) {
        Ticket ticket = createTicketUseCase.execute(
                request.title(),
                request.description(),
                request.contactEmail()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(TicketResponse.from(ticket));
    }
}
