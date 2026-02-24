package com.omnisupport.ai.infrastructure.ai;

import com.omnisupport.ai.application.port.VectorStorePort;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OracleVectorStoreAdapter implements VectorStorePort {

    private final VectorStore vectorStore;

    public OracleVectorStoreAdapter(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @Override
    public List<Document> similaritySearch(String query, int maxResults) {
        return vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(query)
                        .topK(maxResults)
                        .build()
        );
    }
}
