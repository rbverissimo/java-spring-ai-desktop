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

    public boolean existsById(String conversationId) {
        String sql = """
                SELECT COUNT(1) FROM conversations
                WHERE ID = :conversationId
                """;
        return jdbcClient.sql(sql)
            .param("conversationId", conversationId)
            .query(Integer.class)
            .single() > 0;
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


    public List<Map<String, Object>> list(int limit) {
        if(limit < 1) limit = 1;
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
