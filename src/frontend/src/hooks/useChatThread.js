import { useState, useEffect } from 'react';
import { api } from '../api/client';
import { adaptChatHistory } from '../api/adapters/chatAdapter';

export function useChatThread(conversationId) {
  const [messages, setMessages] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [isFetchingHistory, setIsFetchingHistory] = useState(false);

  useEffect(() => {
    let isMounted = true;

    const fetchHistory = async () => {
      if (!conversationId) return;

      setIsFetchingHistory(true);
      try {
        const data = await api.get(`/api/chat/history?conversationId=${encodeURIComponent(conversationId)}`);
        if (isMounted) {
          console.log(data);
          const fetchedMessages = adaptChatHistory(data);
          setMessages(fetchedMessages);
        }
      } catch (error) {
        if (isMounted) {
          console.error('Failed to fetch chat history:', error);
          setMessages([]);
        }
      } finally {
        if (isMounted) {
          setIsFetchingHistory(false);
        }
      }
    };

    fetchHistory();

    return () => {
      isMounted = false;
    };
  }, [conversationId]);

  const sendMessage = async (input) => {
    if (!input || !input.trim()) return;

    const userMessage = { role: 'user', content: input };
    setMessages((prev) => [...prev, userMessage]);
    setIsLoading(true);

    try {
      const chatData = {
        message: userMessage.content,
        conversationId: conversationId || 'default-thread'
      };

      const data = await api.post('/api/chat/', chatData);

      const aiMessage = { role: 'assistant', content: data.response };
      setMessages((prev) => [...prev, aiMessage]);
    } catch (error) {
      console.error('Error:', error);
      const errorMessage = { role: 'error', content: 'Sorry, something went wrong. Please check your backend connection.' };
      setMessages((prev) => [...prev, errorMessage]);
    } finally {
      setIsLoading(false);
    }
  };

  return {
    messages,
    isLoading,
    isFetchingHistory,
    sendMessage
  };
}
