package com.omnisupport.tickets.infrastructure.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.omnisupport.tickets.application.port.TicketEventPublisher;
import com.omnisupport.tickets.domain.Ticket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaTicketEventPublisher implements TicketEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaTicketEventPublisher.class);
    private static final String TOPIC = "ticket.created";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaTicketEventPublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publishTicketCreated(Ticket ticket) {
        TicketCreatedEvent event = new TicketCreatedEvent(
                ticket.id(),
                ticket.title(),
                ticket.description(),
                ticket.contactEmail(),
                ticket.createdAt()
        );
        try {
            String payload = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(TOPIC, String.valueOf(ticket.id()), payload);
            log.debug("Published TicketCreatedEvent for ticket id={}", ticket.id());
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize TicketCreatedEvent for ticket id={}", ticket.id(), e);
            throw new EventPublishException("Failed to publish ticket created event", e);
        }
    }
}
