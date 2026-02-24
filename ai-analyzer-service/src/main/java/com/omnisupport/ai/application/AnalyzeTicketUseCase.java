package com.omnisupport.ai.application;

import com.omnisupport.ai.application.port.LlmPort;
import com.omnisupport.ai.application.port.VectorStorePort;
import com.omnisupport.ai.domain.AnalysisResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnalyzeTicketUseCase {

    private static final Logger log = LoggerFactory.getLogger(AnalyzeTicketUseCase.class);
    private static final int RAG_MAX_RESULTS = 5;

    private final VectorStorePort vectorStorePort;
    private final LlmPort llmPort;

    public AnalyzeTicketUseCase(VectorStorePort vectorStorePort, LlmPort llmPort) {
        this.vectorStorePort = vectorStorePort;
        this.llmPort = llmPort;
    }

    public AnalysisResult execute(String ticketDescription) {
        log.debug("Analyzing ticket: {}", ticketDescription);
        List<Document> relevantDocs = vectorStorePort.similaritySearch(ticketDescription, RAG_MAX_RESULTS);
        String context = relevantDocs.stream()
                .map(Document::getContent)
                .collect(Collectors.joining("\n\n"));
        if (context.isBlank()) {
            context = "No hay documentación relevante en la base de conocimientos.";
        }
        return llmPort.analyzeWithRag(ticketDescription, context);
    }
}
