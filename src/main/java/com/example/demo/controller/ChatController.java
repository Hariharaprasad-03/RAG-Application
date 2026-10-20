package com.example.demo.controller;

import com.example.demo.service.ChatService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class ChatController {

    private static Logger LOGGER = LoggerFactory.getLogger(ChatController.class);
    private final ChatClient geminiClient;
    private final ChatClient openAiClient;
    private final ChatService chatService ;

    ChatController(@Qualifier("geminiClient") ChatClient geminiClient,
                   @Qualifier("openAiClient") ChatClient openAiClient,
                   ChatService chatService){
        this.geminiClient = geminiClient;
        this.openAiClient = openAiClient;
        this.chatService = chatService ;
    }

    @GetMapping("/gemini")
    public String askGemini(@RequestParam("message") String message) {
        LOGGER.info("Inside the gemini Chat contoller ");
        return chatService.chat(message);
    }

    @GetMapping("/openai")
    public String askOpenAi(@RequestParam String prompt) {
        return openAiClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}
