
package com.example.demo.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class MultiModelConfig {

    @Value("classpath:prompts/system.st")
    private Resource systemPromptResource;

    @Bean("geminiClient")
    public ChatClient geminiChatClient(GoogleGenAiChatModel geminiModel) {
        return ChatClient.builder(geminiModel)
                .defaultSystem(systemPromptResource)
                .build();
    }

    @Bean("openAiClient")
    public ChatClient openAiChatClient(OpenAiChatModel openAiModel) {
        return ChatClient.builder(openAiModel)
                .defaultSystem(
                        "You are OpenAI GPT-4o-mini. Provide detailed technical steps."
                )
                .build();
    }
}