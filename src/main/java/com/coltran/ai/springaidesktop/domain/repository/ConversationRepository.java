package com.coltran.ai.springaidesktop.domain.repository;

import java.util.List;

import com.coltran.ai.springaidesktop.domain.model.Conversation;

public interface ConversationRepository {
    boolean existsBy(String id);
    void save(Conversation conversation);
    List<Conversation> list(int limit);
}
