package com.coltran.ai.springaidesktop.domain.model;

import java.time.LocalDateTime;

public class Conversation {
    
    private String id;
    private String title;
    private LocalDateTime updatedAt;
    
    public Conversation(String id, String title, LocalDateTime updatedAt) {
        this.id = id;
        this.title = id;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void rename(String newTitle) {
        if(newTitle == null || newTitle.isBlank()){
            throw new IllegalArgumentException("A new valid title is required to rename conversation.");
        }

        this.title = newTitle;
        this.updatedAt = LocalDateTime.now();
    }
}
