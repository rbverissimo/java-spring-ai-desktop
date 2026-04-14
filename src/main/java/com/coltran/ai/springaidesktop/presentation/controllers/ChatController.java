package com.coltran.ai.springaidesktop.presentation.controllers;

import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ChatController {
    
    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory) {

        this.chatClient = chatClientBuilder
            .defaultAdvisors(
                MessageChatMemoryAdvisor
                .builder(chatMemory)
                .build()
            )
            .build();

    }

    @PostMapping("/chat")
    public Map<String, String> generateResponse(@RequestBody Map<String, String> request) {

        String userMessage = request.get("message");

        if(userMessage.isBlank()) return Map.of("response", "No content");

        String conversationId = request.getOrDefault("conversationId", "default-conversation");

        String response = this.chatClient.prompt()
            .user(userMessage)
            .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
            .call()
            .content();

        return Map.of("response", response);
    }
}
