package com.coltran.ai.springaidesktop.infrastructure.database;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class ConversationRepository {

    private final JdbcClient jdbcClient;

    public ConversationRepository(JdbcClient jdbcClient){
        this.jdbcClient = jdbcClient;
    }

    public void save(String conversationId, String title) {
        String sql = """
            INSERT INTO conversations (id, title) 
            VALUES (:id, :title)
            ON CONFLICT(id) DO UPDATE SET 
            updated_at = CURRENT_TIMESTAMP
            """;
            
        jdbcClient.sql(sql)
            .param("id", conversationId)
            .param("title", title)
            .update();
    }


    public List<Map<String, Object>> getRecentConversations(int limit) {
        String sql = """
            SELECT id as conversationId, title, created_at, updated_at 
            FROM conversations 
            ORDER BY updated_at DESC 
            LIMIT :limit
            """;
            
        return jdbcClient.sql(sql)
            .param("limit", limit)
            .query().listOfRows();
    }
    
}
