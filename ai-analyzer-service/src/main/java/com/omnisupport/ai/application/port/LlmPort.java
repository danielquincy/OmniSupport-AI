package com.omnisupport.ai.application.port;

import com.omnisupport.ai.domain.AnalysisResult;

public interface LlmPort {

    AnalysisResult analyzeWithRag(String ticketDescription, String contextFromKnowledgeBase);
}
