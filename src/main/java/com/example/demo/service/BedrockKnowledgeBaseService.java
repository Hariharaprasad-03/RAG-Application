package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.bedrockagentruntime.BedrockAgentRuntimeClient;
import software.amazon.awssdk.services.bedrockagentruntime.model.KnowledgeBaseQuery;
import software.amazon.awssdk.services.bedrockagentruntime.model.RetrieveRequest;
import software.amazon.awssdk.services.bedrockagentruntime.model.RetrieveResponse;

import java.util.List;

@Service
public class BedrockKnowledgeBaseService {

    private final BedrockAgentRuntimeClient agentRuntimeClient;

    @Value("${aws.bedrock.knowledge-base-id}")
    private String knowledgeBaseId;

    public BedrockKnowledgeBaseService(BedrockAgentRuntimeClient agentRuntimeClient) {
        this.agentRuntimeClient = agentRuntimeClient;
    }

    public List<String> retrieveContext(String query) {
        RetrieveRequest retrieveRequest = RetrieveRequest.builder()
                .knowledgeBaseId(knowledgeBaseId)
                .retrievalQuery(KnowledgeBaseQuery.builder()
                        .text(query)
                        .build())
                .build();
        RetrieveResponse response = agentRuntimeClient.retrieve(retrieveRequest);
        return response.retrievalResults().stream()
                .map(result -> result.content().text())
                .toList();
    }
}