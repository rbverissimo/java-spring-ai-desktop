package com.coltran.ai.springaidesktop.presentation.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ChatController {
    
    private final ChatClient chatClient;

    private final ChatMemory chatMemory;

    public ChatController(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory) {

        this.chatMemory = chatMemory;

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

    @GetMapping("/chat/history")
    public List<Map<String, String>> getChatHistory(@RequestParam String conversationId) {
        List<Message> history = this.chatMemory.get(conversationId);

        return history.stream().map(message -> {
            String role = message.getMessageType() == MessageType.USER ? "user" : "assistant";
            String content = message.getText() != null ? message.getText() : "";
            return Map.of(
                "role", role,
                "content", content
            );
        }).toList();
    }
}
