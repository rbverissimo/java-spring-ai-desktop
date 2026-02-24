CREATE TABLE IF NOT EXISTS spring_ai_chat_memory (
    conversation_id TEXT NOT NULL,
    content TEXT NOT NULL,
    type TEXT NOT NULL,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_chat_memory_conv_id 
ON spring_ai_chat_memory(conversation_id);