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

import com.coltran.ai.springaidesktop.infrastructure.database.ConversationRepository;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {
    
    private final ChatClient chatClient;
    private final ChatMemory chatMemory;
    private final ConversationRepository conversationRepository;

    public ChatController(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory, ConversationRepository conversationRepository) {

        this.chatMemory = chatMemory;
        this.conversationRepository = conversationRepository;

        this.chatClient = chatClientBuilder
            .defaultAdvisors(
                MessageChatMemoryAdvisor
                .builder(chatMemory)
                .build()
            )
            .build();

    }

    @PostMapping("/")
    public Map<String, String> generateResponse(@RequestBody Map<String, String> request) {

        String userMessage = request.get("message");

        if(userMessage.isBlank()) return Map.of("response", "No content");

        String conversationId = request.getOrDefault("conversationId", "default-conversation");

        var isNewConversation = conversationRepository.existsById(conversationId);
        if(isNewConversation) {
            String title = this.chatClient.prompt()
                .user("Summarize the following into a short 3-to-4 word title, no quotes, no extra text, focus on the core idea of this text: " + userMessage)
                .call()
                .content();
            
            conversationRepository.save(conversationId, title);
        }

        String response = this.chatClient.prompt()
            .user(userMessage)
            .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
            .call()
            .content();

        return Map.of("response", response);
    }

    @GetMapping("/history")
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

    public List<Map<String, Object>> listConversations() {
        return conversationRepository.list(50);
    }
}
