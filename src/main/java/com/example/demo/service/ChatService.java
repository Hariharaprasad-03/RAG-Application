package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ChatService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatService.class);
    private final ChatClient chatClient;
    private final BedrockKnowledgeBaseService knowledgeBaseService ;

    ChatService(@Qualifier("geminiClient") ChatClient geminiClient ,
                BedrockKnowledgeBaseService bedrockKnowledgeBaseService){
        this.knowledgeBaseService = bedrockKnowledgeBaseService;
        this.chatClient = geminiClient ;
    }
    public String chat(String userQuery) {
        LOGGER.info("Chat service chat method called & called knowledge base for retrival ");
        List<String> contextChunks = knowledgeBaseService.retrieveContext(userQuery);
        LOGGER.info("information retrived from knowledge base {}",contextChunks.toString());
        String context = contextChunks.isEmpty()
                ? "No reference documents found in the Knowledge Base."
                : String.join("\n---\n", contextChunks);

        return chatClient.prompt()
                .user(promptUserSpec -> promptUserSpec.text("""
                        Context:
                        {context}

                        Question:
                        {question}
                        """)
                        .param("context", context)
                        .param("question", userQuery))
                .call()
                .content();
    }
}
