package com.omnisupport.ai.infrastructure.messaging;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.omnisupport.ai.application.AnalyzeTicketUseCase;
import com.omnisupport.ai.domain.AnalysisResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TicketCreatedConsumer {

    private static final Logger log = LoggerFactory.getLogger(TicketCreatedConsumer.class);

    private final AnalyzeTicketUseCase analyzeTicketUseCase;
    private final ObjectMapper objectMapper;

    public TicketCreatedConsumer(AnalyzeTicketUseCase analyzeTicketUseCase, ObjectMapper objectMapper) {
        this.analyzeTicketUseCase = analyzeTicketUseCase;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "ticket.created", groupId = "ai-analyzer-service")
    public void consume(String message) {
        try {
            JsonNode node = objectMapper.readTree(message);
            Long ticketId = node.path("ticketId").asLong();
            String description = node.path("description").asText();

            log.info("Received TicketCreatedEvent for ticket id={}", ticketId);

            AnalysisResult result = analyzeTicketUseCase.execute(description);

            log.info("Analysis completed for ticket id={}: category={}, suggestedResponse={}",
                    ticketId, result.category(), result.suggestedResponse());
        } catch (IOException e) {
            log.error("Failed to deserialize TicketCreatedEvent", e);
        }
    }
}
