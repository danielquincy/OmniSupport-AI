package com.omnisupport.ai.application.port;

import org.springframework.ai.document.Document;

import java.util.List;

public interface VectorStorePort {

    List<Document> similaritySearch(String query, int maxResults);
}
