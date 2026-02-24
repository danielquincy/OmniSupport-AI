package com.omnisupport.ai.infrastructure.ai;

import com.omnisupport.ai.application.port.LlmPort;
import com.omnisupport.ai.domain.AnalysisResult;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SpringAiLlmAdapter implements LlmPort {

    private static final Logger log = LoggerFactory.getLogger(SpringAiLlmAdapter.class);
    private static final String DEFAULT_CATEGORY = "GENERAL";
    private static final String DEFAULT_RESPONSE = "Gracias por contactarnos. Un agente revisará su consulta y le responderá en breve.";

    private static final Pattern CATEGORY_PATTERN = Pattern.compile("(?i)categor[ií]a[:\\s]+([^\\n]+)");
    private static final Pattern RESPONSE_PATTERN = Pattern.compile("(?i)(?:respuesta|sugerencia)[:\\s]+([\\s\\S]+)");

    private final ChatModel chatModel;

    public SpringAiLlmAdapter(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @Override
    @CircuitBreaker(name = "llmService", fallbackMethod = "fallback")
    public AnalysisResult analyzeWithRag(String ticketDescription, String contextFromKnowledgeBase) {
        String promptText = buildRagPrompt(ticketDescription, contextFromKnowledgeBase);
        String response = chatModel.call(new Prompt(promptText)).getResult().getOutput().getContent();
        return parseLlmResponse(response);
    }

    private AnalysisResult fallback(String ticketDescription, String contextFromKnowledgeBase, Exception ex) {
        log.warn("LLM circuit breaker fallback activated for ticket analysis", ex);
        return new AnalysisResult(DEFAULT_CATEGORY, DEFAULT_RESPONSE);
    }

    private String buildRagPrompt(String ticketDescription, String contextFromKnowledgeBase) {
        return """
            Eres un asistente de soporte técnico. Analiza el siguiente ticket de soporte y proporciona:
            1. Una categoría apropiada (ej: TÉCNICO, FACTURACIÓN, CUENTA, GENERAL)
            2. Una respuesta sugerida para el cliente basada en el contexto de la base de conocimientos.

            Contexto de la base de conocimientos:
            %s

            Descripción del ticket:
            %s

            Responde en el siguiente formato:
            Categoría: [tu categoría]
            Respuesta sugerida: [tu respuesta]
            """.formatted(contextFromKnowledgeBase, ticketDescription);
    }

    private AnalysisResult parseLlmResponse(String response) {
        String category = extractGroup(response, CATEGORY_PATTERN, 1).orElse(DEFAULT_CATEGORY).trim();
        String suggestedResponse = extractGroup(response, RESPONSE_PATTERN, 1).orElse(DEFAULT_RESPONSE).trim();
        return new AnalysisResult(category, suggestedResponse);
    }

    private Optional<String> extractGroup(String text, Pattern pattern, int group) {
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? Optional.of(matcher.group(group).trim()) : Optional.empty();
    }
}
